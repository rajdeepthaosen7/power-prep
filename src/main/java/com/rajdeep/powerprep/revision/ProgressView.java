package com.rajdeep.powerprep.revision;

import java.time.Instant;
import java.util.List;

/** Revision state of a chapter as the app shows it. {@code nextRound}, {@code dueAt}, {@code isDue} are null when all rounds are done. */
public record ProgressView(Instant studiedAt, List<Round> rounds, Integer nextRound, Instant dueAt, Boolean isDue) {
    public record Round(int round, Instant dueAt, Instant doneAt) {}
}
