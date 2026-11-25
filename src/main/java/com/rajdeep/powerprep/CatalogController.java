package com.rajdeep.powerprep;

import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

/** Read-only catalog: subjects, topics, questions. */
@RestController
@RequestMapping("/api")
public class CatalogController {
    private final JdbcTemplate jdbc;
    public CatalogController(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    @GetMapping("/subjects")
    public List<Map<String, Object>> subjects() {
        return jdbc.queryForList("""
            SELECT s.id, s.name, s.slug, s.in_syllabus AS "inSyllabus",
                   (SELECT count(*) FROM topic t WHERE t.subject_id = s.id) AS topics,
                   (SELECT count(*) FROM question q WHERE q.subject_id = s.id AND q.origin = 'PYQ') AS questions
            FROM subject s ORDER BY s.sort_order""");
    }

    @GetMapping("/subjects/{slug}/topics")
    public List<Map<String, Object>> topics(@PathVariable String slug) {
        return jdbc.queryForList("""
            SELECT t.id, t.name,
                   count(DISTINCT q.id) AS questions,
                   count(DISTINCT so.exam) FILTER (WHERE so.kind = 'STATE') AS "stateExams",
                   count(DISTINCT q.id)    FILTER (WHERE so.kind = 'GATE')  AS "gateQuestions"
            FROM topic t
            JOIN subject s ON s.id = t.subject_id
            LEFT JOIN question q         ON q.topic_id = t.id AND q.status <> 'RETIRED' AND q.origin = 'PYQ'
            LEFT JOIN question_source qs ON qs.question_id = q.id
            LEFT JOIN source so          ON so.id = qs.source_id
            WHERE s.slug = ?
            GROUP BY t.id
            ORDER BY count(DISTINCT q.id) DESC, t.name""", slug);
    }

    @GetMapping("/topics/{id}/questions")
    public List<Map<String, Object>> questions(@PathVariable int id) {
        return jdbc.queryForList("""
            SELECT q.id, ps.code AS source, pqs.q_no AS "qNo", pqs.marks,
                   q.nature AS type, q.summary, q.stem_md AS body, q.status,
                   (SELECT string_agg(so.code || ' Q' || qs.q_no, ', ' ORDER BY so.year, so.code)
                      FROM question_source qs JOIN source so ON so.id = qs.source_id
                     WHERE qs.question_id = q.id) AS sources
            FROM question q
            JOIN LATERAL (SELECT qs.source_id, qs.q_no, qs.marks FROM question_source qs
                          WHERE qs.question_id = q.id ORDER BY qs.is_primary DESC, qs.source_id LIMIT 1) pqs ON TRUE
            JOIN source ps ON ps.id = pqs.source_id
            WHERE q.topic_id = ? AND q.status <> 'RETIRED'
            ORDER BY ps.kind DESC, ps.year, q.id""", id);
    }
}
