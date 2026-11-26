package com.rajdeep.powerprep.question.grading;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/** Grades an answer with the grader registered for the question's answer type. */
@Service
public class AnswerGrading {
    private final Map<AnswerType, AnswerGrader> graders = new EnumMap<>(AnswerType.class);

    public AnswerGrading(List<AnswerGrader> graders) {
        graders.forEach(g -> this.graders.put(g.type(), g));
    }

    public boolean isCorrect(AnswerKey key, GivenAnswer answer) {
        AnswerGrader g = graders.get(key.type());
        if (g == null) throw new IllegalStateException("no grader for " + key.type());
        return g.isCorrect(key, answer);
    }
}
