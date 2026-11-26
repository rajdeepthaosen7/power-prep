package com.rajdeep.powerprep.today;

import com.rajdeep.powerprep.revision.ChapterProgress;
import java.time.Instant;

/** A studied chapter with its revision dates and what the revision card shows. */
public record RevisionRow(long userId, int chapterId, Instant studiedAt, Instant rev1At, Instant rev2At, Instant rev3At,
                          String chapterSlug, String chapterName, int chapterSort, String subjectSlug, String subjectName,
                          String summaryTopic, long openMistakes) {
    ChapterProgress progress() { return new ChapterProgress(userId, chapterId, studiedAt, rev1At, rev2At, rev3At); }
}
