package com.rajdeep.powerprep.mistakes;

/**
 * When a question is on the retest list (docs/pyq-plan.md).
 * It goes on the list when an answer is wrong, or right but marked "I guessed" / "I didn't know".
 * It leaves the list only after it is answered right, and not marked unsure, on two different days after that.
 * Days are counted in India time.
 */
public final class RetestRule {
    public static final int SURE_DAYS_TO_CLEAR = 2;

    private RetestRule() {}

    /**
     * @param hadBadAnswer  the question was ever wrong or marked unsure
     * @param sureDaysAfter distinct days with a right, sure answer after the last bad one
     */
    public static boolean isOpen(boolean hadBadAnswer, long sureDaysAfter) {
        return hadBadAnswer && sureDaysAfter < SURE_DAYS_TO_CLEAR;
    }
}
