package com.rajdeep.powerprep.mocktest;

import com.rajdeep.powerprep.attempt.AttemptMode;
import com.rajdeep.powerprep.attempt.AttemptRecorder;
import com.rajdeep.powerprep.attempt.NewAttempt;
import com.rajdeep.powerprep.common.error.BadRequestException;
import com.rajdeep.powerprep.common.error.ConflictException;
import com.rajdeep.powerprep.common.error.NotFoundException;
import com.rajdeep.powerprep.common.web.CurrentUser;
import com.rajdeep.powerprep.question.OptionRow;
import com.rajdeep.powerprep.question.OptionView;
import com.rajdeep.powerprep.question.QuestionBank;
import com.rajdeep.powerprep.question.SourceView;
import com.rajdeep.powerprep.question.grading.AnswerGrading;
import com.rajdeep.powerprep.question.grading.AnswerKey;
import com.rajdeep.powerprep.question.grading.AnswerType;
import com.rajdeep.powerprep.question.grading.GivenAnswer;
import java.math.BigDecimal;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Tests tab: chapter-wise mock tests in the APSC format (4-option MCQs, negative marking, timed).
 * Questions are sent without answers; answers, analysis and sources come back only after submitting.
 * The browser keeps the answers while the test runs and sends them once (fewer database writes).
 */
@Service
public class MockTestService {
    private final MockTestRepository tests;
    private final QuestionBank bank;
    private final AnswerGrading grading;
    private final AttemptRecorder recorder;
    private final Clock clock;

    public MockTestService(MockTestRepository tests, QuestionBank bank, AnswerGrading grading,
                           AttemptRecorder recorder, Clock clock) {
        this.tests = tests;
        this.bank = bank;
        this.grading = grading;
        this.recorder = recorder;
        this.clock = clock;
    }

    public TestList list(String subjectSlug, CurrentUser user) {
        List<TestList.Test> published = tests.publishedTests(subjectSlug);
        Map<Integer, List<TestList.Attempt>> attemptsByTest = user.isSignedIn() && !published.isEmpty()
            ? tests.attempts(user.id(), subjectSlug).stream()
                .collect(Collectors.groupingBy(TestList.Attempt::testId, LinkedHashMap::new, Collectors.toList()))
            : Map.of();
        List<TestList.Test> withAttempts = published.stream()
            .map(t -> t.withAttempts(attemptsByTest.getOrDefault(t.id(), List.of()))).toList();
        List<TestList.Chapter> chapters = tests.chaptersOf(subjectSlug).stream()
            .map(c -> new TestList.Chapter(c.id(), c.slug(), c.name(), c.sortOrder(),
                withAttempts.stream().filter(t -> t.chapterId() != null && t.chapterId() == c.id()).toList()))
            .toList();
        return new TestList(chapters, withAttempts.stream().filter(t -> t.chapterId() == null).toList());
    }

    /** Starts a test, or resumes the attempt that is still running. */
    @Transactional
    public StartedTest start(long userId, int testId) {
        TestMeta test = publishedTest(testId);
        long attemptId = tests.openAttempt(userId, testId).orElseGet(() -> tests.createAttempt(userId, testId));
        TestAttemptRow attempt = tests.attempt(attemptId, userId).orElseThrow();
        Map<Integer, List<OptionRow>> options = tests.optionsOf(testId);
        List<StartedTest.Question> questions = tests.questionsOf(testId).stream()
            .map(q -> new StartedTest.Question(q.id(), q.position(), q.stemMd(),
                options.getOrDefault(q.id(), List.of()).stream().map(OptionView::of).toList()))
            .toList();
        return new StartedTest(attemptId, attempt.startedAt().toString(), clock.instant().toString(), test, questions,
            tests.figuresOf(testId));
    }

    public record Answer(int questionId, String chosen, Integer timeMs, Boolean marked) {}

    /** Grades all answers with negative marking, saves them, and returns the full result. */
    @Transactional
    public TestResult submit(long userId, long attemptId, List<Answer> answers, Long timeMs) {
        TestAttemptRow attempt = ownAttempt(attemptId, userId);
        if (!attempt.isInProgress()) throw new ConflictException("this test is already submitted");
        TestMeta test = publishedTest(attempt.testId());
        Map<Integer, List<OptionRow>> options = tests.optionsOf(attempt.testId());

        Map<Integer, Answer> given = new HashMap<>();
        if (answers != null) answers.forEach(a -> given.put(a.questionId(), a));

        int correct = 0, wrong = 0, skipped = 0;
        for (int qid : tests.questionIds(attempt.testId())) {
            Answer a = given.get(qid);
            GivenAnswer answer = GivenAnswer.of(a == null || a.chosen() == null ? List.of() : List.of(a.chosen()), null);
            if (!answer.hasOptions()) {
                skipped++;
                continue;
            }
            boolean right;
            try {
                right = grading.isCorrect(mcqKey(options.getOrDefault(qid, List.of())), answer);
            } catch (BadRequestException e) {
                throw new BadRequestException("unknown option for question " + qid);
            }
            if (right) correct++; else wrong++;
            recorder.record(new NewAttempt(userId, qid, answer.chosenLabels(), null, right, a.timeMs(), AttemptMode.TEST, attemptId));
        }
        tests.saveScore(attemptId, TestScore.of(test.marksCorrect(), test.marksWrong(), correct, wrong, skipped), timeMs);
        return result(userId, attemptId);
    }

    public TestResult result(long userId, long attemptId) {
        TestAttemptRow a = ownAttempt(attemptId, userId);
        if (!a.isSubmitted()) throw new ConflictException("this test is not submitted yet");
        List<TestResult.QuestionRow> rows = tests.resultQuestions(attemptId, a.testId());
        List<Integer> ids = rows.stream().map(TestResult.QuestionRow::id).toList();
        Map<Integer, List<OptionRow>> options = bank.options(ids);
        Map<Integer, List<SourceView>> sources = bank.sources(ids);
        List<TestResult.Question> questions = rows.stream().map(q -> {
            List<OptionRow> opts = options.getOrDefault(q.id(), List.of());
            return new TestResult.Question(q, opts.stream().map(OptionView::of).toList(),
                QuestionBank.correctLabels(opts), sources.getOrDefault(q.id(), List.of()));
        }).toList();
        int nCorrect = a.nCorrect() == null ? 0 : a.nCorrect(), nWrong = a.nWrong() == null ? 0 : a.nWrong();
        return new TestResult(attemptId, publishedTest(a.testId()), a.startedAt(), a.submittedAt(), a.timeMs(),
            a.score(), a.maxScore(), a.nCorrect(), a.nWrong(), a.nUnattempted(), TestScore.accuracyPct(nCorrect, nWrong),
            topicScores(rows), questions, tests.figuresOf(a.testId()), tests.history(userId, a.testId()));
    }

    // ------------------------------------------------------------------ helpers

    private TestMeta publishedTest(int testId) {
        return tests.publishedTest(testId).orElseThrow(() -> new NotFoundException("test not found"));
    }

    private TestAttemptRow ownAttempt(long attemptId, long userId) {
        return tests.attempt(attemptId, userId).orElseThrow(() -> new NotFoundException("attempt not found"));
    }

    /** Test questions are graded as single-answer MCQs, whatever their study type. */
    private static AnswerKey mcqKey(List<OptionRow> options) {
        Set<String> valid = options.stream().map(OptionRow::label).collect(Collectors.toSet());
        Set<String> right = options.stream().filter(OptionRow::correct).map(OptionRow::label).collect(Collectors.toSet());
        return new AnswerKey(AnswerType.MCQ, valid, right, null, null);
    }

    private static List<TestResult.TopicScore> topicScores(List<TestResult.QuestionRow> rows) {
        record Tally(String slug, String name, int sort, int[] n) {}   // n = total, correct, wrong, skipped
        Map<String, Tally> byTopic = new LinkedHashMap<>();
        for (TestResult.QuestionRow q : rows) {
            Tally t = byTopic.computeIfAbsent(q.topicSlug(), k -> new Tally(k, q.topic(), q.topicSort(), new int[4]));
            t.n()[0]++;
            t.n()[q.chosen() == null ? 3 : Boolean.TRUE.equals(q.correct()) ? 1 : 2]++;
        }
        List<TestResult.TopicScore> out = new ArrayList<>();
        byTopic.values().forEach(t -> out.add(new TestResult.TopicScore(t.slug(), t.name(), t.sort(), t.n()[0], t.n()[1], t.n()[2], t.n()[3])));
        out.sort(Comparator.comparingInt(TestResult.TopicScore::sort));
        return out;
    }
}
