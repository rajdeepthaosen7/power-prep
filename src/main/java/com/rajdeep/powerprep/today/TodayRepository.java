package com.rajdeep.powerprep.today;

import java.util.List;
import java.util.Optional;

public interface TodayRepository {
    /** Chapters with notes, in study order: Circuits first, then Power Systems (the two tracks), then the rest. */
    List<TrackChapter> chaptersWithNotes(long userId);
    /** First topic of the chapter that still has exam questions the user has not attempted. */
    Optional<TodayPlan.TopicLink> firstUnattemptedTopic(int chapterId, long userId);
    List<RevisionRow> studiedChapters(long userId);
    TodayPlan.MistakeCounts mistakeCounts(long userId);
    /** Mixed mocks of studied chapters, with the user's best score in percent (null if never taken). */
    List<TodayPlan.MockToRetake> mixedMocksOfStudiedChapters(long userId);
    TodayPlan.Activity activity(long userId);
}
