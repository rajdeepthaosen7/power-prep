package com.rajdeep.powerprep.mistakes;

import com.rajdeep.powerprep.common.jdbc.Rows;
import java.util.List;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
class JdbcMistakeRepository implements MistakeRepository {
    private final JdbcClient jdbc;

    JdbcMistakeRepository(JdbcClient jdbc) { this.jdbc = jdbc; }

    @Override
    public List<MistakeRow> log(long userId) {
        return jdbc.sql("""
                WITH a AS (
                  SELECT question_id, is_correct, chosen_labels, nat_value, mode, attempted_at, self_mark,
                         row_number() OVER (PARTITION BY question_id ORDER BY attempted_at DESC, id DESC) AS rn,
                         count(*) FILTER (WHERE is_correct = FALSE) OVER (PARTITION BY question_id) AS wrong,
                         count(*) FILTER (WHERE self_mark IN ('GUESSED', 'DONT_KNOW')) OVER (PARTITION BY question_id) AS unsure,
                         max(attempted_at) FILTER (WHERE is_correct = FALSE) OVER (PARTITION BY question_id) AS last_wrong_at
                  FROM attempt WHERE user_id = :uid)
                SELECT q.id, q.stem_md, q.answer_type, q.origin, q.difficulty, q.solution_md, q.answer_status, q.nat_min, q.nat_max,
                       t.name AS topic, c.slug AS chapter_slug, c.name AS chapter_name, c.sort_order AS chapter_sort,
                       s.slug AS subject_slug, s.name AS subject_name,
                       a.is_correct AS last_correct, a.chosen_labels AS last_chosen, a.nat_value AS last_nat, a.mode AS last_mode,
                       a.attempted_at AS last_at, a.wrong, a.last_wrong_at, mn.reason,
                       a.self_mark AS last_self_mark, COALESCE(rs.sure_days, 0) AS sure_days
                FROM a JOIN question q ON q.id = a.question_id JOIN topic t ON t.id = q.topic_id JOIN subject s ON s.id = q.subject_id
                LEFT JOIN chapter c ON c.id = t.chapter_id
                LEFT JOIN mistake_note mn ON mn.user_id = :uid AND mn.question_id = q.id
                LEFT JOIN retest_state rs ON rs.user_id = :uid AND rs.question_id = q.id   -- RetestRule data (V10)
                WHERE a.rn = 1 AND (a.wrong > 0 OR a.unsure > 0) AND q.stem_md IS NOT NULL
                ORDER BY a.is_correct, a.last_wrong_at DESC""")
            .param("uid", userId).query(Rows.of(MistakeRow.class)).list();
    }

    @Override
    public long wrongAttempts(long userId, int questionId) {
        return jdbc.sql("SELECT count(*) FROM attempt WHERE user_id = :uid AND question_id = :qid AND is_correct = FALSE")
            .param("uid", userId).param("qid", questionId).query(Long.class).single();
    }

    @Override
    public void saveReason(long userId, int questionId, MistakeReason reason) {
        jdbc.sql("""
                INSERT INTO mistake_note(user_id, question_id, reason) VALUES (:uid, :qid, :reason)
                ON CONFLICT (user_id, question_id) DO UPDATE SET reason = EXCLUDED.reason, updated_at = now()""")
            .param("uid", userId).param("qid", questionId).param("reason", reason.name()).update();
    }

    @Override
    public void clearReason(long userId, int questionId) {
        jdbc.sql("DELETE FROM mistake_note WHERE user_id = :uid AND question_id = :qid")
            .param("uid", userId).param("qid", questionId).update();
    }
}
