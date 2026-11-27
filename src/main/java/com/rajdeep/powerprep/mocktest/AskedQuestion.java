package com.rajdeep.powerprep.mocktest;

import com.rajdeep.powerprep.question.OptionRow;
import com.rajdeep.powerprep.question.grading.AnswerKey;
import com.rajdeep.powerprep.question.grading.AnswerType;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * How one question is asked inside a test: the answer type there, the options shown, and the key to grade it.
 *
 * <p>APSC style (APSC asks MCQ only):
 * <ul><li>MCQ: as it is.</li>
 * <li>NAT: as a single-answer MCQ when it has 4 test options with one right; otherwise the number is typed.</li>
 * <li>MSQ: as a single-answer MCQ with combination options ("1 and 3 only") when we have them; otherwise as MSQ.</li></ul>
 * GATE style: the original type. NAT has no options; MSQ shows its statements and needs every right one.
 */
public record AskedQuestion(int questionId, AnswerType askedAs, List<OptionRow> options, AnswerKey key) {

    public static AskedQuestion of(TestStyle style, int questionId, AnswerType type, List<OptionRow> options,
                                   List<OptionRow> mcqOptions, BigDecimal natMin, BigDecimal natMax) {
        List<OptionRow> opts = options == null ? List.of() : options;
        List<OptionRow> mcq = mcqOptions == null ? List.of() : mcqOptions;
        if (style == TestStyle.APSC) {
            switch (type) {
                case MCQ: return choice(questionId, AnswerType.MCQ, opts);
                case NAT:
                    if (hasOneRight(opts)) return choice(questionId, AnswerType.MCQ, opts);
                    return typed(questionId, natMin, natMax);
                case MSQ:
                    if (hasOneRight(mcq)) return choice(questionId, AnswerType.MCQ, mcq);
                    return choice(questionId, AnswerType.MSQ, opts);
                default: throw new IllegalStateException("unknown type " + type);
            }
        }
        return switch (type) {
            case MCQ -> choice(questionId, AnswerType.MCQ, opts);
            case MSQ -> choice(questionId, AnswerType.MSQ, opts);
            case NAT -> typed(questionId, natMin, natMax);
        };
    }

    private static boolean hasOneRight(List<OptionRow> opts) {
        return opts.size() >= 2 && opts.stream().filter(OptionRow::correct).count() == 1;
    }

    private static AskedQuestion choice(int questionId, AnswerType askedAs, List<OptionRow> opts) {
        Set<String> valid = opts.stream().map(OptionRow::label).collect(Collectors.toSet());
        Set<String> right = opts.stream().filter(OptionRow::correct).map(OptionRow::label).collect(Collectors.toSet());
        return new AskedQuestion(questionId, askedAs, List.copyOf(opts), new AnswerKey(askedAs, valid, right, null, null));
    }

    private static AskedQuestion typed(int questionId, BigDecimal natMin, BigDecimal natMax) {
        return new AskedQuestion(questionId, AnswerType.NAT, List.of(), new AnswerKey(AnswerType.NAT, Set.of(), Set.of(), natMin, natMax));
    }
}
