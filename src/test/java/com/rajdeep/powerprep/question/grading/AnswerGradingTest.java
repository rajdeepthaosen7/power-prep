package com.rajdeep.powerprep.question.grading;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.rajdeep.powerprep.common.error.BadRequestException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AnswerGradingTest {
    private final AnswerGrading grading = new AnswerGrading(List.of(new McqGrader(), new MsqGrader(), new NatGrader()));
    private static final Set<String> ABCD = Set.of("A", "B", "C", "D");

    private static AnswerKey options(AnswerType type, String... right) {
        return new AnswerKey(type, ABCD, Set.of(right), null, null);
    }

    private static GivenAnswer pick(String... labels) { return GivenAnswer.of(List.of(labels), null); }

    @Test
    void mcqIsRightOnlyForOneCorrectPick() {
        assertThat(grading.isCorrect(options(AnswerType.MCQ, "B"), pick("b"))).isTrue();
        assertThat(grading.isCorrect(options(AnswerType.MCQ, "B"), pick("A"))).isFalse();
        assertThat(grading.isCorrect(options(AnswerType.MCQ, "B"), pick("B", "C"))).isFalse();
    }

    @Test
    void mcqWithTwoCorrectOptionsAcceptsEither() {
        assertThat(grading.isCorrect(options(AnswerType.MCQ, "A", "C"), pick("C"))).isTrue();
    }

    @Test
    void msqNeedsTheExactSet() {
        assertThat(grading.isCorrect(options(AnswerType.MSQ, "A", "C"), pick("C", " a "))).isTrue();
        assertThat(grading.isCorrect(options(AnswerType.MSQ, "A", "C"), pick("A"))).isFalse();
    }

    @Test
    void optionQuestionsRejectEmptyAndUnknownPicks() {
        assertThatThrownBy(() -> grading.isCorrect(options(AnswerType.MCQ, "A"), pick()))
            .isInstanceOf(BadRequestException.class).hasMessage("choose an option");
        assertThatThrownBy(() -> grading.isCorrect(options(AnswerType.MCQ, "A"), pick("E")))
            .isInstanceOf(BadRequestException.class).hasMessage("unknown option");
    }

    @Test
    void natIsRightInsideTheRangeIncludingEnds() {
        AnswerKey key = new AnswerKey(AnswerType.NAT, Set.of(), Set.of(), new BigDecimal("1.48"), new BigDecimal("1.52"));
        assertThat(grading.isCorrect(key, GivenAnswer.of(null, new BigDecimal("1.48")))).isTrue();
        assertThat(grading.isCorrect(key, GivenAnswer.of(null, new BigDecimal("1.5")))).isTrue();
        assertThat(grading.isCorrect(key, GivenAnswer.of(null, new BigDecimal("1.53")))).isFalse();
        assertThatThrownBy(() -> grading.isCorrect(key, GivenAnswer.of(null, null))).isInstanceOf(BadRequestException.class);
    }

    @Test
    void givenAnswerIsNormalised() {
        GivenAnswer a = GivenAnswer.of(List.of(" c", "A", "", "a"), null);
        assertThat(a.chosenLabels()).isEqualTo("A,C");
        assertThat(GivenAnswer.of(List.of("  "), null).hasOptions()).isFalse();
    }
}
