package com.rajdeep.powerprep;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Syllabus tab: the exam's syllabus, item by item, with how many past questions (and APSC 2023 questions)
 * each item has, which chapters teach it, and how many of its questions the signed-in user has solved.
 */
@RestController
@RequestMapping("/api")
public class SyllabusController {
    static final String DEFAULT_EXAM = "APGCL-AM-2026";
    private final JdbcTemplate jdbc;

    public SyllabusController(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    @GetMapping("/syllabus")
    public Map<String, Object> syllabus(@RequestParam(defaultValue = DEFAULT_EXAM) String exam, Authentication auth) {
        long uid = userId(auth);
        List<Map<String, Object>> rows = jdbc.queryForList("""
            SELECT si.id, si.code, si.parent_id AS "parentId", si.text, si.note, s.slug AS "subjectSlug", s.name AS "subjectName"
            FROM syllabus_item si LEFT JOIN subject s ON s.id = si.subject_id
            WHERE si.exam_code = ? ORDER BY si.parent_id NULLS FIRST, si.sort_order""", exam);
        // per item: distinct PYQs of its topics, APSC 2023 ones, written ones, solved by this user
        List<Map<String, Object>> counts = jdbc.queryForList("""
            SELECT sit.syllabus_item_id AS id,
                   count(DISTINCT q.id) AS questions,
                   count(DISTINCT q.id) FILTER (WHERE EXISTS (SELECT 1 FROM question_source qs JOIN source so ON so.id = qs.source_id
                                                             WHERE qs.question_id = q.id AND so.kind = 'STATE')) AS apsc,
                   count(DISTINCT q.id) FILTER (WHERE q.status = 'READY') AS written,
                   count(DISTINCT q.id) FILTER (WHERE EXISTS (SELECT 1 FROM attempt a WHERE a.question_id = q.id AND a.user_id = ?)) AS solved
            FROM syllabus_item_topic sit JOIN syllabus_item si ON si.id = sit.syllabus_item_id
            JOIN question q ON q.topic_id = sit.topic_id AND q.origin = 'PYQ' AND q.status <> 'RETIRED'
            WHERE si.exam_code = ? GROUP BY sit.syllabus_item_id""", uid, exam);
        Map<Object, Map<String, Object>> countById = new LinkedHashMap<>();
        counts.forEach(c -> countById.put(c.get("id"), c));
        List<Map<String, Object>> chapters = jdbc.queryForList("""
            SELECT DISTINCT sit.syllabus_item_id AS id, c.slug, c.name, c.sort_order AS "sortOrder", cs_sub.slug AS "subjectSlug",
                   EXISTS (SELECT 1 FROM topic t2 JOIN concept_section cs ON cs.topic_id = t2.id WHERE t2.chapter_id = c.id) AS notes
            FROM syllabus_item_topic sit JOIN syllabus_item si ON si.id = sit.syllabus_item_id
            JOIN topic t ON t.id = sit.topic_id JOIN chapter c ON c.id = t.chapter_id JOIN subject cs_sub ON cs_sub.id = c.subject_id
            WHERE si.exam_code = ? ORDER BY c.sort_order""", exam);

        List<Map<String, Object>> sections = new ArrayList<>();
        Map<Object, Map<String, Object>> sectionById = new LinkedHashMap<>();
        for (Map<String, Object> r : rows) {
            if (r.get("parentId") == null) {
                Map<String, Object> s = new LinkedHashMap<>();
                s.put("code", r.get("code"));
                s.put("text", r.get("text"));
                s.put("note", r.get("note"));
                s.put("subjectSlug", r.get("subjectSlug"));
                s.put("subjectName", r.get("subjectName"));
                s.put("items", new ArrayList<Map<String, Object>>());
                sections.add(s);
                sectionById.put(r.get("id"), s);
            }
        }
        for (Map<String, Object> r : rows) {
            Map<String, Object> s = sectionById.get(r.get("parentId"));
            if (s == null) continue;
            Map<String, Object> c = countById.getOrDefault(r.get("id"), Map.of());
            Map<String, Object> it = new LinkedHashMap<>();
            it.put("code", r.get("code"));
            it.put("text", r.get("text"));
            it.put("note", r.get("note"));
            for (String k : List.of("questions", "apsc", "written", "solved"))
                it.put(k, ((Number) c.getOrDefault(k, 0)).longValue());
            it.put("chapters", chapters.stream().filter(ch -> ch.get("id").equals(r.get("id")))
                .map(ch -> Map.of("slug", ch.get("slug"), "name", ch.get("name"), "sortOrder", ch.get("sortOrder"), "notes", ch.get("notes"),
                    "subjectSlug", ch.get("subjectSlug")))
                .toList());
            @SuppressWarnings("unchecked") List<Map<String, Object>> items = (List<Map<String, Object>>) s.get("items");
            items.add(it);
        }
        // per section: distinct questions (an item can share topics with another item, so item counts do not add up)
        List<Map<String, Object>> secCounts = jdbc.queryForList("""
            SELECT si.parent_id AS id, count(DISTINCT q.id) AS questions,
                   count(DISTINCT q.id) FILTER (WHERE EXISTS (SELECT 1 FROM question_source qs JOIN source so ON so.id = qs.source_id
                                                             WHERE qs.question_id = q.id AND so.kind = 'STATE')) AS apsc,
                   count(DISTINCT q.id) FILTER (WHERE EXISTS (SELECT 1 FROM attempt a WHERE a.question_id = q.id AND a.user_id = ?)) AS solved
            FROM syllabus_item_topic sit JOIN syllabus_item si ON si.id = sit.syllabus_item_id
            JOIN question q ON q.topic_id = sit.topic_id AND q.origin = 'PYQ' AND q.status <> 'RETIRED'
            WHERE si.exam_code = ? GROUP BY si.parent_id""", uid, exam);
        for (Map<String, Object> c : secCounts) {
            Map<String, Object> s = sectionById.get(c.get("id"));
            if (s != null) for (String k : List.of("questions", "apsc", "solved")) s.put(k, ((Number) c.get(k)).longValue());
        }
        for (Map<String, Object> s : sections) for (String k : List.of("questions", "apsc", "solved")) s.putIfAbsent(k, 0L);
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("exam", exam);
        out.put("sections", sections);
        return out;
    }

    private static long userId(Authentication auth) {
        if (auth == null || !auth.isAuthenticated() || auth.getName() == null) return -1L;
        try { return Long.parseLong(auth.getName()); } catch (NumberFormatException e) { return -1L; }
    }
}
