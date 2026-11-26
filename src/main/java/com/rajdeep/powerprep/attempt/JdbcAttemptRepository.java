package com.rajdeep.powerprep.attempt;

import com.rajdeep.powerprep.common.jdbc.Rows;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
class JdbcAttemptRepository implements AttemptRepository {
    private final JdbcClient jdbc;

    JdbcAttemptRepository(JdbcClient jdbc) { this.jdbc = jdbc; }

    @Override
    public long insert(NewAttempt a) {
        return jdbc.sql("""
                INSERT INTO attempt(user_id, question_id, chosen_labels, nat_value, is_correct, time_ms, mode, test_attempt_id)
                VALUES (:uid, :qid, :chosen, :nat, :correct, :timeMs, :mode, :testAttemptId) RETURNING id""")
            .param("uid", a.userId()).param("qid", a.questionId()).param("chosen", a.chosenLabels())
            .param("nat", a.natValue()).param("correct", a.correct()).param("timeMs", a.timeMs())
            .param("mode", a.mode().name()).param("testAttemptId", a.testAttemptId())
            .query(Long.class).single();
    }

    @Override
    public void updateQuestionState(long userId, int questionId, boolean correct) {
        jdbc.sql("""
                INSERT INTO user_question_state(user_id, question_id, last_correct, times_seen, times_correct, last_attempt_at, updated_at)
                VALUES (:uid, :qid, :correct, 1, :plus, now(), now())
                ON CONFLICT (user_id, question_id) DO UPDATE SET last_correct = EXCLUDED.last_correct,
                  times_seen = user_question_state.times_seen + 1,
                  times_correct = user_question_state.times_correct + EXCLUDED.times_correct,
                  last_attempt_at = now(), updated_at = now()""")
            .param("uid", userId).param("qid", questionId).param("correct", correct).param("plus", correct ? 1 : 0)
            .update();
    }

    private record Row(int questionId, String chosen, BigDecimal natValue, Boolean correct, Instant at, long tries) {}

    @Override
    public Map<Integer, LastAttempt> latest(long userId, Collection<Integer> ids) {
        Map<Integer, LastAttempt> out = new LinkedHashMap<>();
        if (ids.isEmpty()) return out;
        jdbc.sql("""
                SELECT DISTINCT ON (question_id) question_id, chosen_labels AS chosen, nat_value, is_correct AS correct,
                       attempted_at AS at, count(*) OVER (PARTITION BY question_id) AS tries
                FROM attempt WHERE user_id = :uid AND question_id IN (:ids)
                ORDER BY question_id, attempted_at DESC""")
            .param("uid", userId).param("ids", ids).query(Rows.of(Row.class)).list()
            .forEach(r -> out.put(r.questionId(), new LastAttempt(r.chosen(), r.natValue(), r.correct(), r.at(), r.tries())));
        return out;
    }
}
