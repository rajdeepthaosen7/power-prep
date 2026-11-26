package com.rajdeep.powerprep.attempt;

import java.util.Collection;
import java.util.Map;

public interface AttemptRepository {
    /** Saves the attempt and returns its id. */
    long insert(NewAttempt attempt);

    /** Updates the per-question summary (times seen, times right, last result). */
    void updateQuestionState(long userId, int questionId, boolean correct);

    /** Latest attempt per question for this user. */
    Map<Integer, LastAttempt> latest(long userId, Collection<Integer> questionIds);
}
