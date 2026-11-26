package com.rajdeep.powerprep.mocktest;

import java.math.BigDecimal;
import java.time.Instant;

public record TestAttemptRow(long id, long userId, int testId, String status, Instant startedAt, Instant submittedAt,
                             BigDecimal score, BigDecimal maxScore, Integer nCorrect, Integer nWrong, Integer nUnattempted,
                             Long timeMs) {
    public static final String IN_PROGRESS = "IN_PROGRESS", SUBMITTED = "SUBMITTED";

    public boolean isSubmitted() { return SUBMITTED.equals(status); }
    public boolean isInProgress() { return IN_PROGRESS.equals(status); }
}
