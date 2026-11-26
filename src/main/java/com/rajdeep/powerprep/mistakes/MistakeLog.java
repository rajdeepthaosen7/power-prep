package com.rajdeep.powerprep.mistakes;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.rajdeep.powerprep.question.OptionView;
import com.rajdeep.powerprep.question.SourceView;
import java.util.List;
import java.util.Map;

/** The mistake log: open ones first. {@code byReason} and {@code untagged} count open mistakes only. */
public record MistakeLog(List<Question> questions, Map<String, Long> byReason, long untagged, Map<String, String> figures) {

    public record Question(@JsonUnwrapped MistakeRow mistake, List<OptionView> options, List<String> correctLabels,
                           List<SourceView> sources) {}
}
