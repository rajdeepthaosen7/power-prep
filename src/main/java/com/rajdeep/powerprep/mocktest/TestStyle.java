package com.rajdeep.powerprep.mocktest;

import com.rajdeep.powerprep.common.error.BadRequestException;

/**
 * How a test asks and marks its questions.
 * APSC: every question as a single-answer MCQ (NAT and MSQ turned into MCQ when we have such options), +1 / -0.25,
 * 1.2 min per question. GATE: the original format (MCQ, MSQ, NAT) and GATE marks, 1.8 min per mark (100 marks = 3 h).
 */
public enum TestStyle {
    APSC, GATE;

    public static TestStyle parseOrApsc(String value) {
        if (value == null || value.isBlank()) return APSC;
        try {
            return valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("style must be APSC or GATE");
        }
    }
}
