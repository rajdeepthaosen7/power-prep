package com.rajdeep.powerprep.revision;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;

class RevisionScheduleTest {
    private static final Instant STUDIED = Instant.parse("2026-11-01T10:00:00Z");

    private static RevisionSchedule at(Instant now) { return new RevisionSchedule(Clock.fixed(now, ZoneOffset.UTC)); }

    @Test
    void roundsFallDueAfterOneSevenAndTwentyOneDays() {
        ProgressView v = at(STUDIED).view(new ChapterProgress(1, 1, STUDIED, null, null, null));
        assertThat(v.rounds()).extracting(ProgressView.Round::dueAt).containsExactly(
            STUDIED.plus(Duration.ofDays(1)), STUDIED.plus(Duration.ofDays(7)), STUDIED.plus(Duration.ofDays(21)));
        assertThat(v.nextRound()).isEqualTo(1);
        assertThat(v.isDue()).isFalse();
    }

    @Test
    void nextRoundIsTheFirstNotDoneAndIsDueOnceItsDayCame() {
        Instant now = STUDIED.plus(Duration.ofDays(8));
        ProgressView v = at(now).view(new ChapterProgress(1, 1, STUDIED, STUDIED.plus(Duration.ofDays(1)), null, null));
        assertThat(v.nextRound()).isEqualTo(2);
        assertThat(v.isDue()).isTrue();
    }

    @Test
    void allDoneHasNoNextRound() {
        ProgressView v = at(STUDIED).view(new ChapterProgress(1, 1, STUDIED, STUDIED, STUDIED, STUDIED));
        assertThat(v.nextRound()).isNull();
        assertThat(v.dueAt()).isNull();
        assertThat(v.isDue()).isNull();
    }
}
