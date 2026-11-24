package com.rajdeep.powerprep;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * Study tab: chapters with notes, and questions you attempt first.
 * The correct answer and the analysis of a question are sent only after the signed-in user has attempted it.
 */
@RestController
@RequestMapping("/api")
public class StudyController {
    private final JdbcTemplate jdbc;

    public StudyController(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    /** Chapters of a subject with their topics and counts. */
    @GetMapping("/subjects/{slug}/chapters")
    public List<Map<String, Object>> chapters(@PathVariable String slug, Authentication auth) {
        Long uid = userId(auth);
        List<Map<String, Object>> chapters = jdbc.queryForList("""
            SELECT c.id, c.slug, c.name, c.description, c.sort_order AS "sortOrder"
            FROM chapter c JOIN subject s ON s.id = c.subject_id
            WHERE s.slug = ? ORDER BY c.sort_order""", slug);
        List<Map<String, Object>> topics = jdbc.queryForList("""
            SELECT t.id, t.slug, t.name, t.chapter_id AS "chapterId", t.sort_order AS "sortOrder",
                   (SELECT count(*) FROM concept_section cs WHERE cs.topic_id = t.id AND cs.status = 'PUBLISHED') AS notes,
                   (SELECT count(*) FROM question q WHERE q.topic_id = t.id AND q.status <> 'RETIRED') AS questions,
                   (SELECT count(*) FROM question q WHERE q.topic_id = t.id AND q.status = 'READY') AS ready,
                   (SELECT count(DISTINCT a.question_id) FROM attempt a JOIN question q ON q.id = a.question_id
                     WHERE q.topic_id = t.id AND a.user_id = ?) AS attempted
            FROM topic t JOIN subject s ON s.id = t.subject_id
            WHERE s.slug = ? AND t.chapter_id IS NOT NULL
            ORDER BY t.sort_order, t.name""", uid == null ? -1L : uid, slug);
        for (Map<String, Object> c : chapters) {
            List<Map<String, Object>> ts = topics.stream().filter(t -> c.get("id").equals(t.get("chapterId"))).toList();
            c.put("topics", ts);
            c.put("notes", ts.stream().mapToLong(t -> ((Number) t.get("notes")).longValue()).sum());
            c.put("questions", ts.stream().mapToLong(t -> ((Number) t.get("questions")).longValue()).sum());
            c.put("ready", ts.stream().mapToLong(t -> ((Number) t.get("ready")).longValue()).sum());
            c.put("attempted", ts.stream().mapToLong(t -> ((Number) t.get("attempted")).longValue()).sum());
        }
        return chapters;
    }

    /** One chapter: notes of every topic, the ready questions of every topic, and all figures they use. */
    @GetMapping("/subjects/{slug}/chapters/{chapter}")
    public Map<String, Object> chapter(@PathVariable String slug, @PathVariable String chapter, Authentication auth) {
        Long uid = userId(auth);
        List<Map<String, Object>> found = jdbc.queryForList("""
            SELECT c.id, c.slug, c.name, c.description, c.sort_order AS "sortOrder", s.name AS "subjectName"
            FROM chapter c JOIN subject s ON s.id = c.subject_id WHERE s.slug = ? AND c.slug = ?""", slug, chapter);
        if (found.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "chapter not found");
        Map<String, Object> ch = found.get(0);
        int chapterId = ((Number) ch.get("id")).intValue();

        List<Map<String, Object>> topics = jdbc.queryForList("""
            SELECT t.id, t.slug, t.name FROM topic t WHERE t.chapter_id = ? ORDER BY t.sort_order, t.name""", chapterId);
        List<Map<String, Object>> sections = jdbc.queryForList("""
            SELECT cs.id, cs.topic_id AS "topicId", cs.slug, cs.title, cs.body_md AS "bodyMd"
            FROM concept_section cs JOIN topic t ON t.id = cs.topic_id
            WHERE t.chapter_id = ? AND cs.status = 'PUBLISHED' ORDER BY cs.sort_order, cs.id""", chapterId);
        List<Map<String, Object>> questions = jdbc.queryForList("""
            SELECT q.id, q.topic_id AS "topicId", q.content_key AS key, q.stem_md AS "stemMd", q.answer_type AS "answerType",
                   q.nature, q.difficulty, q.answer_status AS "answerStatus"
            FROM question q JOIN topic t ON t.id = q.topic_id
            WHERE t.chapter_id = ? AND q.status = 'READY'
            ORDER BY t.sort_order, q.difficulty NULLS LAST, q.id""", chapterId);
        attachQuestionDetails(questions, uid);

        for (Map<String, Object> t : topics) {
            Object id = t.get("id");
            t.put("sections", sections.stream().filter(s -> id.equals(s.get("topicId"))).toList());
            t.put("questions", questions.stream().filter(qq -> id.equals(qq.get("topicId"))).toList());
        }
        ch.put("topics", topics);
        ch.put("figures", figures("""
            SELECT f.storage_key, f.svg FROM figure f
            LEFT JOIN concept_section cs ON cs.id = f.concept_section_id
            LEFT JOIN topic t1 ON t1.id = cs.topic_id
            LEFT JOIN question q ON q.id = f.question_id
            LEFT JOIN topic t2 ON t2.id = q.topic_id
            WHERE f.svg IS NOT NULL AND (t1.chapter_id = ? OR t2.chapter_id = ?)""", chapterId, chapterId));
        return ch;
    }

    public record AttemptRequest(List<String> chosen, BigDecimal natValue, Integer timeMs, String mode) {}

    /** Record an attempt and send back the result with the analysis. */
    @PostMapping("/questions/{id}/attempts")
    public Map<String, Object> attempt(@PathVariable int id, @RequestBody AttemptRequest req, Authentication auth) {
        Long uid = userId(auth);
        if (uid == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "sign in to attempt questions");
        List<Map<String, Object>> qs = jdbc.queryForList(
            "SELECT answer_type, nat_min, nat_max FROM question WHERE id = ? AND status = 'READY'", id);
        if (qs.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "question not found");
        Map<String, Object> q = qs.get(0);
        String type = (String) q.get("answer_type");
        String mode = req.mode() == null ? "STUDY" : req.mode().toUpperCase();
        if (!Set.of("STUDY", "PRACTICE", "TEST", "REVISION").contains(mode)) mode = "STUDY";

        Boolean correct;
        String chosen = null;
        if ("NAT".equals(type)) {
            if (req.natValue() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "natValue is required");
            BigDecimal min = (BigDecimal) q.get("nat_min"), max = (BigDecimal) q.get("nat_max");
            correct = min != null && max != null && req.natValue().compareTo(min) >= 0 && req.natValue().compareTo(max) <= 0;
        } else {
            if (req.chosen() == null || req.chosen().isEmpty())
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "choose an option");
            Set<String> picked = req.chosen().stream().map(String::trim).map(String::toUpperCase).collect(Collectors.toCollection(TreeSet::new));
            Set<String> right = new TreeSet<>(jdbc.queryForList(
                "SELECT label FROM question_option WHERE question_id = ? AND is_correct", String.class, id));
            Set<String> valid = new TreeSet<>(jdbc.queryForList(
                "SELECT label FROM question_option WHERE question_id = ?", String.class, id));
            if (!valid.containsAll(picked)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "unknown option");
            // MCQ: one pick, right if it is a correct option (a defective question can have two correct options).
            // MSQ: the picked set must equal the correct set.
            correct = "MSQ".equals(type) ? picked.equals(right) : (picked.size() == 1 && right.containsAll(picked));
            chosen = String.join(",", picked);
        }
        Long attemptId = jdbc.queryForObject("""
            INSERT INTO attempt(user_id, question_id, chosen_labels, nat_value, is_correct, time_ms, mode)
            VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id""", Long.class,
            uid, id, chosen, req.natValue(), correct, req.timeMs(), mode);
        jdbc.update("""
            INSERT INTO user_question_state(user_id, question_id, last_correct, times_seen, times_correct, last_attempt_at, updated_at)
            VALUES (?, ?, ?, 1, ?, now(), now())
            ON CONFLICT (user_id, question_id) DO UPDATE SET last_correct = EXCLUDED.last_correct,
              times_seen = user_question_state.times_seen + 1,
              times_correct = user_question_state.times_correct + EXCLUDED.times_correct,
              last_attempt_at = now(), updated_at = now()""", uid, id, correct, correct ? 1 : 0);

        Map<String, Object> out = new LinkedHashMap<>();
        out.put("attemptId", attemptId);
        out.put("correct", correct);
        out.putAll(review(id, uid));
        return out;
    }

    // ---------- helpers ----------

    private void attachQuestionDetails(List<Map<String, Object>> questions, Long uid) {
        if (questions.isEmpty()) return;
        Integer[] ids = questions.stream().map(m -> ((Number) m.get("id")).intValue()).toArray(Integer[]::new);
        String in = String.join(",", Arrays.stream(ids).map(i -> "?").toList());
        List<Map<String, Object>> sources = jdbc.queryForList("""
            SELECT qs.question_id AS qid, s.code, s.kind, s.exam, s.year, s.set_label AS "setLabel", qs.q_no AS "qNo",
                   qs.marks, qs.is_primary AS "primary", qs.source_url AS url
            FROM question_source qs JOIN source s ON s.id = qs.source_id
            WHERE qs.question_id IN (%s) ORDER BY qs.is_primary DESC, s.year, s.code""".formatted(in), (Object[]) ids);
        List<Map<String, Object>> options = jdbc.queryForList("""
            SELECT question_id AS qid, label, body_md AS "bodyMd" FROM question_option
            WHERE question_id IN (%s) ORDER BY label""".formatted(in), (Object[]) ids);
        Map<Integer, Map<String, Object>> last = new LinkedHashMap<>();
        if (uid != null) {
            List<Object> args = new ArrayList<>(Arrays.asList((Object[]) ids));
            args.add(0, uid);
            for (Map<String, Object> a : jdbc.queryForList("""
                SELECT DISTINCT ON (question_id) question_id AS qid, chosen_labels AS chosen, nat_value AS "natValue",
                       is_correct AS correct, attempted_at AS at,
                       count(*) OVER (PARTITION BY question_id) AS tries
                FROM attempt WHERE user_id = ? AND question_id IN (%s)
                ORDER BY question_id, attempted_at DESC""".formatted(in), args.toArray())) {
                last.put(((Number) a.get("qid")).intValue(), a);
            }
        }
        for (Map<String, Object> q : questions) {
            int id = ((Number) q.get("id")).intValue();
            q.put("sources", sources.stream().filter(s -> ((Number) s.get("qid")).intValue() == id).toList());
            q.put("options", options.stream().filter(o -> ((Number) o.get("qid")).intValue() == id)
                .map(o -> Map.of("label", o.get("label"), "bodyMd", o.get("bodyMd"))).toList());
            Map<String, Object> a = last.get(id);
            q.put("attempt", a);
            if (a != null) q.put("review", review(id, uid));
        }
    }

    /** The correct answer and the analysis. Callers must have checked that the user attempted the question. */
    private Map<String, Object> review(int id, Long uid) {
        Map<String, Object> r = new LinkedHashMap<>(jdbc.queryForMap("""
            SELECT q.solution_md AS "solutionMd", q.nat_min AS "natMin", q.nat_max AS "natMax", q.answer_status AS "answerStatus"
            FROM question q WHERE q.id = ?""", id));
        r.put("correctLabels", jdbc.queryForList(
            "SELECT label FROM question_option WHERE question_id = ? AND is_correct ORDER BY label", String.class, id));
        return r;
    }

    private Map<String, String> figures(String sql, Object... args) {
        Map<String, String> out = new LinkedHashMap<>();
        jdbc.query(sql, rs -> { out.put(rs.getString(1), rs.getString(2)); }, args);
        return out;
    }

    private static Long userId(Authentication auth) {
        if (auth == null || !auth.isAuthenticated() || auth.getName() == null) return null;
        try { return Long.parseLong(auth.getName()); } catch (NumberFormatException e) { return null; }
    }
}
