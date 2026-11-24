package com.rajdeep.powerprep;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * Practice tab: PYQ counts as a pivot table, and the question list behind each cell.
 * Dimensions are whitelisted; values are always bound as parameters.
 */
@RestController
@RequestMapping("/api/practice")
public class PracticeController {

    /** dimension -> {key expression, label expression, sort expression} */
    private static final Map<String, String[]> DIMS = Map.of(
            "subject", new String[]{"s.slug",            "s.name",            "min(s.sort_order)"},
            "topic",   new String[]{"t.id::text",        "t.name",            "min(t.name)"},
            "exam",    new String[]{"so.exam",           "so.exam",           "min(CASE so.kind WHEN 'STATE' THEN 0 ELSE 1 END || so.exam)"},
            "year",    new String[]{"so.year::text",     "so.year::text",     "min(so.year)"},
            "paper",   new String[]{"so.code",           "so.code",           "min(so.kind || so.year || so.code)"},
            "kind",    new String[]{"so.kind",           "so.kind",           "min(so.kind)"},
            "nature",  new String[]{"coalesce(q.nature,'UNTAGGED')", "coalesce(q.nature,'UNTAGGED')", "min(coalesce(q.nature,'UNTAGGED'))"});

    private static final String FROM = """
            FROM question q
            JOIN question_source qs ON qs.question_id = q.id
            JOIN source so          ON so.id = qs.source_id
            JOIN topic t            ON t.id = q.topic_id
            JOIN subject s          ON s.id = q.subject_id
            WHERE q.status <> 'RETIRED'""";

    private final JdbcTemplate jdbc;
    public PracticeController(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    record Filter(String where, List<Object> args) {}

    private static Filter filter(String subject, Integer topic, String exam, Integer year, String kind, String paper) {
        StringBuilder w = new StringBuilder();
        List<Object> a = new ArrayList<>();
        if (subject != null) { w.append(" AND s.slug = ?");   a.add(subject); }
        if (topic != null)   { w.append(" AND t.id = ?");     a.add(topic); }
        if (exam != null)    { w.append(" AND so.exam = ?");  a.add(exam); }
        if (year != null)    { w.append(" AND so.year = ?");  a.add(year); }
        if (kind != null)    { w.append(" AND so.kind = ?");  a.add(kind); }
        if (paper != null)   { w.append(" AND so.code = ?");  a.add(paper); }
        return new Filter(w.toString(), a);
    }

    private static String[] dim(String name) {
        String[] d = DIMS.get(name);
        if (d == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "unknown dimension: " + name);
        return d;
    }

    @GetMapping("/pivot")
    public Map<String, Object> pivot(@RequestParam(defaultValue = "subject") String rows,
                                     @RequestParam(defaultValue = "exam") String cols,
                                     @RequestParam(required = false) String subject,
                                     @RequestParam(required = false) Integer topic,
                                     @RequestParam(required = false) String exam,
                                     @RequestParam(required = false) Integer year,
                                     @RequestParam(required = false) String kind,
                                     @RequestParam(required = false) String paper) {
        if (rows.equals(cols)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "rows and cols must differ");
        String[] r = dim(rows), c = dim(cols);
        Filter f = filter(subject, topic, exam, year, kind, paper);

        List<Map<String, Object>> rowHeads = jdbc.queryForList(
                "SELECT " + r[0] + " AS key, " + r[1] + " AS label, count(DISTINCT q.id) AS total " + FROM + f.where()
                + " GROUP BY 1, 2 ORDER BY " + r[2] + ", 2", f.args().toArray());
        List<Map<String, Object>> colHeads = jdbc.queryForList(
                "SELECT " + c[0] + " AS key, " + c[1] + " AS label, count(DISTINCT q.id) AS total " + FROM + f.where()
                + " GROUP BY 1, 2 ORDER BY " + c[2] + ", 2", f.args().toArray());
        List<Map<String, Object>> cells = jdbc.queryForList(
                "SELECT " + r[0] + " AS row, " + c[0] + " AS col, count(DISTINCT q.id) AS count " + FROM + f.where()
                + " GROUP BY 1, 2", f.args().toArray());
        Integer total = jdbc.queryForObject("SELECT count(DISTINCT q.id) " + FROM + f.where(), Integer.class, f.args().toArray());

        Map<String, Object> out = new LinkedHashMap<>();
        out.put("rows", rowHeads);
        out.put("cols", colHeads);
        out.put("cells", cells);
        out.put("total", total);
        return out;
    }

    @GetMapping("/questions")
    public List<Map<String, Object>> questions(@RequestParam(required = false) String subject,
                                               @RequestParam(required = false) Integer topic,
                                               @RequestParam(required = false) String exam,
                                               @RequestParam(required = false) Integer year,
                                               @RequestParam(required = false) String kind,
                                               @RequestParam(required = false) String paper,
                                               @RequestParam(defaultValue = "500") int limit) {
        Filter f = filter(subject, topic, exam, year, kind, paper);
        List<Object> args = new ArrayList<>(f.args());
        args.add(Math.min(Math.max(limit, 1), 500));
        return jdbc.queryForList("""
            SELECT q.id, s.name AS subject, t.id AS "topicId", t.name AS topic, q.nature, q.status,
                   q.summary, q.stem_md AS stem,
                   string_agg(DISTINCT so.code || ' Q' || qs.q_no, ', ') AS sources,
                   min(so.year) AS "firstYear"
            """ + FROM + f.where() + " " + """
             GROUP BY q.id, s.name, t.id, t.name
             ORDER BY min(so.year), q.id
             LIMIT ?""", args.toArray());
    }
}
