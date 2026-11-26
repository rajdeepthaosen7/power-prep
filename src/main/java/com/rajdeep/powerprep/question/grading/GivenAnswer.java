package com.rajdeep.powerprep.question.grading;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

/** The user's answer, normalised: option labels trimmed, upper-case, sorted, blanks removed. */
public record GivenAnswer(SortedSet<String> chosen, BigDecimal natValue) {

    public static GivenAnswer of(Collection<String> labels, BigDecimal natValue) {
        SortedSet<String> set = new TreeSet<>();
        if (labels != null) {
            for (String l : labels) {
                if (l != null && !l.isBlank()) set.add(l.trim().toUpperCase());
            }
        }
        return new GivenAnswer(set, natValue);
    }

    public boolean hasOptions() { return !chosen.isEmpty(); }

    /** Labels as stored in the attempt table: "A" or "A,C". */
    public String chosenLabels() { return chosen.isEmpty() ? null : String.join(",", chosen); }
}
