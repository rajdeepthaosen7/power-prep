package com.rajdeep.powerprep.today;

import com.rajdeep.powerprep.common.jdbc.Rows;
import com.rajdeep.powerprep.mistakes.RetestRule;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
class JdbcTodayRepository implements TodayRepository {
    private final JdbcClient jdbc;

    JdbcTodayRepository(JdbcClient jdbc) { this.jdbc = jdbc; }

    @Override
    public List<TrackChapter> chaptersWithNotes(long userId) {
        return jdbc.sql("""
                SELECT s.slug AS subject_slug, s.name AS subject_name, c.id, c.slug, c.name, c.sort_order,
                       (SELECT count(*) FROM question q JOIN topic t ON t.id = q.topic_id
                         WHERE t.chapter_id = c.id AND q.status = 'READY' AND q.origin = 'PYQ') AS ready,
                       (SELECT count(DISTINCT a.question_id) FROM attempt a JOIN question q ON q.id = a.question_id
                         JOIN topic t ON t.id = q.topic_id
                         WHERE t.chapter_id = c.id AND q.origin = 'PYQ' AND a.user_id = :uid) AS attempted,
                       EXISTS (SELECT 1 FROM chapter_progress cp WHERE cp.user_id = :uid AND cp.chapter_id = c.id) AS studied
                FROM chapter c JOIN subject s ON s.id = c.subject_id
                WHERE EXISTS (SELECT 1 FROM topic t JOIN concept_section cs ON cs.topic_id = t.id WHERE t.chapter_id = c.id)
                ORDER BY CASE s.slug WHEN 'electric-circuits' THEN 0 WHEN 'power-systems' THEN 1 ELSE 2 END, s.name, c.sort_order""")
            .param("uid", userId).query(Rows.of(TrackChapter.class)).list();
    }

    @Override
    public Optional<TodayPlan.TopicLink> firstUnattemptedTopic(int chapterId, long userId) {
        return jdbc.sql("""
                SELECT t.slug, t.name FROM topic t WHERE t.chapter_id = :cid
                  AND EXISTS (SELECT 1 FROM question q WHERE q.topic_id = t.id AND q.status = 'READY' AND q.origin = 'PYQ'
                              AND NOT EXISTS (SELECT 1 FROM attempt a WHERE a.question_id = q.id AND a.user_id = :uid))
                ORDER BY t.sort_order LIMIT 1""")
            .param("cid", chapterId).param("uid", userId).query(Rows.of(TodayPlan.TopicLink.class)).optional();
    }

    @Override
    public List<RevisionRow> studiedChapters(long userId) {
        return jdbc.sql("""
                SELECT cp.*, c.slug AS chapter_slug, c.name AS chapter_name, c.sort_order AS chapter_sort,
                       s.slug AS subject_slug, s.name AS subject_name,
                       (SELECT t.slug FROM topic t WHERE t.chapter_id = c.id AND t.slug LIKE '%-chapter-summary' LIMIT 1) AS summary_topic,
                       (SELECT count(*) FROM retest_state rs JOIN question q ON q.id = rs.question_id JOIN topic t ON t.id = q.topic_id
                         WHERE t.chapter_id = c.id AND rs.user_id = cp.user_id AND q.stem_md IS NOT NULL
                           AND rs.sure_days < :clear) AS open_mistakes
                FROM chapter_progress cp JOIN chapter c ON c.id = cp.chapter_id JOIN subject s ON s.id = c.subject_id
                WHERE cp.user_id = :uid""")
            .param("uid", userId).param("clear", RetestRule.SURE_DAYS_TO_CLEAR).query(Rows.of(RevisionRow.class)).list();
    }

    @Override
    public TodayPlan.MistakeCounts mistakeCounts(long userId) {
        return jdbc.sql("""
                SELECT count(*) AS open,
                       count(*) FILTER (WHERE mn.reason IS NULL AND EXISTS (SELECT 1 FROM attempt w WHERE w.user_id = :uid
                                         AND w.question_id = rs.question_id AND w.is_correct = FALSE)) AS untagged
                FROM retest_state rs JOIN question q ON q.id = rs.question_id AND q.stem_md IS NOT NULL
                LEFT JOIN mistake_note mn ON mn.user_id = :uid AND mn.question_id = rs.question_id
                WHERE rs.user_id = :uid AND rs.sure_days < :clear   -- on the retest list (RetestRule)""")
            .param("uid", userId).param("clear", RetestRule.SURE_DAYS_TO_CLEAR)
            .query(Rows.of(TodayPlan.MistakeCounts.class)).single();
    }

    @Override
    public List<TodayPlan.MockToRetake> mixedMocksOfStudiedChapters(long userId) {
        return jdbc.sql("""
                SELECT t.id, t.title, t.variant, c.name AS chapter_name, c.sort_order AS chapter_sort,
                       s.slug AS subject_slug, s.name AS subject_name,
                       (SELECT max(ta.score / NULLIF(ta.max_score, 0)) * 100 FROM test_attempt ta
                         WHERE ta.test_id = t.id AND ta.user_id = :uid AND ta.status = 'SUBMITTED') AS best_pct,
                       (SELECT count(*) FROM test_question tq WHERE tq.test_id = t.id) AS questions, t.duration_min
                FROM test t JOIN chapter c ON c.id = t.chapter_id JOIN subject s ON s.id = c.subject_id
                JOIN chapter_progress cp ON cp.chapter_id = c.id AND cp.user_id = :uid
                WHERE t.variant = 'MIX' AND t.status = 'PUBLISHED'
                ORDER BY s.name, c.sort_order""")
            .param("uid", userId).query(Rows.of(TodayPlan.MockToRetake.class)).list();
    }

    @Override
    public TodayPlan.Activity activity(long userId) {
        return jdbc.sql("""
                SELECT count(*) FILTER (WHERE attempted_at >= date_trunc('day', now() AT TIME ZONE 'Asia/Kolkata') AT TIME ZONE 'Asia/Kolkata') AS today,
                       count(*) AS week, count(*) FILTER (WHERE is_correct) AS week_correct
                FROM attempt WHERE user_id = :uid AND attempted_at >= now() - interval '7 days'""")
            .param("uid", userId).query(Rows.of(TodayPlan.Activity.class)).single();
    }
}
