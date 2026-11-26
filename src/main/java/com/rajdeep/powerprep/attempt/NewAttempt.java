package com.rajdeep.powerprep.attempt;

import java.math.BigDecimal;

/** One answer to one question, about to be saved. {@code testAttemptId} is set only inside a mock test. */
public record NewAttempt(long userId, int questionId, String chosenLabels, BigDecimal natValue, boolean correct,
                         Integer timeMs, AttemptMode mode, Long testAttemptId) {}
