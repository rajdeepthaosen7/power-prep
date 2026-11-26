package com.rajdeep.powerprep.study;

import com.rajdeep.powerprep.common.jdbc.Rows;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
class JdbcStudyRepository implements StudyRepository {
    private final JdbcClient jdbc;

    JdbcStudyRepository(JdbcClient jdbc) { this.jdbc = jdbc; }

    @Override
    public List<ChapterRow> chaptersOf(String subjectSlug) {
        return jdbc.sql("""
                SELECT c.id, c.slug, c.name, c.description, c.sort_order, s.name AS subject_name
                FROM chapter c JOIN subject s ON s.id = c.subject_id
                WHERE s.slug = :slug ORDER BY c.sort_order""")
            .param("slug", subjectSlug).query(Rows.of(ChapterRow.class)).list();
    }

    @Override
    public List<TopicSummary> topicSummaries(String subjectSlug, long userId) {
        return jdbc.sql("""
                SELECT t.id, t.slug, t.name, t.chapter_id, t.sort_order,
                       (SELECT count(*) FROM concept_section cs WHERE cs.topic_id = t.id AND cs.status = 'PUBLISHED') AS notes,
                       (SELECT count(*) FROM question q WHERE q.topic_id = t.id AND q.status <> 'RETIRED' AND q.origin = 'PYQ') AS questions,
                       (SELECT count(*) FROM question q WHERE q.topic_id = t.id AND q.status = 'READY' AND q.origin = 'PYQ') AS ready,
                       (SELECT count(DISTINCT a.question_id) FROM attempt a JOIN question q ON q.id = a.question_id
                         WHERE q.topic_id = t.id AND q.origin = 'PYQ' AND a.user_id = :uid) AS attempted
                FROM topic t JOIN subject s ON s.id = t.subject_id
                WHERE s.slug = :slug AND t.chapter_id IS NOT NULL
                ORDER BY t.sort_order, t.name""")
            .param("uid", userId).param("slug", subjectSlug).query(Rows.of(TopicSummary.class)).list();
    }

    @Override
    public Optional<ChapterRow> chapter(String subjectSlug, String chapterSlug) {
        return jdbc.sql("""
                SELECT c.id, c.slug, c.name, c.description, c.sort_order, s.name AS subject_name
                FROM chapter c JOIN subject s ON s.id = c.subject_id WHERE s.slug = :subject AND c.slug = :chapter""")
            .param("subject", subjectSlug).param("chapter", chapterSlug).query(Rows.of(ChapterRow.class)).optional();
    }

    @Override
    public List<TopicRef> topicsOf(int chapterId) {
        return jdbc.sql("SELECT t.id, t.slug, t.name FROM topic t WHERE t.chapter_id = :id ORDER BY t.sort_order, t.name")
            .param("id", chapterId).query(Rows.of(TopicRef.class)).list();
    }

    @Override
    public List<NoteSection> publishedSections(int chapterId) {
        return jdbc.sql("""
                SELECT cs.id, cs.topic_id, cs.slug, cs.title, cs.body_md
                FROM concept_section cs JOIN topic t ON t.id = cs.topic_id
                WHERE t.chapter_id = :id AND cs.status = 'PUBLISHED' ORDER BY cs.sort_order, cs.id""")
            .param("id", chapterId).query(Rows.of(NoteSection.class)).list();
    }

    @Override
    public List<QuestionRow> readyExamQuestions(int chapterId) {
        return jdbc.sql("""
                SELECT q.id, q.topic_id, q.content_key AS key, q.stem_md, q.answer_type, q.nature, q.difficulty, q.answer_status
                FROM question q JOIN topic t ON t.id = q.topic_id
                WHERE t.chapter_id = :id AND q.status = 'READY' AND q.origin = 'PYQ'
                ORDER BY t.sort_order, q.difficulty NULLS LAST, q.id""")
            .param("id", chapterId).query(Rows.of(QuestionRow.class)).list();
    }

    @Override
    public Map<String, String> figuresOfChapter(int chapterId) {
        Map<String, String> out = new LinkedHashMap<>();
        jdbc.sql("""
                SELECT f.storage_key, f.svg FROM figure f
                LEFT JOIN concept_section cs ON cs.id = f.concept_section_id
                LEFT JOIN topic t1 ON t1.id = cs.topic_id
                LEFT JOIN question q ON q.id = f.question_id
                LEFT JOIN topic t2 ON t2.id = q.topic_id
                WHERE f.svg IS NOT NULL AND (t1.chapter_id = :id OR t2.chapter_id = :id)""")
            .param("id", chapterId).query(rs -> { out.put(rs.getString(1), rs.getString(2)); });
        return out;
    }
}
