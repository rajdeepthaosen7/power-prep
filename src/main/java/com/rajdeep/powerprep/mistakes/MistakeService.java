package com.rajdeep.powerprep.mistakes;

import com.rajdeep.powerprep.common.error.NotFoundException;
import com.rajdeep.powerprep.question.OptionRow;
import com.rajdeep.powerprep.question.OptionView;
import com.rajdeep.powerprep.question.QuestionBank;
import com.rajdeep.powerprep.question.SourceView;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Mistake log: every question the user got wrong (Study or Tests), with a one-tap reason.
 * A mistake stays open until the user's latest attempt at it is right.
 */
@Service
public class MistakeService {
    private final MistakeRepository mistakes;
    private final QuestionBank bank;

    public MistakeService(MistakeRepository mistakes, QuestionBank bank) {
        this.mistakes = mistakes;
        this.bank = bank;
    }

    public MistakeLog log(long userId) {
        List<MistakeRow> rows = mistakes.log(userId);
        List<Integer> ids = rows.stream().map(MistakeRow::id).toList();
        Map<Integer, List<OptionRow>> options = bank.options(ids);
        Map<Integer, List<SourceView>> sources = bank.sources(ids);
        List<MistakeLog.Question> questions = rows.stream().map(r -> {
            List<OptionRow> opts = options.getOrDefault(r.id(), List.of());
            return new MistakeLog.Question(r, opts.stream().map(OptionView::of).toList(),
                QuestionBank.correctLabels(opts), sources.getOrDefault(r.id(), List.of()));
        }).toList();

        Map<String, Long> byReason = new LinkedHashMap<>();
        for (MistakeReason k : MistakeReason.values()) byReason.put(k.name(), 0L);
        long untagged = 0;
        for (MistakeRow r : rows) {
            if (!r.isOpen()) continue;
            if (r.reason() == null) untagged++; else byReason.merge(r.reason(), 1L, Long::sum);
        }
        return new MistakeLog(questions, byReason, untagged, bank.figuresOfQuestions(ids));
    }

    public record ReasonSaved(int questionId, String reason) {}

    @Transactional
    public ReasonSaved setReason(long userId, int questionId, MistakeReason reason) {
        if (mistakes.wrongAttempts(userId, questionId) == 0) throw new NotFoundException("not in your mistake log");
        if (reason == null) mistakes.clearReason(userId, questionId);
        else mistakes.saveReason(userId, questionId, reason);
        return new ReasonSaved(questionId, reason == null ? "" : reason.name());
    }
}
