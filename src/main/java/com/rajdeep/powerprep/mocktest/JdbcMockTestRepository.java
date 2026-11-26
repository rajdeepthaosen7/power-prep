package com.rajdeep.powerprep.mocktest;

import com.rajdeep.powerprep.common.jdbc.Rows;
import com.rajdeep.powerprep.question.OptionRow;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
class JdbcMockTestRepository implements MockTestRepository {
    private final JdbcClient jdbc;

    JdbcMockTestRepository(JdbcClient jdbc) { this.jdbc = jdbc; }

    @Override
    public List<TestList.Chapter> chaptersOf(String subjectSlug) {
        record Row(int id, String slug, String name, int sortOrder) {}
        return jdbc.sql("""
                SELECT c.id, c.slug, c.name, c.sort_order
                FROM chapter c JOIN subject s ON s.id = c.subject_id WHERE s.slug = :slug ORDER BY c.sort_order""")
            .param("slug", subjectSlug).query(Rows.of(Row.class)).list().stream()
            .map(r -> new TestList.Chapter(r.id(), r.slug(), r.name(), r.sortOrder(), List.of())).toList();
    }

    private record TestRow(int id, String code, String title, String variant, String description, Integer durationMin,
                           BigDecimal marksCorrect, BigDecimal marksWrong, Integer chapterId, long questions) {
        TestList.Test toTest() {
            return new TestList.Test(id, code, title, variant, description, durationMin, marksCorrect, marksWrong,
                chapterId, questions, List.of());
        }
    }

    @Override
    public List<TestList.Test> publishedTests(String subjectSlug) {
        return jdbc.sql("""
                SELECT t.id, t.code, t.title, t.variant, t.description, t.duration_min, t.marks_correct, t.marks_wrong, t.chapter_id,
                       (SELECT count(*) FROM test_question tq WHERE tq.test_id = t.id) AS questions
                FROM test t JOIN subject s ON s.id = t.subject_id
                WHERE s.slug = :slug AND t.status = 'PUBLISHED' ORDER BY t.sort_order, t.id""")
            .param("slug", subjectSlug).query(Rows.of(TestRow.class)).list().stream().map(TestRow::toTest).toList();
    }

    @Override
    public List<TestList.Attempt> attempts(long userId, String subjectSlug) {
        return jdbc.sql("""
                SELECT ta.id, ta.test_id, ta.status, ta.started_at, ta.submitted_at, ta.score, ta.max_score,
                       ta.n_correct, ta.n_wrong, ta.n_unattempted AS n_skipped
                FROM test_attempt ta JOIN test t ON t.id = ta.test_id JOIN subject s ON s.id = t.subject_id
                WHERE ta.user_id = :uid AND s.slug = :slug ORDER BY ta.started_at DESC""")
            .param("uid", userId).param("slug", subjectSlug).query(Rows.of(TestList.Attempt.class)).list();
    }

    @Override
    public Optional<TestMeta> publishedTest(int testId) {
        return jdbc.sql("""
                SELECT t.id, t.code, t.title, t.variant, t.description, t.duration_min, t.marks_correct, t.marks_wrong,
                       c.slug AS chapter_slug, c.name AS chapter_name, s.slug AS subject_slug, s.name AS subject_name,
                       (SELECT count(*) FROM test_question tq WHERE tq.test_id = t.id) AS questions
                FROM test t LEFT JOIN chapter c ON c.id = t.chapter_id LEFT JOIN subject s ON s.id = t.subject_id
                WHERE t.id = :id AND t.status = 'PUBLISHED'""")
            .param("id", testId).query(Rows.of(TestMeta.class)).optional();
    }

    @Override
    public Optional<Long> openAttempt(long userId, int testId) {
        return jdbc.sql("""
                SELECT id FROM test_attempt WHERE user_id = :uid AND test_id = :tid AND status = 'IN_PROGRESS'
                ORDER BY started_at DESC LIMIT 1""")
            .param("uid", userId).param("tid", testId).query(Long.class).optional();
    }

    @Override
    public long createAttempt(long userId, int testId) {
        return jdbc.sql("INSERT INTO test_attempt(user_id, test_id) VALUES (:uid, :tid) RETURNING id")
            .param("uid", userId).param("tid", testId).query(Long.class).single();
    }

    @Override
    public Optional<TestAttemptRow> attempt(long attemptId, long userId) {
        return jdbc.sql("SELECT * FROM test_attempt WHERE id = :id AND user_id = :uid")
            .param("id", attemptId).param("uid", userId).query(Rows.of(TestAttemptRow.class)).optional();
    }

    @Override
    public List<StartedTest.QuestionRow> questionsOf(int testId) {
        return jdbc.sql("""
                SELECT q.id, tq.position, q.stem_md
                FROM test_question tq JOIN question q ON q.id = tq.question_id
                WHERE tq.test_id = :id ORDER BY tq.position""")
            .param("id", testId).query(Rows.of(StartedTest.QuestionRow.class)).list();
    }

    @Override
    public List<Integer> questionIds(int testId) {
        return jdbc.sql("SELECT question_id FROM test_question WHERE test_id = :id ORDER BY position")
            .param("id", testId).query(Integer.class).list();
    }

    @Override
    public Map<Integer, List<OptionRow>> optionsOf(int testId) {
        return jdbc.sql("""
                SELECT o.question_id, o.label, o.body_md, o.is_correct AS correct FROM question_option o
                JOIN test_question tq ON tq.question_id = o.question_id WHERE tq.test_id = :id ORDER BY o.label""")
            .param("id", testId).query(Rows.of(OptionRow.class)).list().stream()
            .collect(Collectors.groupingBy(OptionRow::questionId, LinkedHashMap::new, Collectors.toList()));
    }

    @Override
    public Map<String, String> figuresOf(int testId) {
        Map<String, String> out = new LinkedHashMap<>();
        jdbc.sql("""
                SELECT f.storage_key, f.svg FROM figure f JOIN test_question tq ON tq.question_id = f.question_id
                WHERE tq.test_id = :id AND f.svg IS NOT NULL""")
            .param("id", testId).query(rs -> { out.put(rs.getString(1), rs.getString(2)); });
        return out;
    }

    @Override
    public void saveScore(long attemptId, TestScore s, Long timeMs) {
        jdbc.sql("""
                UPDATE test_attempt SET status = 'SUBMITTED', submitted_at = now(), score = :score, max_score = :max,
                  n_correct = :correct, n_wrong = :wrong, n_unattempted = :skipped, time_ms = :timeMs WHERE id = :id""")
            .param("score", s.score()).param("max", s.maxScore()).param("correct", s.correct()).param("wrong", s.wrong())
            .param("skipped", s.skipped()).param("timeMs", timeMs).param("id", attemptId).update();
    }

    @Override
    public List<TestResult.QuestionRow> resultQuestions(long attemptId, int testId) {
        return jdbc.sql("""
                SELECT q.id, tq.position, q.stem_md, q.solution_md, q.answer_status, q.origin, q.difficulty,
                       t.name AS topic, t.slug AS topic_slug, t.sort_order AS topic_sort,
                       a.chosen_labels AS chosen, a.is_correct AS correct, a.time_ms
                FROM test_question tq JOIN question q ON q.id = tq.question_id JOIN topic t ON t.id = q.topic_id
                LEFT JOIN attempt a ON a.test_attempt_id = :aid AND a.question_id = q.id
                WHERE tq.test_id = :tid ORDER BY tq.position""")
            .param("aid", attemptId).param("tid", testId).query(Rows.of(TestResult.QuestionRow.class)).list();
    }

    @Override
    public List<TestResult.HistoryEntry> history(long userId, int testId) {
        return jdbc.sql("""
                SELECT id, submitted_at, score, max_score FROM test_attempt
                WHERE user_id = :uid AND test_id = :tid AND status = 'SUBMITTED' ORDER BY submitted_at""")
            .param("uid", userId).param("tid", testId).query(Rows.of(TestResult.HistoryEntry.class)).list();
    }
}
