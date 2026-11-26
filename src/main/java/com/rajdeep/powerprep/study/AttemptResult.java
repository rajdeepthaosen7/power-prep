package com.rajdeep.powerprep.study;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.rajdeep.powerprep.question.AnswerReview;

/** Result of one attempt, with the answer and analysis. */
public record AttemptResult(long attemptId, boolean correct, @JsonUnwrapped AnswerReview review) {}
