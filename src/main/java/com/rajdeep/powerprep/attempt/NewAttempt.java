package com.rajdeep.powerprep.attempt;

import java.math.BigDecimal;

/**
 * One answer to one question, about to be saved. {@code testAttemptId} is set only inside a test.
 * {@code selfMark} is the user's own mark (knew / guessed / didn't know), null when not given.
 */
public record NewAttempt(long userId, int questionId, String chosenLabels, BigDecimal natValue, boolean correct,
                         Integer timeMs, AttemptMode mode, Long testAttemptId, SelfMark selfMark) {

    public NewAttempt(long userId, int questionId, String chosenLabels, BigDecimal natValue, boolean correct,
                      Integer timeMs, AttemptMode mode, Long testAttemptId) {
        this(userId, questionId, chosenLabels, natValue, correct, timeMs, mode, testAttemptId, null);
    }
}
