package com.rajdeep.powerprep.practice;

import com.rajdeep.powerprep.common.jdbc.Rows;
import java.util.List;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
class JdbcPracticeRepository implements PracticeRepository {
    private static final String FROM = """
             FROM question q
            JOIN question_source qs ON qs.question_id = q.id
            JOIN source so          ON so.id = qs.source_id
            JOIN topic t            ON t.id = q.topic_id
            JOIN subject s          ON s.id = q.subject_id
            WHERE q.status <> 'RETIRED'""";

    private final JdbcClient jdbc;

    JdbcPracticeRepository(JdbcClient jdbc) { this.jdbc = jdbc; }

    @Override
    public List<PivotTable.Header> headers(PivotDimension d, QuestionFilter f) {
        return jdbc.sql("SELECT " + d.key + " AS key, " + d.label + " AS label, count(DISTINCT q.id) AS total"
                        + FROM + f.where() + " GROUP BY 1, 2 ORDER BY " + d.sort + ", 2")
            .params(f.params()).query(Rows.of(PivotTable.Header.class)).list();
    }

    @Override
    public List<PivotTable.Cell> cells(PivotDimension r, PivotDimension c, QuestionFilter f) {
        return jdbc.sql("SELECT " + r.key + " AS row, " + c.key + " AS col, count(DISTINCT q.id) AS count"
                        + FROM + f.where() + " GROUP BY 1, 2")
            .params(f.params()).query(Rows.of(PivotTable.Cell.class)).list();
    }

    @Override
    public long total(QuestionFilter f) {
        return jdbc.sql("SELECT count(DISTINCT q.id)" + FROM + f.where()).params(f.params()).query(Long.class).single();
    }

    @Override
    public List<PracticeQuestion> questions(QuestionFilter f, int limit) {
        return jdbc.sql("""
                SELECT q.id, s.name AS subject, t.id AS topic_id, t.name AS topic, q.nature, q.status,
                       q.summary, q.stem_md AS stem,
                       string_agg(DISTINCT so.code || ' Q' || qs.q_no, ', ') AS sources,
                       min(so.year) AS first_year""" + FROM + f.where() + " " + """
                 GROUP BY q.id, s.name, t.id, t.name
                 ORDER BY min(so.year), q.id
                 LIMIT :limit""")
            .params(f.params()).param("limit", limit).query(Rows.of(PracticeQuestion.class)).list();
    }
}
