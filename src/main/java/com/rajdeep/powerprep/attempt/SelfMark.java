package com.rajdeep.powerprep.attempt;

import com.rajdeep.powerprep.common.error.BadRequestException;

/**
 * The user's own mark after answering a question. A right answer that was a guess is not knowing,
 * so GUESSED and DONT_KNOW keep the question in the retest list even when the answer was right.
 */
public enum SelfMark {
    KNEW, GUESSED, DONT_KNOW;

    /** Null or blank means "not marked". Anything else must be one of the three values. */
    public static SelfMark parseOrNull(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("selfMark must be KNEW, GUESSED or DONT_KNOW");
        }
    }

    /** True when the user was not sure (a guess or did not know). */
    public boolean isUnsure() { return this != KNEW; }
}
