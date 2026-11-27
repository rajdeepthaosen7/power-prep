package com.rajdeep.powerprep.mocktest;

import static org.assertj.core.api.Assertions.assertThat;

import com.rajdeep.powerprep.mocktest.ScoredAnswer.Outcome;
import com.rajdeep.powerprep.question.grading.AnswerType;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class TestScoreTest {
    @Test
    void apscMarkingIsPlusOneMinusQuarter() {
        TestScore s = TestScore.of(BigDecimal.ONE, new BigDecimal("0.25"), 30, 8, 12);
        assertThat(s.score()).isEqualByComparingTo("28");          // 30 - 8 x 0.25
        assertThat(s.maxScore()).isEqualByComparingTo("50");
    }

    @Test
    void accuracyIsRoundedPercentOfAttempted() {
        assertThat(TestScore.accuracyPct(2, 1)).isEqualByComparingTo("67");
        assertThat(TestScore.accuracyPct(0, 0)).isNull();
    }

    @Test
    void gateMarkingUsesEachQuestionsMarks() {
        BigDecimal one = BigDecimal.ONE, two = BigDecimal.valueOf(2);
        TestScore s = TestScore.gate(List.of(
            new ScoredAnswer(AnswerType.MCQ, two, Outcome.CORRECT),    // +2
            new ScoredAnswer(AnswerType.MCQ, one, Outcome.WRONG),      // -1/3
            new ScoredAnswer(AnswerType.MCQ, two, Outcome.WRONG),      // -2/3
            new ScoredAnswer(AnswerType.NAT, two, Outcome.WRONG),      // no negative for NAT
            new ScoredAnswer(AnswerType.MSQ, one, Outcome.WRONG),      // no negative for MSQ
            new ScoredAnswer(AnswerType.NAT, one, Outcome.SKIPPED)));
        assertThat(s.score()).isEqualByComparingTo("1.000");       // 2 - 0.333 - 0.667
        assertThat(s.maxScore()).isEqualByComparingTo("9");
        assertThat(s.correct()).isEqualTo(1);
        assertThat(s.wrong()).isEqualTo(4);
        assertThat(s.skipped()).isEqualTo(1);
    }
}
