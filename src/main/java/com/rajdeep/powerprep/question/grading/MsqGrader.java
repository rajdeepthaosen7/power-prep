package com.rajdeep.powerprep.question.grading;

import org.springframework.stereotype.Component;

/** Several picks; right only if the picked set equals the correct set. */
@Component
class MsqGrader extends OptionGrader {
    @Override public AnswerType type() { return AnswerType.MSQ; }

    @Override
    protected boolean rule(AnswerKey key, GivenAnswer answer) {
        return answer.chosen().equals(key.correctLabels());
    }
}
