package com.rajdeep.powerprep.question.grading;

import com.rajdeep.powerprep.common.error.BadRequestException;

/** Shared checks for option questions: something chosen, and only labels the question has. */
abstract class OptionGrader implements AnswerGrader {

    @Override
    public final boolean isCorrect(AnswerKey key, GivenAnswer answer) {
        if (!answer.hasOptions()) throw new BadRequestException("choose an option");
        if (!key.validLabels().containsAll(answer.chosen())) throw new BadRequestException("unknown option");
        return rule(key, answer);
    }

    protected abstract boolean rule(AnswerKey key, GivenAnswer answer);
}
