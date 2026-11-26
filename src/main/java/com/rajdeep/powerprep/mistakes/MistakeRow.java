package com.rajdeep.powerprep.mistakes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.time.Instant;

/** A question the user got wrong at least once, with its latest attempt. lastCorrect = true means it is fixed now. */
public record MistakeRow(int id, String stemMd, String answerType, String origin, Integer difficulty, String solutionMd,
                         String answerStatus, BigDecimal natMin, BigDecimal natMax, String topic, String chapterSlug,
                         String chapterName, Integer chapterSort, String subjectSlug, String subjectName,
                         Boolean lastCorrect, String lastChosen, BigDecimal lastNat, String lastMode, Instant lastAt,
                         long wrong, Instant lastWrongAt, String reason) {

    @JsonIgnore
    public boolean isOpen() { return !Boolean.TRUE.equals(lastCorrect); }
}
