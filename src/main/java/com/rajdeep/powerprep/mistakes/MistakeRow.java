package com.rajdeep.powerprep.mistakes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * A question the user got wrong at least once (or marked "guessed" / "didn't know"), with its latest attempt.
 * lastCorrect = true means the latest answer is right. {@code retest}: still on the retest list ({@link RetestRule}:
 * right and sure on two different days clears it). {@code lastSelfMark}: the user's mark on the latest answer.
 */
public record MistakeRow(int id, String stemMd, String answerType, String origin, Integer difficulty, String solutionMd,
                         String answerStatus, BigDecimal natMin, BigDecimal natMax, String topic, String chapterSlug,
                         String chapterName, Integer chapterSort, String subjectSlug, String subjectName,
                         Boolean lastCorrect, String lastChosen, BigDecimal lastNat, String lastMode, Instant lastAt,
                         long wrong, Instant lastWrongAt, String reason, String lastSelfMark, long sureDays) {

    @JsonProperty("retest")
    public boolean retest() { return RetestRule.isOpen(true, sureDays); }

    /** On the retest list: the same as {@link #retest()}. */
    @JsonIgnore
    public boolean isOpen() { return retest(); }
}
