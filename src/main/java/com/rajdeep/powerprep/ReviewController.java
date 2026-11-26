package com.rajdeep.powerprep;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * The daily loop of the prep strategy:
 *  - Today: what to study next on each track, revisions due, open mistakes, mocks to retake.
 *  - Mistake log: every question the user got wrong (Study or Tests), with a one-tap reason, re-attempted until right.
 *  - Spaced revision: a chapter is revised 1, 7 and 21 days after it was studied.
 */
@RestController
@RequestMapping("/api")
public class ReviewController {
    static final Set<String> REASONS = Set.of("CONCEPT", "FORMULA", "CALCULATION", "MISREAD", "GUESS", "TIME");
    static final int[] REVISION_DAYS = {1, 7, 21};
    static final int MOCK_TARGET_PCT = 85;

    private final JdbcTemplate jdbc;

    public ReviewController(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    // ------------------------------------------------------------------ mistake log

    /** Every question the user has answered wrong at least once. "open" = the latest attempt is still wrong. */
    @GetMapping("/mistakes")
    public Map<String, Object> mistakes(Authentication auth) {
        long uid = requireUser(auth);
        List<Map<String, Object>> rows = jdbc.queryForList("""
            WITH a AS (
              SELECT question_id, is_correct, chosen_labels, nat_value, mode, attempted_at,
                     row_number() OVER (PARTITION BY question_id ORDER BY attempted_at DESC, id DESC) AS rn,
                     count(*) FILTER (WHERE is_correct = FALSE) OVER (PARTITION BY question_id) AS wrong,
                     max(attempted_at) FILTER (WHERE is_correct = FALSE) OVER (PARTITION BY question_id) AS last_wrong_at
              FROM attempt WHERE user_id = ?)
            SELECT q.id, q.stem_md AS "stemMd", q.answer_type AS "answerType", q.origin, q.difficulty,
                   q.solution_md AS "solutionMd", q.answer_status AS "answerStatus", q.nat_min AS "natMin", q.nat_max AS "natMax",
                   t.name AS topic, c.slug AS "chapterSlug", c.name AS "chapterName", c.sort_order AS "chapterSort",
                   s.slug AS "subjectSlug", s.name AS "subjectName",
                   a.is_correct AS "lastCorrect", a.chosen_labels AS "lastChosen", a.nat_value AS "lastNat", a.mode AS "lastMode",
                   a.attempted_at AS "lastAt", a.wrong, a.last_wrong_at AS "lastWrongAt", mn.reason
            FROM a JOIN question q ON q.id = a.question_id JOIN topic t ON t.id = q.topic_id JOIN subject s ON s.id = q.subject_id
            LEFT JOIN chapter c ON c.id = t.chapter_id
            LEFT JOIN mistake_note mn ON mn.user_id = ? AND mn.question_id = q.id
            WHERE a.rn = 1 AND a.wrong > 0 AND q.stem_md IS NOT NULL
            ORDER BY a.is_correct, a.last_wrong_at DESC""", uid, uid);
        if (!rows.isEmpty()) {
            Integer[] ids = rows.stream().map(r -> ((Number) r.get("id")).intValue()).toArray(Integer[]::new);
            String in = String.join(",", Arrays.stream(ids).map(i -> "?").toList());
            List<Map<String, Object>> opts = jdbc.queryForList("""
                SELECT question_id AS qid, label, body_md AS "bodyMd", is_correct AS correct FROM question_option
                WHERE question_id IN (%s) ORDER BY label""".formatted(in), (Object[]) ids);
            List<Map<String, Object>> srcs = jdbc.queryForList("""
                SELECT qs.question_id AS qid, s.code, s.kind, s.exam, s.year, s.set_label AS "setLabel", qs.q_no AS "qNo",
                       qs.marks, qs.is_primary AS "primary", qs.source_url AS url
                FROM question_source qs JOIN source s ON s.id = qs.source_id
                WHERE qs.question_id IN (%s) ORDER BY qs.is_primary DESC, s.year""".formatted(in), (Object[]) ids);
            for (Map<String, Object> r : rows) {
                int id = ((Number) r.get("id")).intValue();
                List<Map<String, Object>> o = opts.stream().filter(x -> ((Number) x.get("qid")).intValue() == id).toList();
                r.put("options", o.stream().map(x -> Map.of("label", x.get("label"), "bodyMd", x.get("bodyMd"))).toList());
                r.put("correctLabels", o.stream().filter(x -> Boolean.TRUE.equals(x.get("correct"))).map(x -> x.get("label")).toList());
                r.put("sources", srcs.stream().filter(x -> ((Number) x.get("qid")).intValue() == id).toList());
            }
        }
        Map<String, Long> byReason = new LinkedHashMap<>();
        for (String k : List.of("CONCEPT", "FORMULA", "CALCULATION", "MISREAD", "GUESS", "TIME")) byReason.put(k, 0L);
        long untagged = 0;
        for (Map<String, Object> r : rows) {
            if (Boolean.TRUE.equals(r.get("lastCorrect"))) continue;
            if (r.get("reason") == null) untagged++; else byReason.merge((String) r.get("reason"), 1L, Long::sum);
        }
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("questions", rows);
        out.put("byReason", byReason);
        out.put("untagged", untagged);
        out.put("figures", figures(rows));
        return out;
    }

    public record ReasonRequest(String reason) {}

    @PostMapping("/mistakes/{questionId}/reason")
    public Map<String, Object> reason(@PathVariable int questionId, @RequestBody ReasonRequest req, Authentication auth) {
        long uid = requireUser(auth);
        String r = req.reason() == null || req.reason().isBlank() ? null : req.reason().trim().toUpperCase();
        if (r != null && !REASONS.contains(r)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "unknown reason");
        Integer wrong = jdbc.queryForObject("SELECT count(*) FROM attempt WHERE user_id = ? AND question_id = ? AND is_correct = FALSE",
            Integer.class, uid, questionId);
        if (wrong == null || wrong == 0) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "not in your mistake log");
        if (r == null) jdbc.update("DELETE FROM mistake_note WHERE user_id = ? AND question_id = ?", uid, questionId);
        else jdbc.update("""
            INSERT INTO mistake_note(user_id, question_id, reason) VALUES (?, ?, ?)
            ON CONFLICT (user_id, question_id) DO UPDATE SET reason = EXCLUDED.reason, updated_at = now()""", uid, questionId, r);
        return Map.of("questionId", questionId, "reason", r == null ? "" : r);
    }

    // ------------------------------------------------------------------ spaced revision of chapters

    public record ProgressRequest(String action) {}

    /** action: "studied" (start the 1/7/21-day clock), "revised" (finish the next due round), "reset" (forget the chapter). */
    @PostMapping("/chapters/{id}/progress")
    public Map<String, Object> progress(@PathVariable int id, @RequestBody ProgressRequest req, Authentication auth) {
        long uid = requireUser(auth);
        Integer exists = jdbc.queryForObject("SELECT count(*) FROM chapter WHERE id = ?", Integer.class, id);
        if (exists == null || exists == 0) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "chapter not found");
        String action = req.action() == null ? "" : req.action().toLowerCase();
        switch (action) {
            case "studied" -> jdbc.update("""
                INSERT INTO chapter_progress(user_id, chapter_id) VALUES (?, ?) ON CONFLICT DO NOTHING""", uid, id);
            case "revised" -> {
                Map<String, Object> p = progressRow(uid, id);
                if (p == null) throw new ResponseStatusException(HttpStatus.CONFLICT, "mark the chapter as studied first");
                Integer round = nextRound(p);
                if (round == null) throw new ResponseStatusException(HttpStatus.CONFLICT, "all three revisions are done");
                jdbc.update("UPDATE chapter_progress SET rev" + round + "_at = now() WHERE user_id = ? AND chapter_id = ?", uid, id);
            }
            case "reset" -> jdbc.update("DELETE FROM chapter_progress WHERE user_id = ? AND chapter_id = ?", uid, id);
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "action must be studied, revised or reset");
        }
        return progressView(progressRow(uid, id));
    }

    /** Progress of one chapter for the chapter page (null when not studied yet). */
    Map<String, Object> progressOf(long uid, int chapterId) {
        Map<String, Object> p = progressRow(uid, chapterId);
        return p == null ? null : progressView(p);
    }

    // ------------------------------------------------------------------ today

    @GetMapping("/today")
    public Map<String, Object> today(Authentication auth) {
        long uid = requireUser(auth);
        autoMarkStudied(uid);

        // 1. next chapter to study on each track (subjects that have notes), in the order of the chapters
        List<Map<String, Object>> chapters = jdbc.queryForList("""
            SELECT s.slug AS "subjectSlug", s.name AS "subjectName", c.id, c.slug, c.name, c.sort_order AS "sortOrder",
                   (SELECT count(*) FROM question q JOIN topic t ON t.id = q.topic_id
                     WHERE t.chapter_id = c.id AND q.status = 'READY' AND q.origin = 'PYQ') AS ready,
                   (SELECT count(DISTINCT a.question_id) FROM attempt a JOIN question q ON q.id = a.question_id JOIN topic t ON t.id = q.topic_id
                     WHERE t.chapter_id = c.id AND q.origin = 'PYQ' AND a.user_id = ?) AS attempted,
                   EXISTS (SELECT 1 FROM chapter_progress cp WHERE cp.user_id = ? AND cp.chapter_id = c.id) AS studied
            FROM chapter c JOIN subject s ON s.id = c.subject_id
            WHERE EXISTS (SELECT 1 FROM topic t JOIN concept_section cs ON cs.topic_id = t.id WHERE t.chapter_id = c.id)
            ORDER BY CASE s.slug WHEN 'electric-circuits' THEN 0 WHEN 'power-systems' THEN 1 ELSE 2 END, s.name, c.sort_order""", uid, uid);
        Map<String, Map<String, Object>> tracks = new LinkedHashMap<>();
        Map<String, Integer> readyChapters = new LinkedHashMap<>();
        for (Map<String, Object> c : chapters) {
            String subj = (String) c.get("subjectSlug");
            readyChapters.merge(subj, 1, Integer::sum);
            if (tracks.containsKey(subj) || Boolean.TRUE.equals(c.get("studied"))) continue;
            Map<String, Object> t = new LinkedHashMap<>(c);
            t.put("topic", jdbc.queryForList("""
                SELECT t.slug, t.name FROM topic t WHERE t.chapter_id = ?
                  AND EXISTS (SELECT 1 FROM question q WHERE q.topic_id = t.id AND q.status = 'READY' AND q.origin = 'PYQ'
                              AND NOT EXISTS (SELECT 1 FROM attempt a WHERE a.question_id = q.id AND a.user_id = ?))
                ORDER BY t.sort_order LIMIT 1""", c.get("id"), uid).stream().findFirst().orElse(null));
            tracks.put(subj, t);
        }
        List<Map<String, Object>> trackList = new ArrayList<>();
        for (Map<String, Object> c : chapters) {               // keep one row per subject, even when every chapter is studied
            String subj = (String) c.get("subjectSlug");
            if (trackList.stream().anyMatch(x -> subj.equals(x.get("subjectSlug")))) continue;
            Map<String, Object> t = tracks.get(subj);
            if (t == null) {
                t = new LinkedHashMap<>();
                t.put("subjectSlug", subj);
                t.put("subjectName", c.get("subjectName"));
                t.put("allStudied", true);
            }
            t.put("chaptersReady", readyChapters.get(subj));
            trackList.add(t);
        }

        // 2. revisions due now, and the next few coming up
        List<Map<String, Object>> due = new ArrayList<>(), upcoming = new ArrayList<>();
        for (Map<String, Object> p : jdbc.queryForList("""
                SELECT cp.*, c.slug AS "chapterSlug", c.name AS "chapterName", c.sort_order AS "chapterSort", s.slug AS "subjectSlug", s.name AS "subjectName",
                       (SELECT t.slug FROM topic t WHERE t.chapter_id = c.id AND t.slug LIKE '%-chapter-summary' LIMIT 1) AS "summaryTopic",
                       (SELECT count(*) FROM attempt a JOIN question q ON q.id = a.question_id JOIN topic t ON t.id = q.topic_id
                         WHERE t.chapter_id = c.id AND a.user_id = cp.user_id AND a.is_correct = FALSE
                           AND NOT EXISTS (SELECT 1 FROM attempt b WHERE b.user_id = a.user_id AND b.question_id = a.question_id
                                           AND b.attempted_at > a.attempted_at)) AS "openMistakes"
                FROM chapter_progress cp JOIN chapter c ON c.id = cp.chapter_id JOIN subject s ON s.id = c.subject_id
                WHERE cp.user_id = ?""", uid)) {
            Map<String, Object> v = progressView(p);
            if (v.get("nextRound") == null) continue;
            Map<String, Object> r = new LinkedHashMap<>(v);
            for (String k : List.of("chapter_id", "chapterSlug", "chapterName", "chapterSort", "subjectSlug", "subjectName", "summaryTopic", "openMistakes"))
                r.put(k.equals("chapter_id") ? "chapterId" : k, p.get(k));
            (Boolean.TRUE.equals(v.get("isDue")) ? due : upcoming).add(r);
        }
        upcoming.sort((a, b) -> ((Instant) a.get("dueAt")).compareTo((Instant) b.get("dueAt")));

        // 3. mistakes
        Map<String, Object> mistakes = jdbc.queryForMap("""
            WITH last AS (SELECT DISTINCT ON (question_id) question_id, is_correct FROM attempt WHERE user_id = ?
                          ORDER BY question_id, attempted_at DESC, id DESC)
            SELECT count(*) FILTER (WHERE NOT l.is_correct) AS open,
                   count(*) FILTER (WHERE NOT l.is_correct AND mn.reason IS NULL) AS untagged
            FROM last l JOIN question q ON q.id = l.question_id AND q.stem_md IS NOT NULL
            LEFT JOIN mistake_note mn ON mn.user_id = ? AND mn.question_id = l.question_id""", uid, uid);

        // 4. mocks: for studied chapters, the mixed mock until it reaches the target
        List<Map<String, Object>> mocks = jdbc.queryForList("""
            SELECT t.id, t.title, t.variant, c.name AS "chapterName", c.sort_order AS "chapterSort", s.slug AS "subjectSlug", s.name AS "subjectName",
                   (SELECT max(ta.score / NULLIF(ta.max_score, 0)) * 100 FROM test_attempt ta
                     WHERE ta.test_id = t.id AND ta.user_id = ? AND ta.status = 'SUBMITTED') AS "bestPct",
                   (SELECT count(*) FROM test_question tq WHERE tq.test_id = t.id) AS questions, t.duration_min AS "durationMin"
            FROM test t JOIN chapter c ON c.id = t.chapter_id JOIN subject s ON s.id = c.subject_id
            JOIN chapter_progress cp ON cp.chapter_id = c.id AND cp.user_id = ?
            WHERE t.variant = 'MIX' AND t.status = 'PUBLISHED'
            ORDER BY s.name, c.sort_order""", uid, uid);
        mocks.removeIf(m -> m.get("bestPct") != null && ((Number) m.get("bestPct")).doubleValue() >= MOCK_TARGET_PCT);

        Map<String, Object> week = jdbc.queryForMap("""
            SELECT count(*) FILTER (WHERE attempted_at >= date_trunc('day', now() AT TIME ZONE 'Asia/Kolkata') AT TIME ZONE 'Asia/Kolkata') AS today,
                   count(*) AS week, count(*) FILTER (WHERE is_correct) AS "weekCorrect"
            FROM attempt WHERE user_id = ? AND attempted_at >= now() - interval '7 days'""", uid);

        Map<String, Object> out = new LinkedHashMap<>();
        out.put("tracks", trackList);
        out.put("revisionsDue", due);
        out.put("revisionsUpcoming", upcoming.subList(0, Math.min(3, upcoming.size())));
        out.put("mistakes", mistakes);
        out.put("mocks", mocks);
        out.put("mockTargetPct", MOCK_TARGET_PCT);
        out.put("activity", week);
        return out;
    }

    /** A chapter counts as studied once every one of its written exam questions has been attempted. */
    private void autoMarkStudied(long uid) {
        jdbc.update("""
            INSERT INTO chapter_progress(user_id, chapter_id, studied_at)
            SELECT ?, c.id, (SELECT max(a.attempted_at) FROM attempt a JOIN question q ON q.id = a.question_id JOIN topic t ON t.id = q.topic_id
                             WHERE t.chapter_id = c.id AND a.user_id = ?)
            FROM chapter c
            WHERE EXISTS (SELECT 1 FROM question q JOIN topic t ON t.id = q.topic_id WHERE t.chapter_id = c.id AND q.status = 'READY' AND q.origin = 'PYQ')
              AND NOT EXISTS (SELECT 1 FROM question q JOIN topic t ON t.id = q.topic_id
                              WHERE t.chapter_id = c.id AND q.status = 'READY' AND q.origin = 'PYQ'
                                AND NOT EXISTS (SELECT 1 FROM attempt a WHERE a.question_id = q.id AND a.user_id = ?))
            ON CONFLICT DO NOTHING""", uid, uid, uid);
    }

    // ------------------------------------------------------------------ helpers

    private Map<String, Object> progressRow(long uid, int chapterId) {
        List<Map<String, Object>> r = jdbc.queryForList("SELECT * FROM chapter_progress WHERE user_id = ? AND chapter_id = ?", uid, chapterId);
        return r.isEmpty() ? null : r.get(0);
    }

    private static Integer nextRound(Map<String, Object> p) {
        for (int k = 1; k <= 3; k++) if (p.get("rev" + k + "_at") == null) return k;
        return null;
    }

    static Map<String, Object> progressView(Map<String, Object> p) {
        if (p == null) return null;
        Instant studied = ((Timestamp) p.get("studied_at")).toInstant();
        Map<String, Object> v = new LinkedHashMap<>();
        v.put("studiedAt", studied);
        List<Map<String, Object>> rounds = new ArrayList<>();
        for (int k = 1; k <= 3; k++) {
            Object done = p.get("rev" + k + "_at");
            Map<String, Object> r = new LinkedHashMap<>();
            r.put("round", k);
            r.put("dueAt", studied.plus(Duration.ofDays(REVISION_DAYS[k - 1])));
            r.put("doneAt", done == null ? null : ((Timestamp) done).toInstant());
            rounds.add(r);
        }
        v.put("rounds", rounds);
        Integer next = nextRound(p);
        v.put("nextRound", next);
        if (next != null) {
            Instant dueAt = studied.plus(Duration.ofDays(REVISION_DAYS[next - 1]));
            v.put("dueAt", dueAt);
            v.put("isDue", !dueAt.isAfter(Instant.now()));
        }
        return v;
    }

    private Map<String, String> figures(List<Map<String, Object>> rows) {
        Map<String, String> out = new LinkedHashMap<>();
        if (rows.isEmpty()) return out;
        Integer[] ids = rows.stream().map(r -> ((Number) r.get("id")).intValue()).toArray(Integer[]::new);
        String in = String.join(",", Arrays.stream(ids).map(i -> "?").toList());
        jdbc.query("SELECT storage_key, svg FROM figure WHERE svg IS NOT NULL AND question_id IN (%s)".formatted(in),
            rs -> { out.put(rs.getString(1), rs.getString(2)); }, (Object[]) ids);
        return out;
    }

    private static long requireUser(Authentication auth) {
        if (auth == null || !auth.isAuthenticated() || auth.getName() == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "sign in first");
        try { return Long.parseLong(auth.getName()); }
        catch (NumberFormatException e) { throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "sign in first"); }
    }
}
