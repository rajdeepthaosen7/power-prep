package com.rajdeep.powerprep.revision;

import java.util.Optional;

public interface ChapterProgressRepository {
    boolean chapterExists(int chapterId);
    Optional<ChapterProgress> find(long userId, int chapterId);
    void markStudied(long userId, int chapterId);
    void markRevised(long userId, int chapterId, int round);
    void delete(long userId, int chapterId);

    /** Marks as studied every chapter whose written exam questions the user has all attempted. */
    void markFullyAttemptedChapters(long userId);
}
