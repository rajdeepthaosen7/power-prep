package com.rajdeep.powerprep.attempt;

/** Where an attempt was made. Unknown values from the client count as STUDY. */
public enum AttemptMode {
    STUDY, PRACTICE, TEST, REVISION;

    public static AttemptMode parseOrStudy(String value) {
        if (value == null) return STUDY;
        try {
            return valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return STUDY;
        }
    }
}
