package com.rajdeep.powerprep.question.grading;

/**
 * Grades one answer type. Add a new answer type by adding a new implementation (open/closed):
 * {@link AnswerGrading} picks the grader by {@link #type()}.
 */
public interface AnswerGrader {
    AnswerType type();

    /** @throws com.rajdeep.powerprep.common.error.BadRequestException when the answer is incomplete or invalid */
    boolean isCorrect(AnswerKey key, GivenAnswer answer);
}
