package com.rajdeep.powerprep;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * Tests tab: chapter-wise mock tests in the APSC format (4-option MCQs, negative marking, timed).
 * Questions are sent without answers. Answers, analysis and sources come back only after the attempt is submitted.
 * Answers live in the browser while the test runs and are sent once on submit (fewer database writes).
 */
@RestController
@RequestMapping("/api")
public class TestController {
    private final JdbcTemplate jdbc;

    public TestController(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    /** Published tests of a subject, grouped by chapter, with the signed-in user's attempts. */
    @GetMapping("/tests")
    public Map<String, Object> list(@RequestParam String subject, Authentication auth) {
        Long uid = userId(auth);
        List<Map<String, Object>> chapters = jdbc.queryForList("""
            SELECT c.id, c.slug, c.name, c.sort_order AS "sortOrder"
            FROM chapter c JOIN subject s ON s.id = c.subject_id WHERE s.slug = ? ORDER BY c.sort_order""", subject);
        List<Map<String, Object>> tests = jdbc.queryForList("""
            SELECT t.id, t.code, t.title, t.variant, t.description, t.duration_min AS "durationMin",
                   t.marks_correct AS "marksCorrect", t.marks_wrong AS "marksWrong", t.chapter_id AS "chapterId",
                   (SELECT count(*) FROM test_question tq WHERE tq.test_id = t.id) AS questions
            FROM test t JOIN subject s ON s.id = t.subject_id
            WHERE s.slug = ? AND t.status = 'PUBLISHED' ORDER BY t.sort_order, t.id""", subject);
        Map<Object, List<Map<String, Object>>> attemptsByTest = new HashMap<>();
        if (uid != null && !tests.isEmpty()) {
            for (Map<String, Object> a : jdbc.queryForList("""
                SELECT ta.id, ta.test_id AS "testId", ta.status, ta.started_at AS "startedAt", ta.submitted_at AS "submittedAt",
                       ta.score, ta.max_score AS "maxScore", ta.n_correct AS "nCorrect", ta.n_wrong AS "nWrong",
                       ta.n_unattempted AS "nSkipped"
                FROM test_attempt ta JOIN test t ON t.id = ta.test_id JOIN subject s ON s.id = t.subject_id
                WHERE ta.user_id = ? AND s.slug = ? ORDER BY ta.started_at DESC""", uid, subject)) {
                attemptsByTest.computeIfAbsent(a.get("testId"), k -> new ArrayList<>()).add(a);
            }
        }
        for (Map<String, Object> t : tests) t.put("attempts", attemptsByTest.getOrDefault(t.get("id"), List.of()));
        for (Map<String, Object> c : chapters) {
            c.put("tests", tests.stream().filter(t -> c.get("id").equals(t.get("chapterId"))).toList());
        }
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("chapters", chapters);
        out.put("subjectTests", tests.stream().filter(t -> t.get("chapterId") == null).toList());
        return out;
    }

    /** Start a test, or resume the attempt that is still running. Questions come without answers. */
    @PostMapping("/tests/{id}/start")
    @Transactional
    public Map<String, Object> start(@PathVariable int id, Authentication auth) {
        long uid = requireUser(auth);
        Map<String, Object> test = testMeta(id);
        List<Long> open = jdbc.queryForList("""
            SELECT id FROM test_attempt WHERE user_id = ? AND test_id = ? AND status = 'IN_PROGRESS'
            ORDER BY started_at DESC LIMIT 1""", Long.class, uid, id);
        long attemptId = open.isEmpty()
            ? jdbc.queryForObject("INSERT INTO test_attempt(user_id, test_id) VALUES (?, ?) RETURNING id", Long.class, uid, id)
            : open.get(0);
        Timestamp started = jdbc.queryForObject("SELECT started_at FROM test_attempt WHERE id = ?", Timestamp.class, attemptId);

        List<Map<String, Object>> questions = jdbc.queryForList("""
            SELECT q.id, tq.position, q.stem_md AS "stemMd"
            FROM test_question tq JOIN question q ON q.id = tq.question_id
            WHERE tq.test_id = ? ORDER BY tq.position""", id);
        Map<Integer, List<Map<String, Object>>> options = optionsOf(questions, false);
        for (Map<String, Object> q : questions) q.put("options", options.getOrDefault(((Number) q.get("id")).intValue(), List.of()));

        Map<String, Object> out = new LinkedHashMap<>();
        out.put("attemptId", attemptId);
        out.put("startedAt", started.toInstant().toString());
        out.put("serverNow", Instant.now().toString());
        out.put("test", test);
        out.put("questions", questions);
        out.put("figures", figuresOfTest(id));
        return out;
    }

    public record Answer(int questionId, String chosen, Integer timeMs, Boolean marked) {}
    public record SubmitRequest(List<Answer> answers, Long timeMs) {}

    /** Submit all answers at once, grade them with negative marking, and return the full result. */
    @PostMapping("/test-attempts/{id}/submit")
    @Transactional
    public Map<String, Object> submit(@PathVariable long id, @RequestBody SubmitRequest req, Authentication auth) {
        long uid = requireUser(auth);
        Map<String, Object> ta = ownAttempt(id, uid);
        if (!"IN_PROGRESS".equals(ta.get("status"))) throw new ResponseStatusException(HttpStatus.CONFLICT, "this test is already submitted");
        int testId = ((Number) ta.get("test_id")).intValue();
        Map<String, Object> test = testMeta(testId);

        List<Integer> qids = jdbc.queryForList(
            "SELECT question_id FROM test_question WHERE test_id = ? ORDER BY position", Integer.class, testId);
        Map<Integer, Set<String>> right = new HashMap<>(), valid = new HashMap<>();
        jdbc.query("""
            SELECT o.question_id, o.label, o.is_correct FROM question_option o
            JOIN test_question tq ON tq.question_id = o.question_id WHERE tq.test_id = ?""", rs -> {
            int q = rs.getInt(1);
            valid.computeIfAbsent(q, k -> new TreeSet<>()).add(rs.getString(2));
            if (rs.getBoolean(3)) right.computeIfAbsent(q, k -> new TreeSet<>()).add(rs.getString(2));
        }, testId);

        Map<Integer, Answer> given = new HashMap<>();
        if (req.answers() != null) for (Answer a : req.answers()) given.put(a.questionId(), a);

        int nCorrect = 0, nWrong = 0, nSkipped = 0;
        for (int qid : qids) {
            Answer a = given.get(qid);
            String chosen = a == null || a.chosen() == null ? null : a.chosen().trim().toUpperCase();
            if (chosen == null || chosen.isEmpty()) {
                nSkipped++;
                continue;
            }
            if (!valid.getOrDefault(qid, Set.of()).contains(chosen))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "unknown option for question " + qid);
            boolean correct = right.getOrDefault(qid, Set.of()).contains(chosen);
            if (correct) nCorrect++; else nWrong++;
            jdbc.update("""
                INSERT INTO attempt(user_id, question_id, chosen_labels, is_correct, time_ms, mode, test_attempt_id)
                VALUES (?, ?, ?, ?, ?, 'TEST', ?)""", uid, qid, chosen, correct, a.timeMs(), id);
            jdbc.update("""
                INSERT INTO user_question_state(user_id, question_id, last_correct, times_seen, times_correct, last_attempt_at, updated_at)
                VALUES (?, ?, ?, 1, ?, now(), now())
                ON CONFLICT (user_id, question_id) DO UPDATE SET last_correct = EXCLUDED.last_correct,
                  times_seen = user_question_state.times_seen + 1,
                  times_correct = user_question_state.times_correct + EXCLUDED.times_correct,
                  last_attempt_at = now(), updated_at = now()""", uid, qid, correct, correct ? 1 : 0);
        }
        BigDecimal plus = (BigDecimal) test.get("marksCorrect"), minus = (BigDecimal) test.get("marksWrong");
        BigDecimal score = plus.multiply(BigDecimal.valueOf(nCorrect)).subtract(minus.multiply(BigDecimal.valueOf(nWrong)));
        BigDecimal max = plus.multiply(BigDecimal.valueOf(qids.size()));
        jdbc.update("""
            UPDATE test_attempt SET status = 'SUBMITTED', submitted_at = now(), score = ?, max_score = ?,
              n_correct = ?, n_wrong = ?, n_unattempted = ?, time_ms = ? WHERE id = ?""",
            score, max, nCorrect, nWrong, nSkipped, req.timeMs(), id);
        return result(id, auth);
    }

    /** Result of a submitted attempt: score, topic-wise split, and every question with its analysis. */
    @GetMapping("/test-attempts/{id}")
    public Map<String, Object> result(@PathVariable long id, Authentication auth) {
        long uid = requireUser(auth);
        Map<String, Object> ta = ownAttempt(id, uid);
        if (!"SUBMITTED".equals(ta.get("status"))) throw new ResponseStatusException(HttpStatus.CONFLICT, "this test is not submitted yet");
        int testId = ((Number) ta.get("test_id")).intValue();

        List<Map<String, Object>> questions = jdbc.queryForList("""
            SELECT q.id, tq.position, q.stem_md AS "stemMd", q.solution_md AS "solutionMd", q.answer_status AS "answerStatus",
                   q.origin, q.difficulty, t.name AS topic, t.slug AS "topicSlug", t.sort_order AS "topicSort",
                   a.chosen_labels AS chosen, a.is_correct AS correct, a.time_ms AS "timeMs"
            FROM test_question tq JOIN question q ON q.id = tq.question_id JOIN topic t ON t.id = q.topic_id
            LEFT JOIN attempt a ON a.test_attempt_id = ? AND a.question_id = q.id
            WHERE tq.test_id = ? ORDER BY tq.position""", id, testId);
        Map<Integer, List<Map<String, Object>>> options = optionsOf(questions, true);
        Map<Integer, List<Map<String, Object>>> sources = sourcesOf(questions);
        Map<String, Map<String, Object>> byTopic = new LinkedHashMap<>();
        for (Map<String, Object> q : questions) {
            int qid = ((Number) q.get("id")).intValue();
            List<Map<String, Object>> opts = options.getOrDefault(qid, List.of());
            q.put("options", opts.stream().map(o -> Map.of("label", o.get("label"), "bodyMd", o.get("bodyMd"))).toList());
            q.put("correctLabels", opts.stream().filter(o -> Boolean.TRUE.equals(o.get("correct"))).map(o -> o.get("label")).toList());
            q.put("sources", sources.getOrDefault(qid, List.of()));
            Map<String, Object> tp = byTopic.computeIfAbsent((String) q.get("topicSlug"), k -> {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("slug", k);
                m.put("name", q.get("topic"));
                m.put("sort", q.get("topicSort"));
                m.put("total", 0); m.put("correct", 0); m.put("wrong", 0); m.put("skipped", 0);
                return m;
            });
            tp.merge("total", 1, (x, y) -> (int) x + (int) y);
            String bucket = q.get("chosen") == null ? "skipped" : Boolean.TRUE.equals(q.get("correct")) ? "correct" : "wrong";
            tp.merge(bucket, 1, (x, y) -> (int) x + (int) y);
        }
        List<Map<String, Object>> topics = new ArrayList<>(byTopic.values());
        topics.sort((a, b) -> Integer.compare(((Number) a.get("sort")).intValue(), ((Number) b.get("sort")).intValue()));

        Map<String, Object> out = new LinkedHashMap<>();
        out.put("attemptId", id);
        out.put("test", testMeta(testId));
        out.put("startedAt", ta.get("started_at"));
        out.put("submittedAt", ta.get("submitted_at"));
        out.put("timeMs", ta.get("time_ms"));
        out.put("score", ta.get("score"));
        out.put("maxScore", ta.get("max_score"));
        out.put("nCorrect", ta.get("n_correct"));
        out.put("nWrong", ta.get("n_wrong"));
        out.put("nSkipped", ta.get("n_unattempted"));
        int attempted = ((Number) ta.get("n_correct")).intValue() + ((Number) ta.get("n_wrong")).intValue();
        out.put("accuracy", attempted == 0 ? null
            : BigDecimal.valueOf(100L * ((Number) ta.get("n_correct")).intValue()).divide(BigDecimal.valueOf(attempted), 0, RoundingMode.HALF_UP));
        out.put("topics", topics);
        out.put("questions", questions);
        out.put("figures", figuresOfTest(testId));
        out.put("history", jdbc.queryForList("""
            SELECT id, submitted_at AS "submittedAt", score, max_score AS "maxScore" FROM test_attempt
            WHERE user_id = ? AND test_id = ? AND status = 'SUBMITTED' ORDER BY submitted_at""", uid, testId));
        return out;
    }

    // ---------- helpers ----------

    private Map<String, Object> testMeta(int id) {
        List<Map<String, Object>> t = jdbc.queryForList("""
            SELECT t.id, t.code, t.title, t.variant, t.description, t.duration_min AS "durationMin",
                   t.marks_correct AS "marksCorrect", t.marks_wrong AS "marksWrong",
                   c.slug AS "chapterSlug", c.name AS "chapterName", s.slug AS "subjectSlug", s.name AS "subjectName",
                   (SELECT count(*) FROM test_question tq WHERE tq.test_id = t.id) AS questions
            FROM test t LEFT JOIN chapter c ON c.id = t.chapter_id LEFT JOIN subject s ON s.id = t.subject_id
            WHERE t.id = ? AND t.status = 'PUBLISHED'""", id);
        if (t.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "test not found");
        return t.get(0);
    }

    private Map<String, Object> ownAttempt(long id, long uid) {
        List<Map<String, Object>> r = jdbc.queryForList("SELECT * FROM test_attempt WHERE id = ? AND user_id = ?", id, uid);
        if (r.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "attempt not found");
        return r.get(0);
    }

    private Map<Integer, List<Map<String, Object>>> optionsOf(List<Map<String, Object>> questions, boolean withAnswers) {
        Map<Integer, List<Map<String, Object>>> out = new HashMap<>();
        if (questions.isEmpty()) return out;
        Object[] ids = questions.stream().map(m -> ((Number) m.get("id")).intValue()).toArray();
        String in = String.join(",", Arrays.stream(ids).map(i -> "?").toList());
        for (Map<String, Object> o : jdbc.queryForList("""
            SELECT question_id AS qid, label, body_md AS "bodyMd", is_correct AS correct FROM question_option
            WHERE question_id IN (%s) ORDER BY label""".formatted(in), ids)) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("label", o.get("label"));
            m.put("bodyMd", o.get("bodyMd"));
            if (withAnswers) m.put("correct", o.get("correct"));
            out.computeIfAbsent(((Number) o.get("qid")).intValue(), k -> new ArrayList<>()).add(m);
        }
        return out;
    }

    private Map<Integer, List<Map<String, Object>>> sourcesOf(List<Map<String, Object>> questions) {
        Map<Integer, List<Map<String, Object>>> out = new HashMap<>();
        if (questions.isEmpty()) return out;
        Object[] ids = questions.stream().map(m -> ((Number) m.get("id")).intValue()).toArray();
        String in = String.join(",", Arrays.stream(ids).map(i -> "?").toList());
        for (Map<String, Object> s : jdbc.queryForList("""
            SELECT qs.question_id AS qid, s.code, s.kind, s.exam, s.year, s.set_label AS "setLabel", qs.q_no AS "qNo",
                   qs.marks, qs.is_primary AS "primary", qs.source_url AS url
            FROM question_source qs JOIN source s ON s.id = qs.source_id
            WHERE qs.question_id IN (%s) ORDER BY qs.is_primary DESC, s.year, s.code""".formatted(in), ids)) {
            out.computeIfAbsent(((Number) s.get("qid")).intValue(), k -> new ArrayList<>()).add(s);
        }
        return out;
    }

    private Map<String, String> figuresOfTest(int testId) {
        Map<String, String> out = new LinkedHashMap<>();
        jdbc.query("""
            SELECT f.storage_key, f.svg FROM figure f JOIN test_question tq ON tq.question_id = f.question_id
            WHERE tq.test_id = ? AND f.svg IS NOT NULL""", rs -> { out.put(rs.getString(1), rs.getString(2)); }, testId);
        return out;
    }

    private static long requireUser(Authentication auth) {
        Long uid = userId(auth);
        if (uid == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "sign in to take tests");
        return uid;
    }

    private static Long userId(Authentication auth) {
        if (auth == null || !auth.isAuthenticated() || auth.getName() == null) return null;
        try { return Long.parseLong(auth.getName()); } catch (NumberFormatException e) { return null; }
    }
}
