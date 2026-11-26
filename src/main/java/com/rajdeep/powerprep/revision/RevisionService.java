package com.rajdeep.powerprep.revision;

import com.rajdeep.powerprep.common.error.ConflictException;
import com.rajdeep.powerprep.common.error.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Spaced revision of chapters: studied, then revised after 1, 7 and 21 days. */
@Service
public class RevisionService {
    private final ChapterProgressRepository progress;
    private final RevisionSchedule schedule;

    public RevisionService(ChapterProgressRepository progress, RevisionSchedule schedule) {
        this.progress = progress;
        this.schedule = schedule;
    }

    /** Revision state of a chapter, or null if the user has not studied it. */
    public ProgressView progressOf(long userId, int chapterId) {
        return progress.find(userId, chapterId).map(schedule::view).orElse(null);
    }

    @Transactional
    public ProgressView apply(long userId, int chapterId, ProgressAction action) {
        if (!progress.chapterExists(chapterId)) throw new NotFoundException("chapter not found");
        switch (action) {
            case STUDIED -> progress.markStudied(userId, chapterId);
            case REVISED -> {
                ChapterProgress p = progress.find(userId, chapterId)
                    .orElseThrow(() -> new ConflictException("mark the chapter as studied first"));
                Integer round = p.nextRound();
                if (round == null) throw new ConflictException("all three revisions are done");
                progress.markRevised(userId, chapterId, round);
            }
            case RESET -> progress.delete(userId, chapterId);
        }
        return progressOf(userId, chapterId);
    }

    /** A chapter counts as studied once every one of its written exam questions has been attempted. */
    public void markFullyAttemptedChapters(long userId) {
        progress.markFullyAttemptedChapters(userId);
    }
}
