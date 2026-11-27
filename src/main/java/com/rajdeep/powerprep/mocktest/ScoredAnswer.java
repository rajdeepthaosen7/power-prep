package com.rajdeep.powerprep.mocktest;

import com.rajdeep.powerprep.question.grading.AnswerType;
import java.math.BigDecimal;

/** One question of a submitted test, ready to be marked: how it was asked, its marks, and how it went. */
public record ScoredAnswer(AnswerType askedAs, BigDecimal marks, Outcome outcome) {
    public enum Outcome { CORRECT, WRONG, SKIPPED }
}
