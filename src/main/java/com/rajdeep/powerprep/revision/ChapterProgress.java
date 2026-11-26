package com.rajdeep.powerprep.revision;

import java.time.Instant;

/** A chapter the user has studied, and when each of its three revisions was done (null = not yet). */
public record ChapterProgress(long userId, int chapterId, Instant studiedAt, Instant rev1At, Instant rev2At, Instant rev3At) {
    public static final int ROUNDS = 3;

    public Instant doneAt(int round) {
        return switch (round) {
            case 1 -> rev1At;
            case 2 -> rev2At;
            case 3 -> rev3At;
            default -> throw new IllegalArgumentException("round " + round);
        };
    }

    /** The next revision round to do (1..3), or null when all three are done. */
    public Integer nextRound() {
        for (int k = 1; k <= ROUNDS; k++) if (doneAt(k) == null) return k;
        return null;
    }
}
