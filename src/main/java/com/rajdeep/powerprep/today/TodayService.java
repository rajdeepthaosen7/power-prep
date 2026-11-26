package com.rajdeep.powerprep.today;

import com.rajdeep.powerprep.revision.ProgressView;
import com.rajdeep.powerprep.revision.RevisionSchedule;
import com.rajdeep.powerprep.revision.RevisionService;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/** The daily loop of the prep strategy, as one screen. */
@Service
public class TodayService {
    /** A chapter is done when its mixed mock reaches this score. */
    static final int MOCK_TARGET_PCT = 85;
    static final int UPCOMING_REVISIONS = 3;

    private final TodayRepository today;
    private final RevisionService revision;
    private final RevisionSchedule schedule;

    public TodayService(TodayRepository today, RevisionService revision, RevisionSchedule schedule) {
        this.today = today;
        this.revision = revision;
        this.schedule = schedule;
    }

    public TodayPlan plan(long userId) {
        revision.markFullyAttemptedChapters(userId);

        List<TodayPlan.Revision> due = new ArrayList<>(), upcoming = new ArrayList<>();
        for (RevisionRow r : today.studiedChapters(userId)) {
            ProgressView v = schedule.view(r.progress());
            if (v.nextRound() == null) continue;
            TodayPlan.Revision rev = new TodayPlan.Revision(v, r.chapterId(), r.chapterSlug(), r.chapterName(), r.chapterSort(),
                r.subjectSlug(), r.subjectName(), r.summaryTopic(), r.openMistakes());
            (Boolean.TRUE.equals(v.isDue()) ? due : upcoming).add(rev);
        }
        upcoming.sort(Comparator.comparing(r -> r.progress().dueAt()));

        List<TodayPlan.MockToRetake> mocks = today.mixedMocksOfStudiedChapters(userId).stream()
            .filter(m -> m.bestPct() == null || m.bestPct().doubleValue() < MOCK_TARGET_PCT).toList();

        return new TodayPlan(tracks(userId), due, upcoming.subList(0, Math.min(UPCOMING_REVISIONS, upcoming.size())),
            today.mistakeCounts(userId), mocks, MOCK_TARGET_PCT, today.activity(userId));
    }

    /** One row per subject that has notes: its first chapter not yet studied, or "all studied". */
    private List<TodayPlan.Track> tracks(long userId) {
        List<TrackChapter> chapters = today.chaptersWithNotes(userId);
        Map<String, Integer> chaptersReady = new LinkedHashMap<>();
        Map<String, TrackChapter> firstInSubject = new LinkedHashMap<>(), nextToStudy = new LinkedHashMap<>();
        for (TrackChapter c : chapters) {
            chaptersReady.merge(c.subjectSlug(), 1, Integer::sum);
            firstInSubject.putIfAbsent(c.subjectSlug(), c);
            if (!c.studied()) nextToStudy.putIfAbsent(c.subjectSlug(), c);
        }
        return firstInSubject.values().stream().map(first -> {
            int ready = chaptersReady.get(first.subjectSlug());
            TrackChapter next = nextToStudy.get(first.subjectSlug());
            return next == null ? TodayPlan.Track.finished(first, ready)
                : TodayPlan.Track.nextChapter(next, today.firstUnattemptedTopic(next.id(), userId).orElse(null), ready);
        }).toList();
    }
}
