package com.rajdeep.powerprep.today;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.rajdeep.powerprep.revision.ProgressView;
import java.math.BigDecimal;
import java.util.List;

/** The Today screen: what to do next on each track, revisions, open mistakes, mocks to retake, recent activity. */
public record TodayPlan(List<Track> tracks, List<Revision> revisionsDue, List<Revision> revisionsUpcoming,
                        MistakeCounts mistakes, List<MockToRetake> mocks, int mockTargetPct, Activity activity) {

    /**
     * One subject track: the next chapter to study (and its first topic with unattempted questions),
     * or {@code allStudied = true} when every chapter with notes is studied.
     */
    public record Track(String subjectSlug, String subjectName, Integer id, String slug, String name, Integer sortOrder,
                        Long ready, Long attempted, Boolean studied, TopicLink topic, Boolean allStudied, int chaptersReady) {
        static Track nextChapter(TrackChapter c, TopicLink topic, int chaptersReady) {
            return new Track(c.subjectSlug(), c.subjectName(), c.id(), c.slug(), c.name(), c.sortOrder(),
                c.ready(), c.attempted(), c.studied(), topic, null, chaptersReady);
        }

        static Track finished(TrackChapter c, int chaptersReady) {
            return new Track(c.subjectSlug(), c.subjectName(), null, null, null, null, null, null, null, null, true, chaptersReady);
        }
    }

    public record TopicLink(String slug, String name) {}

    public record Revision(@JsonUnwrapped ProgressView progress, int chapterId, String chapterSlug, String chapterName,
                           int chapterSort, String subjectSlug, String subjectName, String summaryTopic, long openMistakes) {}

    public record MistakeCounts(long open, long untagged) {}

    public record MockToRetake(int id, String title, String variant, String chapterName, int chapterSort, String subjectSlug,
                               String subjectName, BigDecimal bestPct, long questions, Integer durationMin) {}

    public record Activity(long today, long week, long weekCorrect) {}
}
