package com.rajdeep.powerprep.question.grading;

import java.math.BigDecimal;
import java.util.Set;

/** What a grader needs to know about a question: its type, its options and the right answer. */
public record AnswerKey(AnswerType type, Set<String> validLabels, Set<String> correctLabels,
                        BigDecimal natMin, BigDecimal natMax) {
    public AnswerKey {
        validLabels = Set.copyOf(validLabels);
        correctLabels = Set.copyOf(correctLabels);
    }
}
