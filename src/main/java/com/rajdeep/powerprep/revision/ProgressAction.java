package com.rajdeep.powerprep.revision;

import com.rajdeep.powerprep.common.error.BadRequestException;
import java.util.Locale;

/** studied = start the 1/7/21-day clock; revised = finish the next due round; reset = forget the chapter. */
public enum ProgressAction {
    STUDIED, REVISED, RESET;

    public static ProgressAction parse(String value) {
        try {
            return valueOf(value.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new BadRequestException("action must be studied, revised or reset");
        }
    }
}
