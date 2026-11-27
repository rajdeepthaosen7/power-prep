package com.rajdeep.powerprep.mocktest;

import com.rajdeep.powerprep.attempt.AttemptMode;
import com.rajdeep.powerprep.attempt.AttemptRecorder;
import com.rajdeep.powerprep.attempt.NewAttempt;
import com.rajdeep.powerprep.attempt.SelfMark;
import com.rajdeep.powerprep.common.error.BadRequestException;
import com.rajdeep.powerprep.common.error.ConflictException;
import com.rajdeep.powerprep.common.error.NotFoundException;
import com.rajdeep.powerprep.common.web.CurrentUser;
import com.rajdeep.powerprep.mocktest.ScoredAnswer.Outcome;
import com.rajdeep.powerprep.question.AnswerReview;
import com.rajdeep.powerprep.question.OptionRow;
import com.rajdeep.powerprep.question.OptionView;
import com.rajdeep.powerprep.question.QuestionBank;
import com.rajdeep.powerprep.question.SourceView;
import com.rajdeep.powerprep.question.TopicLink;
import com.rajdeep.powerprep.question.grading.AnswerGrading;
import com.rajdeep.powerprep.question.grading.AnswerType;
import com.rajdeep.powerprep.question.grading.GivenAnswer;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Tests tab: chapter mocks and the user's own PYQ selections (GATE papers, subject / chapter sets).
 *
 * <p>TEST mode: questions are sent without answers; the browser keeps the answers while the test runs and sends them once
 * (fewer database writes); answers, analysis and sources come back only after submitting.
 * PREPARE mode: each answer is sent on its own and its solution comes back at once; "submit" then only adds up the score.
 *
 * <p>APSC style asks every question as a single-answer MCQ, +1 / -0.25. GATE style keeps MCQ / MSQ / NAT and GATE marks.
 * See {@link AskedQuestion} and {@link TestScore}.
 */
@Service
public class MockTestService {
    private static final BigDecimal APSC_RIGHT = BigDecimal.ONE, APSC_WRONG = new BigDecimal("0.25");
    private static final BigDecimal APSC_MIN_PER_QUESTION = new BigDecimal("1.2"), GATE_MIN_PER_MARK = new BigDecimal("1.8");

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

    /** Starts a test, or resumes the attempt of the same mode and style that is still running. */
    @Transactional
    public StartedTest start(long userId, int testId, TestMode mode, TestStyle style) {
        TestMeta test = publishedTest(testId, userId);
        long attemptId = tests.openAttempt(userId, testId, mode, style)
            .orElseGet(() -> tests.createAttempt(userId, testId, mode, style));
        TestAttemptRow attempt = tests.attempt(attemptId, userId).orElseThrow();
        List<StartedTest.QuestionRow> rows = tests.questionsOf(testId);
        Map<Integer, AskedQuestion> asked = asked(testId, style, rows);
        List<StartedTest.Question> questions = rows.stream().map(q -> {
            AskedQuestion a = asked.get(q.id());
            return new StartedTest.Question(q.id(), q.position(), q.stemMd(), a.options().stream().map(OptionView::of).toList(),
                a.askedAs().name(), q.marks());
        }).toList();
        List<PreparedAnswer> answered = mode == TestMode.PREPARE ? preparedAnswers(attemptId, asked) : List.of();
        return new StartedTest(attemptId, attempt.startedAt().toString(), clock.instant().toString(), test, mode.name(),
            style.name(), timeLimitMin(test, style, rows), questions, tests.figuresOf(testId), answered);
    }

    /**
     * One answer. {@code chosen} is the single label of an MCQ (kept for the chapter-mock screen); {@code chosenLabels}
     * carries MSQ labels; {@code natValue} a typed number. {@code selfMark}: KNEW, GUESSED or DONT_KNOW (optional).
     */
    public record Answer(int questionId, String chosen, List<String> chosenLabels, BigDecimal natValue, Integer timeMs,
                         Boolean marked, String selfMark) {
        GivenAnswer given() {
            List<String> labels = chosenLabels != null ? chosenLabels : chosen == null ? List.of() : List.of(chosen);
            return GivenAnswer.of(labels, natValue);
        }
    }

    /** PREPARE mode: grades and saves one answer, and returns its solution at once. */
    @Transactional
    public PreparedAnswer answer(long userId, long attemptId, Answer a) {
        TestAttemptRow attempt = ownAttempt(attemptId, userId);
        if (!attempt.isInProgress()) throw new ConflictException("this test is already submitted");
        if (attempt.testMode() != TestMode.PREPARE) throw new BadRequestException("answers are sent one by one only in prepare mode");
        List<StartedTest.QuestionRow> rows = tests.questionsOf(attempt.testId());
        if (rows.stream().noneMatch(q -> q.id() == a.questionId())) throw new NotFoundException("question not in this test");
        if (tests.answeredIn(attemptId).stream().anyMatch(r -> r.questionId() == a.questionId()))
            throw new ConflictException("this question is already answered in this attempt");
        AskedQuestion asked = asked(attempt.testId(), attempt.testStyle(), rows).get(a.questionId());
        GivenAnswer given = a.given();
        if (isBlank(asked, given)) throw new BadRequestException("choose an option or type a number first");
        boolean right = grade(asked, given);
        SelfMark mark = SelfMark.parseOrNull(a.selfMark());
        recorder.record(new NewAttempt(userId, a.questionId(), labelsOf(asked, given), natOf(asked, given), right, a.timeMs(),
            AttemptMode.PRACTICE, attemptId, mark));
        return prepared(asked, new MockTestRepository.AnsweredRow(a.questionId(), labelsOf(asked, given), natOf(asked, given), right,
            mark == null ? null : mark.name()));
    }

    /** TEST mode: grades all answers and saves them. PREPARE mode: adds up the answers already saved. Then returns the result. */
    @Transactional
    public TestResult submit(long userId, long attemptId, List<Answer> answers, Long timeMs) {
        TestAttemptRow attempt = ownAttempt(attemptId, userId);
        if (!attempt.isInProgress()) throw new ConflictException("this test is already submitted");
        TestMeta test = publishedTest(attempt.testId(), userId);
        List<StartedTest.QuestionRow> rows = tests.questionsOf(attempt.testId());
        Map<Integer, AskedQuestion> asked = asked(attempt.testId(), attempt.testStyle(), rows);

        List<ScoredAnswer> scored = new ArrayList<>();
        if (attempt.testMode() == TestMode.PREPARE) {
            Map<Integer, MockTestRepository.AnsweredRow> done = new HashMap<>();
            tests.answeredIn(attemptId).forEach(r -> done.put(r.questionId(), r));
            for (StartedTest.QuestionRow q : rows) {
                MockTestRepository.AnsweredRow r = done.get(q.id());
                Outcome o = r == null ? Outcome.SKIPPED : r.correct() ? Outcome.CORRECT : Outcome.WRONG;
                scored.add(new ScoredAnswer(asked.get(q.id()).askedAs(), q.marks(), o));
            }
        } else {
            Map<Integer, Answer> given = new HashMap<>();
            if (answers != null) answers.forEach(a -> given.put(a.questionId(), a));
            for (StartedTest.QuestionRow q : rows) {
                AskedQuestion aq = asked.get(q.id());
                Answer a = given.get(q.id());
                GivenAnswer g = a == null ? GivenAnswer.of(List.of(), null) : a.given();
                if (isBlank(aq, g)) {
                    scored.add(new ScoredAnswer(aq.askedAs(), q.marks(), Outcome.SKIPPED));
                    continue;
                }
                boolean right = grade(aq, g);
                recorder.record(new NewAttempt(userId, q.id(), labelsOf(aq, g), natOf(aq, g), right, a.timeMs(), AttemptMode.TEST,
                    attemptId, SelfMark.parseOrNull(a.selfMark())));
                scored.add(new ScoredAnswer(aq.askedAs(), q.marks(), right ? Outcome.CORRECT : Outcome.WRONG));
            }
        }
        tests.saveScore(attemptId, score(attempt.testStyle(), test, scored), timeMs);
        return result(userId, attemptId);
    }

    public TestResult result(long userId, long attemptId) {
        TestAttemptRow a = ownAttempt(attemptId, userId);
        if (!a.isSubmitted()) throw new ConflictException("this test is not submitted yet");
        List<TestResult.QuestionRow> rows = tests.resultQuestions(attemptId, a.testId());
        List<Integer> ids = rows.stream().map(TestResult.QuestionRow::id).toList();
        Map<Integer, AskedQuestion> asked = asked(a.testId(), a.testStyle(), tests.questionsOf(a.testId()));
        Map<Integer, List<SourceView>> sources = bank.sources(ids);
        Map<Integer, TopicLink> links = bank.topicLinks(ids);
        List<TestResult.Question> questions = rows.stream().map(q -> {
            AskedQuestion aq = asked.get(q.id());
            return new TestResult.Question(q, aq.askedAs().name(), aq.options().stream().map(OptionView::of).toList(),
                aq.key().correctLabels().stream().sorted().toList(), sources.getOrDefault(q.id(), List.of()), links.get(q.id()));
        }).toList();
        int nCorrect = a.nCorrect() == null ? 0 : a.nCorrect(), nWrong = a.nWrong() == null ? 0 : a.nWrong();
        return new TestResult(attemptId, publishedTest(a.testId(), userId), a.testMode().name(), a.testStyle().name(),
            a.startedAt(), a.submittedAt(), a.timeMs(), a.score(), a.maxScore(), a.nCorrect(), a.nWrong(), a.nUnattempted(),
            TestScore.accuracyPct(nCorrect, nWrong), topicScores(rows), questions, tests.figuresOf(a.testId()),
            tests.history(userId, a.testId()));
    }

    // ------------------------------------------------------------------ helpers

    private TestMeta publishedTest(int testId, long userId) {
        return tests.publishedTest(testId, userId).orElseThrow(() -> new NotFoundException("test not found"));
    }

    private TestAttemptRow ownAttempt(long attemptId, long userId) {
        return tests.attempt(attemptId, userId).orElseThrow(() -> new NotFoundException("attempt not found"));
    }

    /** How each question of the test is asked in this style. */
    private Map<Integer, AskedQuestion> asked(int testId, TestStyle style, List<StartedTest.QuestionRow> rows) {
        Map<Integer, List<OptionRow>> options = tests.optionsOf(testId);
        Map<Integer, List<OptionRow>> mcq = tests.mcqOptionsOf(testId);
        Map<Integer, AskedQuestion> out = new LinkedHashMap<>();
        for (StartedTest.QuestionRow q : rows) {
            out.put(q.id(), AskedQuestion.of(style, q.id(), AnswerType.valueOf(q.answerType()), options.get(q.id()),
                mcq.get(q.id()), q.natMin(), q.natMax()));
        }
        return out;
    }

    private static boolean isBlank(AskedQuestion q, GivenAnswer g) {
        return q.askedAs() == AnswerType.NAT ? g.natValue() == null : !g.hasOptions();
    }

    private boolean grade(AskedQuestion q, GivenAnswer g) {
        try {
            return grading.isCorrect(q.key(), g);
        } catch (BadRequestException e) {
            throw new BadRequestException("unknown option for question " + q.questionId());
        }
    }

    private static String labelsOf(AskedQuestion q, GivenAnswer g) { return q.askedAs() == AnswerType.NAT ? null : g.chosenLabels(); }

    private static BigDecimal natOf(AskedQuestion q, GivenAnswer g) { return q.askedAs() == AnswerType.NAT ? g.natValue() : null; }

    private List<PreparedAnswer> preparedAnswers(long attemptId, Map<Integer, AskedQuestion> asked) {
        return tests.answeredIn(attemptId).stream().filter(r -> asked.containsKey(r.questionId()))
            .map(r -> prepared(asked.get(r.questionId()), r)).toList();
    }

    private PreparedAnswer prepared(AskedQuestion q, MockTestRepository.AnsweredRow r) {
        AnswerReview review = bank.review(q.questionId());
        List<SourceView> sources = bank.sources(List.of(q.questionId())).getOrDefault(q.questionId(), List.of());
        TopicLink link = bank.topicLinks(List.of(q.questionId())).get(q.questionId());
        return new PreparedAnswer(q.questionId(), r.chosenLabels(), r.natValue(), r.correct(), r.selfMark(),
            q.key().correctLabels().stream().sorted().toList(), review.natMin(), review.natMax(), review.solutionMd(),
            review.answerStatus(), sources, link);
    }

    private static TestScore score(TestStyle style, TestMeta test, List<ScoredAnswer> scored) {
        if (style == TestStyle.GATE) return TestScore.gate(scored);
        int correct = 0, wrong = 0, skipped = 0;
        for (ScoredAnswer s : scored) {
            switch (s.outcome()) {
                case CORRECT -> correct++;
                case WRONG -> wrong++;
                case SKIPPED -> skipped++;
            }
        }
        BigDecimal right = test.marksCorrect() == null ? APSC_RIGHT : test.marksCorrect();
        BigDecimal minus = test.marksWrong() == null ? APSC_WRONG : test.marksWrong();
        return TestScore.of(right, minus, correct, wrong, skipped);
    }

    /** APSC style: the test's own time, else 1.2 min per question. GATE style: 1.8 min per mark (100 marks = 3 h). */
    private static int timeLimitMin(TestMeta test, TestStyle style, List<StartedTest.QuestionRow> rows) {
        if (style == TestStyle.APSC) {
            if (test.durationMin() != null) return test.durationMin();
            return APSC_MIN_PER_QUESTION.multiply(BigDecimal.valueOf(rows.size())).setScale(0, RoundingMode.CEILING).intValue();
        }
        BigDecimal marks = rows.stream().map(r -> r.marks() == null ? BigDecimal.ONE : r.marks()).reduce(BigDecimal.ZERO, BigDecimal::add);
        return Math.max(1, GATE_MIN_PER_MARK.multiply(marks).setScale(0, RoundingMode.CEILING).intValue());
    }

    private static List<TestResult.TopicScore> topicScores(List<TestResult.QuestionRow> rows) {
        record Tally(String slug, String name, int sort, int[] n) {}   // n = total, correct, wrong, skipped
        Map<String, Tally> byTopic = new LinkedHashMap<>();
        for (TestResult.QuestionRow q : rows) {
            Tally t = byTopic.computeIfAbsent(q.topicSlug(), k -> new Tally(k, q.topic(), q.topicSort(), new int[4]));
            t.n()[0]++;
            t.n()[q.correct() == null ? 3 : Boolean.TRUE.equals(q.correct()) ? 1 : 2]++;
        }
        List<TestResult.TopicScore> out = new ArrayList<>();
        byTopic.values().forEach(t -> out.add(new TestResult.TopicScore(t.slug(), t.name(), t.sort(), t.n()[0], t.n()[1], t.n()[2], t.n()[3])));
        out.sort(Comparator.comparingInt(TestResult.TopicScore::sort));
        return out;
    }
}
