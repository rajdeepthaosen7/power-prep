package com.rajdeep.powerprep.question.grading;

import org.springframework.stereotype.Component;

/** One pick; right if it is a correct option (a defective question can have two correct options). */
@Component
class McqGrader extends OptionGrader {
    @Override public AnswerType type() { return AnswerType.MCQ; }

    @Override
    protected boolean rule(AnswerKey key, GivenAnswer answer) {
        return answer.chosen().size() == 1 && key.correctLabels().containsAll(answer.chosen());
    }
}
