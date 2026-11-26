package com.rajdeep.powerprep.question.grading;

import com.rajdeep.powerprep.common.error.BadRequestException;
import org.springframework.stereotype.Component;

/** A typed number; right if it lies in the answer range [natMin, natMax]. */
@Component
class NatGrader implements AnswerGrader {
    @Override public AnswerType type() { return AnswerType.NAT; }

    @Override
    public boolean isCorrect(AnswerKey key, GivenAnswer answer) {
        if (answer.natValue() == null) throw new BadRequestException("natValue is required");
        return key.natMin() != null && key.natMax() != null
                && answer.natValue().compareTo(key.natMin()) >= 0
                && answer.natValue().compareTo(key.natMax()) <= 0;
    }
}
