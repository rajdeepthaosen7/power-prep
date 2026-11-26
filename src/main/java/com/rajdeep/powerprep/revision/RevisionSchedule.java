package com.rajdeep.powerprep.revision;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

/** Spaced revision rule of the prep strategy: revise a chapter 1, 7 and 21 days after studying it. */
@Component
public class RevisionSchedule {
    static final int[] DAYS_AFTER_STUDY = {1, 7, 21};
    private final Clock clock;

    public RevisionSchedule(Clock clock) { this.clock = clock; }

    public Instant dueAt(ChapterProgress p, int round) {
        return p.studiedAt().plus(Duration.ofDays(DAYS_AFTER_STUDY[round - 1]));
    }

    public ProgressView view(ChapterProgress p) {
        List<ProgressView.Round> rounds = new ArrayList<>();
        for (int k = 1; k <= ChapterProgress.ROUNDS; k++) rounds.add(new ProgressView.Round(k, dueAt(p, k), p.doneAt(k)));
        Integer next = p.nextRound();
        if (next == null) return new ProgressView(p.studiedAt(), rounds, null, null, null);
        Instant due = dueAt(p, next);
        return new ProgressView(p.studiedAt(), rounds, next, due, !due.isAfter(clock.instant()));
    }
}
