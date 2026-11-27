package com.rajdeep.powerprep.pyq;

import com.rajdeep.powerprep.common.jdbc.Rows;
import com.rajdeep.powerprep.mocktest.TestList;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
class JdbcPyqRepository implements PyqRepository {
    /** A question a test may ask: an exam question with its text written. */
    private static final String READY = "q.status = 'READY' AND q.stem_md IS NOT NULL";

    private final JdbcClient jdbc;

    JdbcPyqRepository(JdbcClient jdbc) { this.jdbc = jdbc; }

    @Override
    public List<SourceRow> sourceRows(String exam) {
        return jdbc.sql("""
                SELECT s.code AS source_code, qs.q_no, qs.marks, (%s) AS ready, s.year
                FROM question_source qs JOIN source s ON s.id = qs.source_id JOIN question q ON q.id = qs.question_id
                WHERE s.kind = :exam AND q.origin = 'PYQ'""".formatted(READY))
            .param("exam", exam).query(Rows.of(SourceRow.class)).list();
    }

    @Override
    public List<CountRow> countsByChapter(String exam) {
        return jdbc.sql("""
                WITH qx AS (
                  SELECT q.id, q.subject_id, q.topic_id, bool_or(%s) AS ready, min(s.year) AS year
                  FROM question q JOIN question_source qs ON qs.question_id = q.id JOIN source s ON s.id = qs.source_id
                  WHERE s.kind = :exam AND q.origin = 'PYQ' GROUP BY q.id)
                SELECT sb.slug AS subject_slug, sb.name AS subject_name, sb.sort_order AS subject_sort,
                       c.slug AS chapter_slug, c.name AS chapter_name, c.sort_order AS chapter_sort,
                       count(*) FILTER (WHERE qx.ready)::int AS ready, count(*)::int AS total,
                       min(qx.year) FILTER (WHERE qx.ready) AS first_year, max(qx.year) FILTER (WHERE qx.ready) AS last_year
                FROM qx JOIN subject sb ON sb.id = qx.subject_id JOIN topic t ON t.id = qx.topic_id
                LEFT JOIN chapter c ON c.id = t.chapter_id
                GROUP BY sb.slug, sb.name, sb.sort_order, c.slug, c.name, c.sort_order
                ORDER BY sb.sort_order, c.sort_order NULLS LAST""".formatted(READY))
            .param("exam", exam).query(Rows.of(CountRow.class)).list();
    }

    @Override
    public List<Picked> pick(PyqSelection sel, PaperRef paper) {
        StringBuilder sql = new StringBuilder("""
                SELECT q.id AS question_id, s.code AS source_code, qs.q_no, qs.marks, s.year
                FROM question_source qs JOIN source s ON s.id = qs.source_id JOIN question q ON q.id = qs.question_id
                JOIN topic t ON t.id = q.topic_id
                WHERE s.kind = :exam AND q.origin = 'PYQ' AND\s""").append(READY);
        if (paper != null) {
            sql.append(" AND s.code = :code");
            if (paper.qPrefix() != null) sql.append(" AND qs.q_no LIKE :prefix");
        } else {
            sql.append(" AND q.subject_id = (SELECT id FROM subject WHERE slug = :subject) AND s.year BETWEEN :from AND :to");
            if (sel.chapter() != null) sql.append(" AND t.chapter_id = (SELECT c.id FROM chapter c JOIN subject sb ON sb.id = c.subject_id"
                + " WHERE sb.slug = :subject AND c.slug = :chapter)");
        }
        var stmt = jdbc.sql(sql.toString()).param("exam", sel.exam());
        if (paper != null) {
            stmt = stmt.param("code", paper.sourceCode());
            if (paper.qPrefix() != null) stmt = stmt.param("prefix", paper.qPrefix() + "%");
        } else {
            stmt = stmt.param("subject", sel.subject()).param("from", sel.yearFrom()).param("to", sel.yearTo());
            if (sel.chapter() != null) stmt = stmt.param("chapter", sel.chapter());
        }
        return stmt.query(Rows.of(Picked.class)).list();
    }

    @Override
    public Optional<String> subjectName(String slug) {
        return jdbc.sql("SELECT name FROM subject WHERE slug = :slug").param("slug", slug).query(String.class).optional();
    }

    @Override
    public Optional<String> chapterName(String subjectSlug, String chapterSlug) {
        return jdbc.sql("""
                SELECT c.name FROM chapter c JOIN subject s ON s.id = c.subject_id WHERE s.slug = :subject AND c.slug = :chapter""")
            .param("subject", subjectSlug).param("chapter", chapterSlug).query(String.class).optional();
    }

    @Override
    public Optional<Integer> testIdByCode(String code) {
        return jdbc.sql("SELECT id FROM test WHERE code = :code").param("code", code).query(Integer.class).optional();
    }

    @Override
    public int createSetTest(String code, String title, String description, long ownerId, List<Picked> questions) {
        int id = jdbc.sql("""
                INSERT INTO test(code, title, kind, status, marks_correct, marks_wrong, description, owner_user_id)
                VALUES (:code, :title, 'PYQ_SET', 'PUBLISHED', 1, 0.25, :description, :owner) RETURNING id""")
            .param("code", code).param("title", title).param("description", description).param("owner", ownerId)
            .query(Integer.class).single();
        int position = 1;
        for (Picked p : questions) {
            jdbc.sql("INSERT INTO test_question(test_id, position, question_id, marks) VALUES (:tid, :pos, :qid, :marks)")
                .param("tid", id).param("pos", position++).param("qid", p.questionId()).param("marks", p.marks()).update();
        }
        return id;
    }

    private record OwnRow(String code, String title, long id, int testId, String status, Instant startedAt, Instant submittedAt,
                          BigDecimal score, BigDecimal maxScore, Integer nCorrect, Integer nWrong, Integer nSkipped,
                          String mode, String style) {}

    @Override
    public List<OwnAttempt> ownAttempts(long userId) {
        return jdbc.sql("""
                SELECT t.code, t.title, ta.id, ta.test_id, ta.status, ta.started_at, ta.submitted_at, ta.score, ta.max_score,
                       ta.n_correct, ta.n_wrong, ta.n_unattempted AS n_skipped, ta.mode, ta.style
                FROM test_attempt ta JOIN test t ON t.id = ta.test_id
                WHERE ta.user_id = :uid AND t.kind = 'PYQ_SET' AND t.owner_user_id = :uid ORDER BY ta.started_at DESC""")
            .param("uid", userId).query(Rows.of(OwnRow.class)).list().stream()
            .map(r -> new OwnAttempt(r.code(), r.title(), new TestList.Attempt(r.id(), r.testId(), r.status(), r.startedAt(),
                r.submittedAt(), r.score(), r.maxScore(), r.nCorrect(), r.nWrong(), r.nSkipped(), r.mode(), r.style())))
            .toList();
    }
}
