package com.rajdeep.powerprep.mocktest;

import com.rajdeep.powerprep.question.OptionRow;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MockTestRepository {
    List<TestList.Chapter> chaptersOf(String subjectSlug);
    List<TestList.Test> publishedTests(String subjectSlug);
    List<TestList.Attempt> attempts(long userId, String subjectSlug);

    Optional<TestMeta> publishedTest(int testId);
    Optional<Long> openAttempt(long userId, int testId);
    long createAttempt(long userId, int testId);
    Optional<TestAttemptRow> attempt(long attemptId, long userId);

    List<StartedTest.QuestionRow> questionsOf(int testId);
    List<Integer> questionIds(int testId);
    /** Options (with answer flags) of every question of the test, by question id. */
    Map<Integer, List<OptionRow>> optionsOf(int testId);
    Map<String, String> figuresOf(int testId);

    void saveScore(long attemptId, TestScore score, Long timeMs);
    List<TestResult.QuestionRow> resultQuestions(long attemptId, int testId);
    List<TestResult.HistoryEntry> history(long userId, int testId);
}
