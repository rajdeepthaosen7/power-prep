package com.rajdeep.powerprep.syllabus;

import com.rajdeep.powerprep.common.jdbc.Rows;
import java.util.List;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
class JdbcSyllabusRepository implements SyllabusRepository {
    /** Exam questions of the syllabus items, with the flags we count. */
    private static final String COUNT_COLUMNS = """
            count(DISTINCT q.id) AS questions,
            count(DISTINCT q.id) FILTER (WHERE EXISTS (SELECT 1 FROM question_source qs JOIN source so ON so.id = qs.source_id
                                                      WHERE qs.question_id = q.id AND so.kind = 'STATE')) AS apsc,
            count(DISTINCT q.id) FILTER (WHERE q.status = 'READY') AS written,
            count(DISTINCT q.id) FILTER (WHERE EXISTS (SELECT 1 FROM attempt a WHERE a.question_id = q.id AND a.user_id = :uid)) AS solved
            FROM syllabus_item_topic sit JOIN syllabus_item si ON si.id = sit.syllabus_item_id
            JOIN question q ON q.topic_id = sit.topic_id AND q.origin = 'PYQ' AND q.status <> 'RETIRED'
            WHERE si.exam_code = :exam""";

    private final JdbcClient jdbc;

    JdbcSyllabusRepository(JdbcClient jdbc) { this.jdbc = jdbc; }

    @Override
    public List<ItemRow> items(String exam) {
        return jdbc.sql("""
                SELECT si.id, si.code, si.parent_id, si.text, si.note, s.slug AS subject_slug, s.name AS subject_name
                FROM syllabus_item si LEFT JOIN subject s ON s.id = si.subject_id
                WHERE si.exam_code = :exam ORDER BY si.parent_id NULLS FIRST, si.sort_order""")
            .param("exam", exam).query(Rows.of(ItemRow.class)).list();
    }

    @Override
    public List<Counts> itemCounts(String exam, long userId) {
        return jdbc.sql("SELECT sit.syllabus_item_id AS id, " + COUNT_COLUMNS + " GROUP BY sit.syllabus_item_id")
            .param("exam", exam).param("uid", userId).query(Rows.of(Counts.class)).list();
    }

    @Override
    public List<Counts> sectionCounts(String exam, long userId) {
        return jdbc.sql("SELECT si.parent_id AS id, " + COUNT_COLUMNS + " GROUP BY si.parent_id")
            .param("exam", exam).param("uid", userId).query(Rows.of(Counts.class)).list();
    }

    @Override
    public List<ItemChapter> chapters(String exam) {
        return jdbc.sql("""
                SELECT DISTINCT sit.syllabus_item_id AS item_id, c.slug, c.name, c.sort_order, cs_sub.slug AS subject_slug,
                       EXISTS (SELECT 1 FROM topic t2 JOIN concept_section cs ON cs.topic_id = t2.id WHERE t2.chapter_id = c.id) AS notes
                FROM syllabus_item_topic sit JOIN syllabus_item si ON si.id = sit.syllabus_item_id
                JOIN topic t ON t.id = sit.topic_id JOIN chapter c ON c.id = t.chapter_id JOIN subject cs_sub ON cs_sub.id = c.subject_id
                WHERE si.exam_code = :exam ORDER BY c.sort_order""")
            .param("exam", exam).query(Rows.of(ItemChapter.class)).list();
    }
}
