package com.rajdeep.powerprep.mocktest;

import com.rajdeep.powerprep.question.OptionView;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * A running test: questions without answers. {@code serverNow} lets the browser correct its clock for the timer.
 * {@code mode} TEST or PREPARE, {@code style} APSC or GATE. Each question says how it is asked ({@code askedAs}:
 * MCQ, MSQ or NAT) and its GATE marks. In PREPARE mode, {@code answered} holds the questions already answered,
 * with their solutions, so a resumed attempt shows them again.
 */
public record StartedTest(long attemptId, String startedAt, String serverNow, TestMeta test, String mode, String style,
                          int timeLimitMin, List<Question> questions, Map<String, String> figures,
                          List<PreparedAnswer> answered) {
    public record Question(int id, int position, String stemMd, List<OptionView> options, String askedAs, BigDecimal marks) {}

    record QuestionRow(int id, int position, String stemMd, String answerType, BigDecimal natMin, BigDecimal natMax,
                       BigDecimal marks) {}
}
