package com.rajdeep.powerprep.revision;

import com.rajdeep.powerprep.common.jdbc.Rows;
import java.util.Optional;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
class JdbcChapterProgressRepository implements ChapterProgressRepository {
    private final JdbcClient jdbc;

    JdbcChapterProgressRepository(JdbcClient jdbc) { this.jdbc = jdbc; }

    @Override
    public boolean chapterExists(int chapterId) {
        return jdbc.sql("SELECT EXISTS (SELECT 1 FROM chapter WHERE id = :id)").param("id", chapterId).query(Boolean.class).single();
    }

    @Override
    public Optional<ChapterProgress> find(long userId, int chapterId) {
        return jdbc.sql("SELECT * FROM chapter_progress WHERE user_id = :uid AND chapter_id = :cid")
            .param("uid", userId).param("cid", chapterId).query(Rows.of(ChapterProgress.class)).optional();
    }

    @Override
    public void markStudied(long userId, int chapterId) {
        jdbc.sql("INSERT INTO chapter_progress(user_id, chapter_id) VALUES (:uid, :cid) ON CONFLICT DO NOTHING")
            .param("uid", userId).param("cid", chapterId).update();
    }

    @Override
    public void markRevised(long userId, int chapterId, int round) {
        if (round < 1 || round > ChapterProgress.ROUNDS) throw new IllegalArgumentException("round " + round);
        jdbc.sql("UPDATE chapter_progress SET rev" + round + "_at = now() WHERE user_id = :uid AND chapter_id = :cid")
            .param("uid", userId).param("cid", chapterId).update();
    }

    @Override
    public void delete(long userId, int chapterId) {
        jdbc.sql("DELETE FROM chapter_progress WHERE user_id = :uid AND chapter_id = :cid")
            .param("uid", userId).param("cid", chapterId).update();
    }

    @Override
    public void markFullyAttemptedChapters(long userId) {
        jdbc.sql("""
                INSERT INTO chapter_progress(user_id, chapter_id, studied_at)
                SELECT :uid, c.id, (SELECT max(a.attempted_at) FROM attempt a JOIN question q ON q.id = a.question_id
                                    JOIN topic t ON t.id = q.topic_id WHERE t.chapter_id = c.id AND a.user_id = :uid)
                FROM chapter c
                WHERE EXISTS (SELECT 1 FROM question q JOIN topic t ON t.id = q.topic_id
                              WHERE t.chapter_id = c.id AND q.status = 'READY' AND q.origin = 'PYQ')
                  AND NOT EXISTS (SELECT 1 FROM question q JOIN topic t ON t.id = q.topic_id
                                  WHERE t.chapter_id = c.id AND q.status = 'READY' AND q.origin = 'PYQ'
                                    AND NOT EXISTS (SELECT 1 FROM attempt a WHERE a.question_id = q.id AND a.user_id = :uid))
                ON CONFLICT DO NOTHING""")
            .param("uid", userId).update();
    }
}
