package com.rajdeep.powerprep.mocktest;

import com.rajdeep.powerprep.question.OptionRow;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MockTestRepository {
    List<TestList.Chapter> chaptersOf(String subjectSlug);
    List<TestList.Test> publishedTests(String subjectSlug);
    List<TestList.Attempt> attempts(long userId, String subjectSlug);

    /** A published test the user may take: a shared mock, or the user's own PYQ selection. */
    Optional<TestMeta> publishedTest(int testId, long userId);
    Optional<Long> openAttempt(long userId, int testId, TestMode mode, TestStyle style);
    long createAttempt(long userId, int testId, TestMode mode, TestStyle style);
    Optional<TestAttemptRow> attempt(long attemptId, long userId);

    List<StartedTest.QuestionRow> questionsOf(int testId);
    List<Integer> questionIds(int testId);
    /** Options (with answer flags) of every question of the test, by question id. */
    Map<Integer, List<OptionRow>> optionsOf(int testId);
    /** Single-answer MCQ versions of the test's MSQ questions, by question id. */
    Map<Integer, List<OptionRow>> mcqOptionsOf(int testId);
    /** Answers already saved inside this test attempt (PREPARE mode saves them one by one). */
    List<AnsweredRow> answeredIn(long attemptId);
    Map<String, String> figuresOf(int testId);

    record AnsweredRow(int questionId, String chosenLabels, java.math.BigDecimal natValue, boolean correct, String selfMark) {}

    void saveScore(long attemptId, TestScore score, Long timeMs);
    List<TestResult.QuestionRow> resultQuestions(long attemptId, int testId);
    List<TestResult.HistoryEntry> history(long userId, int testId);
}
