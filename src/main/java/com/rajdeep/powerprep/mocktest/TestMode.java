package com.rajdeep.powerprep.mocktest;

import com.rajdeep.powerprep.common.error.BadRequestException;

/** TEST: timed, answers and solutions only after submit. PREPARE: one question at a time, solution right after each answer. */
public enum TestMode {
    TEST, PREPARE;

    public static TestMode parseOrTest(String value) {
        if (value == null || value.isBlank()) return TEST;
        try {
            return valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("mode must be TEST or PREPARE");
        }
    }
}
