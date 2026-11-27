package com.rajdeep.powerprep.mocktest;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.rajdeep.powerprep.question.OptionView;
import com.rajdeep.powerprep.question.SourceView;
import com.rajdeep.powerprep.question.TopicLink;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

/** A submitted test: score, topic-wise split, every question with its analysis, and earlier scores. */
public record TestResult(long attemptId, TestMeta test, String mode, String style, Instant startedAt, Instant submittedAt, Long timeMs,
                         BigDecimal score, BigDecimal maxScore, Integer nCorrect, Integer nWrong, Integer nSkipped,
                         BigDecimal accuracy, List<TopicScore> topics, List<Question> questions,
                         Map<String, String> figures, List<HistoryEntry> history) {

    public record QuestionRow(int id, int position, String stemMd, String solutionMd, String answerStatus, String origin,
                              Integer difficulty, String topic, String topicSlug, int topicSort, String chosen,
                              Boolean correct, Integer timeMs, String answerType, BigDecimal natMin, BigDecimal natMax,
                              BigDecimal natValue, String selfMark, BigDecimal marks) {}

    /** {@code askedAs}: how the question was asked in this attempt (APSC style turns NAT and MSQ into MCQ). */
    public record Question(@JsonUnwrapped QuestionRow question, String askedAs, List<OptionView> options,
                           List<String> correctLabels, List<SourceView> sources, TopicLink topicLink) {}

    public record TopicScore(String slug, String name, int sort, int total, int correct, int wrong, int skipped) {}

    public record HistoryEntry(long id, Instant submittedAt, BigDecimal score, BigDecimal maxScore) {}
}
