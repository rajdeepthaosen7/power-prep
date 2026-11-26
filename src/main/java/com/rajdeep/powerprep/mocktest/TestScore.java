package com.rajdeep.powerprep.mocktest;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** APSC-style marking: +marksCorrect for a right answer, -marksWrong for a wrong one, 0 for a skipped one. */
public record TestScore(int correct, int wrong, int skipped, BigDecimal score, BigDecimal maxScore) {

    public static TestScore of(BigDecimal marksCorrect, BigDecimal marksWrong, int correct, int wrong, int skipped) {
        BigDecimal score = marksCorrect.multiply(BigDecimal.valueOf(correct))
            .subtract(marksWrong.multiply(BigDecimal.valueOf(wrong)));
        BigDecimal max = marksCorrect.multiply(BigDecimal.valueOf((long) correct + wrong + skipped));
        return new TestScore(correct, wrong, skipped, score, max);
    }

    /** Right answers as a whole percent of attempted ones; null when nothing was attempted. */
    public static BigDecimal accuracyPct(int correct, int wrong) {
        int attempted = correct + wrong;
        return attempted == 0 ? null
            : BigDecimal.valueOf(100L * correct).divide(BigDecimal.valueOf(attempted), 0, RoundingMode.HALF_UP);
    }
}
