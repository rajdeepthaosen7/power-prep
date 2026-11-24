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
                   (SELECT count(*) FROM question q WHERE q.subject_id = s.id) AS questions
            FROM subject s ORDER BY s.sort_order""");
    }

    @GetMapping("/subjects/{slug}/topics")
    public List<Map<String, Object>> topics(@PathVariable String slug) {
        return jdbc.queryForList("""
            SELECT t.id, t.name, count(q.id) AS questions,
                   count(DISTINCT so.exam) FILTER (WHERE so.kind='STATE') AS "stateExams",
                   count(q.id) FILTER (WHERE so.kind='GATE') AS "gateQuestions"
            FROM topic t JOIN subject s ON s.id = t.subject_id
            LEFT JOIN question q ON q.topic_id = t.id
            LEFT JOIN source so ON so.id = q.source_id
            WHERE s.slug = ? GROUP BY t.id ORDER BY count(q.id) DESC, t.name""", slug);
    }

    @GetMapping("/topics/{id}/questions")
    public List<Map<String, Object>> questions(@PathVariable int id) {
        return jdbc.queryForList("""
            SELECT q.id, so.code AS source, q.q_no AS "qNo", q.q_type AS type, q.marks,
                   q.summary, q.body, q.options, q.status
            FROM question q JOIN source so ON so.id = q.source_id
            WHERE q.topic_id = ? ORDER BY so.kind DESC, so.year, q.id""", id);
    }
}
