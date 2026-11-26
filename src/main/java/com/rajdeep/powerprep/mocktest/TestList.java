package com.rajdeep.powerprep.mocktest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/** Tests tab of a subject: chapter-wise tests, and whole-subject tests. Each test carries the user's attempts. */
public record TestList(List<Chapter> chapters, List<Test> subjectTests) {

    public record Chapter(int id, String slug, String name, int sortOrder, List<Test> tests) {}

    public record Test(int id, String code, String title, String variant, String description, Integer durationMin,
                       BigDecimal marksCorrect, BigDecimal marksWrong, Integer chapterId, long questions,
                       List<Attempt> attempts) {
        Test withAttempts(List<Attempt> a) {
            return new Test(id, code, title, variant, description, durationMin, marksCorrect, marksWrong, chapterId, questions, a);
        }
    }

    public record Attempt(long id, int testId, String status, Instant startedAt, Instant submittedAt, BigDecimal score,
                          BigDecimal maxScore, Integer nCorrect, Integer nWrong, Integer nSkipped) {}
}
