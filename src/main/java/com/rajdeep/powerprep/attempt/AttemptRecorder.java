package com.rajdeep.powerprep.attempt;

import org.springframework.stereotype.Service;

/** The one place that saves an answer: the attempt row plus the per-question summary. */
@Service
public class AttemptRecorder {
    private final AttemptRepository attempts;

    public AttemptRecorder(AttemptRepository attempts) { this.attempts = attempts; }

    /** Call inside the caller's transaction. Returns the attempt id. */
    public long record(NewAttempt attempt) {
        long id = attempts.insert(attempt);
        attempts.updateQuestionState(attempt.userId(), attempt.questionId(), attempt.correct());
        return id;
    }
}
