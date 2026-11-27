package com.rajdeep.powerprep.mocktest;

import com.rajdeep.powerprep.question.SourceView;
import com.rajdeep.powerprep.question.TopicLink;
import java.math.BigDecimal;
import java.util.List;

/**
 * One answered question in PREPARE mode: the user's answer and self-mark, the right answer as it was asked
 * (for a NAT asked as MCQ, the right option), the solution, its sources and where it is taught.
 */
public record PreparedAnswer(int questionId, String chosen, BigDecimal natValue, boolean correct, String selfMark,
                             List<String> correctLabels, BigDecimal natMin, BigDecimal natMax, String solutionMd,
                             String answerStatus, List<SourceView> sources, TopicLink topic) {}
