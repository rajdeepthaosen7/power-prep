package com.rajdeep.powerprep.mistakes;

import java.util.List;

public interface MistakeRepository {
    /** Every question the user answered wrong at least once: open ones first, then the latest wrong first. */
    List<MistakeRow> log(long userId);
    long wrongAttempts(long userId, int questionId);
    void saveReason(long userId, int questionId, MistakeReason reason);
    void clearReason(long userId, int questionId);
}
