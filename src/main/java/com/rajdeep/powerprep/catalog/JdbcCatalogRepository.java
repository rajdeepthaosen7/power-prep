package com.rajdeep.powerprep.catalog;

import com.rajdeep.powerprep.common.jdbc.Rows;
import java.util.List;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
class JdbcCatalogRepository implements CatalogRepository {
    private final JdbcClient jdbc;

    JdbcCatalogRepository(JdbcClient jdbc) { this.jdbc = jdbc; }

    @Override
    public List<SubjectView> subjects() {
        return jdbc.sql("""
                SELECT s.id, s.name, s.slug, s.in_syllabus,
                       (SELECT count(*) FROM topic t WHERE t.subject_id = s.id) AS topics,
                       (SELECT count(*) FROM question q WHERE q.subject_id = s.id AND q.origin = 'PYQ') AS questions
                FROM subject s ORDER BY s.sort_order""")
            .query(Rows.of(SubjectView.class)).list();
    }

    @Override
    public List<TopicStats> topicsOf(String subjectSlug) {
        return jdbc.sql("""
                SELECT t.id, t.name,
                       count(DISTINCT q.id) AS questions,
                       count(DISTINCT so.exam) FILTER (WHERE so.kind = 'STATE') AS state_exams,
                       count(DISTINCT q.id)    FILTER (WHERE so.kind = 'GATE')  AS gate_questions
                FROM topic t
                JOIN subject s ON s.id = t.subject_id
                LEFT JOIN question q         ON q.topic_id = t.id AND q.status <> 'RETIRED' AND q.origin = 'PYQ'
                LEFT JOIN question_source qs ON qs.question_id = q.id
                LEFT JOIN source so          ON so.id = qs.source_id
                WHERE s.slug = :slug
                GROUP BY t.id
                ORDER BY count(DISTINCT q.id) DESC, t.name""")
            .param("slug", subjectSlug).query(Rows.of(TopicStats.class)).list();
    }

    @Override
    public List<TopicQuestion> questionsOf(int topicId) {
        return jdbc.sql("""
                SELECT q.id, ps.code AS source, pqs.q_no, pqs.marks,
                       q.nature AS type, q.summary, q.stem_md AS body, q.status,
                       (SELECT string_agg(so.code || ' Q' || qs.q_no, ', ' ORDER BY so.year, so.code)
                          FROM question_source qs JOIN source so ON so.id = qs.source_id
                         WHERE qs.question_id = q.id) AS sources
                FROM question q
                JOIN LATERAL (SELECT qs.source_id, qs.q_no, qs.marks FROM question_source qs
                              WHERE qs.question_id = q.id ORDER BY qs.is_primary DESC, qs.source_id LIMIT 1) pqs ON TRUE
                JOIN source ps ON ps.id = pqs.source_id
                WHERE q.topic_id = :id AND q.status <> 'RETIRED'
                ORDER BY ps.kind DESC, ps.year, q.id""")
            .param("id", topicId).query(Rows.of(TopicQuestion.class)).list();
    }
}
