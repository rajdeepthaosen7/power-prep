package com.rajdeep.powerprep.study;

import com.rajdeep.powerprep.attempt.AttemptMode;
import com.rajdeep.powerprep.attempt.AttemptRecorder;
import com.rajdeep.powerprep.attempt.NewAttempt;
import com.rajdeep.powerprep.attempt.SelfMark;
import com.rajdeep.powerprep.common.error.NotFoundException;
import com.rajdeep.powerprep.question.QuestionBank;
import com.rajdeep.powerprep.question.grading.AnswerGrading;
import com.rajdeep.powerprep.question.grading.AnswerKey;
import com.rajdeep.powerprep.question.grading.GivenAnswer;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Grades and saves a single-question attempt (Study, PYQ, mistake re-solve), then reveals the analysis. */
@Service
public class QuestionAttemptService {
    private final QuestionBank bank;
    private final AnswerGrading grading;
    private final AttemptRecorder recorder;

    public QuestionAttemptService(QuestionBank bank, AnswerGrading grading, AttemptRecorder recorder) {
        this.bank = bank;
        this.grading = grading;
        this.recorder = recorder;
    }

    @Transactional
    public AttemptResult attempt(long userId, int questionId, List<String> chosen, BigDecimal natValue,
                                 Integer timeMs, AttemptMode mode, SelfMark selfMark) {
        AnswerKey key = bank.answerKeyForAttempt(questionId, userId)
            .orElseThrow(() -> new NotFoundException("question not found"));
        GivenAnswer answer = GivenAnswer.of(chosen, natValue);
        boolean correct = grading.isCorrect(key, answer);
        String labels = switch (key.type()) {
            case NAT -> null;
            case MCQ, MSQ -> answer.chosenLabels();
        };
        long attemptId = recorder.record(new NewAttempt(userId, questionId, labels, natValue, correct, timeMs, mode, null, selfMark));
        return new AttemptResult(attemptId, correct, bank.review(questionId));
    }
}
