package com.rajdeep.powerprep.question;

import java.math.BigDecimal;
import java.util.List;

/** The answer and analysis of a question; only for users who have attempted it. */
public record AnswerReview(String solutionMd, BigDecimal natMin, BigDecimal natMax, String answerStatus,
                           List<String> correctLabels) {}
