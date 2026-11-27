package com.rajdeep.powerprep.mocktest;

import com.rajdeep.powerprep.question.grading.AnswerType;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Marking of a test. APSC style ({@link #of}): +marksCorrect for a right answer, -marksWrong for a wrong one, 0 for a skipped one.
 * GATE style ({@link #gate}): each question's own marks; a wrong MCQ loses one third of its marks; a wrong MSQ or NAT loses nothing.
 */
public record TestScore(int correct, int wrong, int skipped, BigDecimal score, BigDecimal maxScore) {
    private static final BigDecimal THREE = BigDecimal.valueOf(3);

    public static TestScore of(BigDecimal marksCorrect, BigDecimal marksWrong, int correct, int wrong, int skipped) {
        BigDecimal score = marksCorrect.multiply(BigDecimal.valueOf(correct))
            .subtract(marksWrong.multiply(BigDecimal.valueOf(wrong)));
        BigDecimal max = marksCorrect.multiply(BigDecimal.valueOf((long) correct + wrong + skipped));
        return new TestScore(correct, wrong, skipped, score, max);
    }

    public static TestScore gate(List<ScoredAnswer> answers) {
        int correct = 0, wrong = 0, skipped = 0;
        BigDecimal score = BigDecimal.ZERO, max = BigDecimal.ZERO;
        for (ScoredAnswer a : answers) {
            BigDecimal marks = a.marks() == null ? BigDecimal.ONE : a.marks();
            max = max.add(marks);
            switch (a.outcome()) {
                case CORRECT -> { correct++; score = score.add(marks); }
                case WRONG -> {
                    wrong++;
                    if (a.askedAs() == AnswerType.MCQ)
                        score = score.subtract(marks.divide(THREE, 3, RoundingMode.HALF_UP));
                }
                case SKIPPED -> skipped++;
            }
        }
        return new TestScore(correct, wrong, skipped, score, max);
    }

    /** Right answers as a whole percent of attempted ones; null when nothing was attempted. */
    public static BigDecimal accuracyPct(int correct, int wrong) {
        int attempted = correct + wrong;
        return attempted == 0 ? null
            : BigDecimal.valueOf(100L * correct).divide(BigDecimal.valueOf(attempted), 0, RoundingMode.HALF_UP);
    }
}
