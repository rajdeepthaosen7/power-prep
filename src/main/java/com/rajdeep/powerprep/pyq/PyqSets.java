package com.rajdeep.powerprep.pyq;

import com.rajdeep.powerprep.mocktest.TestList;
import java.util.List;

/**
 * Subject-wise and chapter-wise PYQ sets of an exam across all years. {@code total}: questions we know of;
 * {@code ready}: those written in the portal. {@code firstYear} / {@code lastYear}: years of the ready ones.
 * {@code attempts}: the user's attempts at sets of this subject (any chapter or years), newest first.
 */
public record PyqSets(String exam, List<Subject> subjects) {
    public record Subject(String slug, String name, int ready, int total, Integer firstYear, Integer lastYear,
                          List<Chapter> chapters, List<SetAttempt> attempts) {}
    public record Chapter(String slug, String name, int ready, int total, Integer firstYear, Integer lastYear) {}
    public record SetAttempt(String title, String chapter, Integer yearFrom, Integer yearTo, TestList.Attempt attempt) {}
}
