package com.rajdeep.powerprep.mocktest;

import static org.assertj.core.api.Assertions.assertThat;

import com.rajdeep.powerprep.question.OptionRow;
import com.rajdeep.powerprep.question.grading.AnswerType;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class AskedQuestionTest {
    private static List<OptionRow> opts(String... rightLabels) {
        List<String> right = List.of(rightLabels);
        return List.of("A", "B", "C", "D").stream().map(l -> new OptionRow(1, l, "opt " + l, right.contains(l))).toList();
    }

    @Test
    void apscAsksNatAsMcqWhenItHasTestOptions() {
        AskedQuestion q = AskedQuestion.of(TestStyle.APSC, 1, AnswerType.NAT, opts("C"), List.of(), BigDecimal.ONE, BigDecimal.TEN);
        assertThat(q.askedAs()).isEqualTo(AnswerType.MCQ);
        assertThat(q.key().correctLabels()).containsExactly("C");
    }

    @Test
    void apscFallsBackToTypedNatWithoutOptions() {
        AskedQuestion q = AskedQuestion.of(TestStyle.APSC, 1, AnswerType.NAT, List.of(), List.of(), BigDecimal.ONE, BigDecimal.TEN);
        assertThat(q.askedAs()).isEqualTo(AnswerType.NAT);
        assertThat(q.options()).isEmpty();
        assertThat(q.key().natMax()).isEqualByComparingTo("10");
    }

    @Test
    void apscAsksMsqWithCombinationOptions() {
        AskedQuestion q = AskedQuestion.of(TestStyle.APSC, 1, AnswerType.MSQ, opts("A", "C"), opts("B"), null, null);
        assertThat(q.askedAs()).isEqualTo(AnswerType.MCQ);
        assertThat(q.key().correctLabels()).containsExactly("B");
    }

    @Test
    void apscKeepsMsqWhenNoCombinationOptions() {
        AskedQuestion q = AskedQuestion.of(TestStyle.APSC, 1, AnswerType.MSQ, opts("A", "C"), List.of(), null, null);
        assertThat(q.askedAs()).isEqualTo(AnswerType.MSQ);
        assertThat(q.key().correctLabels()).containsExactlyInAnyOrder("A", "C");
    }

    @Test
    void gateKeepsTheOriginalFormat() {
        assertThat(AskedQuestion.of(TestStyle.GATE, 1, AnswerType.NAT, opts("C"), List.of(), BigDecimal.ONE, BigDecimal.TEN).askedAs())
            .isEqualTo(AnswerType.NAT);
        AskedQuestion msq = AskedQuestion.of(TestStyle.GATE, 1, AnswerType.MSQ, opts("A", "C"), opts("B"), null, null);
        assertThat(msq.askedAs()).isEqualTo(AnswerType.MSQ);
        assertThat(msq.key().correctLabels()).containsExactlyInAnyOrder("A", "C");
    }
}
