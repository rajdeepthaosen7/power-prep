package com.rajdeep.powerprep.question;

import com.rajdeep.powerprep.common.jdbc.Rows;
import com.rajdeep.powerprep.question.grading.AnswerKey;
import com.rajdeep.powerprep.question.grading.AnswerType;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
class JdbcQuestionBank implements QuestionBank {
    private final JdbcClient jdbc;

    JdbcQuestionBank(JdbcClient jdbc) { this.jdbc = jdbc; }

    private record KeyRow(String answerType, BigDecimal natMin, BigDecimal natMax) {}
    private record SourceRow(int questionId, String code, String kind, String exam, Integer year, String setLabel,
                             String qNo, BigDecimal marks, boolean primary, String url) {
        SourceView view() { return new SourceView(code, kind, exam, year, setLabel, qNo, marks, primary, url); }
    }

    @Override
    public Optional<AnswerKey> answerKeyForAttempt(int questionId, long userId) {
        return jdbc.sql("""
                SELECT answer_type, nat_min, nat_max FROM question q WHERE q.id = :id AND q.status = 'READY'
                  AND (q.origin = 'PYQ' OR EXISTS (SELECT 1 FROM attempt a WHERE a.question_id = q.id AND a.user_id = :uid))""")
            .param("id", questionId).param("uid", userId)
            .query(Rows.of(KeyRow.class)).optional()
            .map(k -> {
                List<OptionRow> opts = options(List.of(questionId)).getOrDefault(questionId, List.of());
                Set<String> valid = opts.stream().map(OptionRow::label).collect(Collectors.toSet());
                Set<String> right = opts.stream().filter(OptionRow::correct).map(OptionRow::label).collect(Collectors.toSet());
                return new AnswerKey(AnswerType.valueOf(k.answerType()), valid, right, k.natMin(), k.natMax());
            });
    }

    @Override
    public Map<Integer, List<OptionRow>> options(Collection<Integer> ids) {
        if (ids.isEmpty()) return Map.of();
        return jdbc.sql("""
                SELECT question_id, label, body_md, is_correct AS correct FROM question_option
                WHERE question_id IN (:ids) ORDER BY label""")
            .param("ids", ids).query(Rows.of(OptionRow.class)).list().stream()
            .collect(Collectors.groupingBy(OptionRow::questionId, LinkedHashMap::new, Collectors.toList()));
    }

    @Override
    public Map<Integer, List<OptionRow>> mcqOptions(Collection<Integer> ids) {
        if (ids.isEmpty()) return Map.of();
        return jdbc.sql("""
                SELECT question_id, label, body_md, is_correct AS correct FROM question_mcq_option
                WHERE question_id IN (:ids) ORDER BY label""")
            .param("ids", ids).query(Rows.of(OptionRow.class)).list().stream()
            .collect(Collectors.groupingBy(OptionRow::questionId, LinkedHashMap::new, Collectors.toList()));
    }

    @Override
    public Map<Integer, TopicLink> topicLinks(Collection<Integer> ids) {
        record Row(int questionId, String subjectSlug, String chapterSlug, String chapterName, String topicSlug,
                   String topicName, boolean hasNotes) {}
        Map<Integer, TopicLink> out = new LinkedHashMap<>();
        if (ids.isEmpty()) return out;
        jdbc.sql("""
                SELECT q.id AS question_id, s.slug AS subject_slug, c.slug AS chapter_slug, c.name AS chapter_name,
                       t.slug AS topic_slug, t.name AS topic_name,
                       EXISTS (SELECT 1 FROM concept_section cs WHERE cs.topic_id = t.id AND cs.status = 'PUBLISHED') AS has_notes
                FROM question q JOIN topic t ON t.id = q.topic_id JOIN subject s ON s.id = q.subject_id
                LEFT JOIN chapter c ON c.id = t.chapter_id WHERE q.id IN (:ids)""")
            .param("ids", ids).query(Rows.of(Row.class)).list()
            .forEach(r -> out.put(r.questionId(), new TopicLink(r.subjectSlug(), r.chapterSlug(), r.chapterName(),
                r.topicSlug(), r.topicName(), r.hasNotes() && r.chapterSlug() != null)));
        return out;
    }

    @Override
    public Map<Integer, List<SourceView>> sources(Collection<Integer> ids) {
        if (ids.isEmpty()) return Map.of();
        return jdbc.sql("""
                SELECT qs.question_id, s.code, s.kind, s.exam, s.year, s.set_label, qs.q_no,
                       qs.marks, qs.is_primary AS "primary", qs.source_url AS url
                FROM question_source qs JOIN source s ON s.id = qs.source_id
                WHERE qs.question_id IN (:ids) ORDER BY qs.is_primary DESC, s.year, s.code""")
            .param("ids", ids).query(Rows.of(SourceRow.class)).list().stream()
            .collect(Collectors.groupingBy(SourceRow::questionId, LinkedHashMap::new,
                Collectors.mapping(SourceRow::view, Collectors.toList())));
    }

    @Override
    public Map<String, String> figuresOfQuestions(Collection<Integer> ids) {
        Map<String, String> out = new LinkedHashMap<>();
        if (ids.isEmpty()) return out;
        jdbc.sql("SELECT storage_key, svg FROM figure WHERE svg IS NOT NULL AND question_id IN (:ids)")
            .param("ids", ids).query(rs -> { out.put(rs.getString(1), rs.getString(2)); });
        return out;
    }

    @Override
    public AnswerReview review(int questionId) {
        record ReviewRow(String solutionMd, BigDecimal natMin, BigDecimal natMax, String answerStatus) {}
        ReviewRow r = jdbc.sql("SELECT solution_md, nat_min, nat_max, answer_status FROM question WHERE id = :id")
            .param("id", questionId).query(Rows.of(ReviewRow.class)).single();
        List<String> right = jdbc.sql("SELECT label FROM question_option WHERE question_id = :id AND is_correct ORDER BY label")
            .param("id", questionId).query(String.class).list();
        return new AnswerReview(r.solutionMd(), r.natMin(), r.natMax(), r.answerStatus(), right);
    }
}
