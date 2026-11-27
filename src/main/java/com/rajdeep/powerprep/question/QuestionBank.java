package com.rajdeep.powerprep.question;

import com.rajdeep.powerprep.question.grading.AnswerKey;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Read access to questions shared by Study, Tests and the mistake log. */
public interface QuestionBank {

    /** Answer key of a ready question the user may attempt: an exam question, or one of ours they have already seen. */
    Optional<AnswerKey> answerKeyForAttempt(int questionId, long userId);

    /** Options of the given questions, by question id, in label order. */
    Map<Integer, List<OptionRow>> options(Collection<Integer> questionIds);

    /** Single-answer MCQ versions of MSQ questions ("1 and 3 only"), by question id, in label order. */
    Map<Integer, List<OptionRow>> mcqOptions(Collection<Integer> questionIds);

    /** Where each question is taught (subject, chapter, topic), by question id. */
    Map<Integer, TopicLink> topicLinks(Collection<Integer> questionIds);

    /** Sources of the given questions, by question id, primary first. */
    Map<Integer, List<SourceView>> sources(Collection<Integer> questionIds);

    /** SVG figures of the given questions, by storage key. */
    Map<String, String> figuresOfQuestions(Collection<Integer> questionIds);

    AnswerReview review(int questionId);

    static List<String> correctLabels(List<OptionRow> options) {
        return options.stream().filter(OptionRow::correct).map(OptionRow::label).toList();
    }
}
