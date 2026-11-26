package com.rajdeep.powerprep.study;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.rajdeep.powerprep.attempt.LastAttempt;
import com.rajdeep.powerprep.question.AnswerReview;
import com.rajdeep.powerprep.question.OptionView;
import com.rajdeep.powerprep.question.SourceView;
import java.util.List;

/** A question on a chapter page. {@code attempt} and {@code review} are null until the user has attempted it. */
public record StudyQuestion(@JsonUnwrapped QuestionRow question, List<SourceView> sources, List<OptionView> options,
                            LastAttempt attempt, AnswerReview review) {}
