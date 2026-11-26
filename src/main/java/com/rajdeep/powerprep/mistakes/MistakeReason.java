package com.rajdeep.powerprep.mistakes;

import com.rajdeep.powerprep.common.error.BadRequestException;
import java.util.Locale;

/** Why the user got a question wrong (one tap in the mistake log). */
public enum MistakeReason {
    CONCEPT, FORMULA, CALCULATION, MISREAD, GUESS, TIME;

    /** null or blank = no reason (clears it). */
    public static MistakeReason parseOrNull(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return valueOf(value.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("unknown reason");
        }
    }
}
