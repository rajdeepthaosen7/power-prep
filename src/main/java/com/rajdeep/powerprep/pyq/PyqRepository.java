package com.rajdeep.powerprep.pyq;

import com.rajdeep.powerprep.mocktest.TestList;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface PyqRepository {
    /** One row per (question, source) of the exam: the question's paper number, marks, and whether it is written. */
    record SourceRow(String sourceCode, String qNo, BigDecimal marks, boolean ready, Integer year) {}
    List<SourceRow> sourceRows(String exam);

    record CountRow(String subjectSlug, String subjectName, int subjectSort, String chapterSlug, String chapterName,
                    Integer chapterSort, int ready, int total, Integer firstYear, Integer lastYear) {}
    /** Counts by subject and chapter (chapter null = not placed in a chapter yet). */
    List<CountRow> countsByChapter(String exam);

    /** Written questions of the selection, with the source they are picked by. */
    record Picked(int questionId, String sourceCode, String qNo, BigDecimal marks, int year) {}
    List<Picked> pick(PyqSelection selection, PaperRef paper);

    Optional<String> subjectName(String slug);
    Optional<String> chapterName(String subjectSlug, String chapterSlug);

    Optional<Integer> testIdByCode(String code);
    int createSetTest(String code, String title, String description, long ownerId, List<Picked> questions);

    /** The user's attempts at PYQ sets, with the code of their test (the code carries the selection). */
    record OwnAttempt(String code, String title, TestList.Attempt attempt) {}
    List<OwnAttempt> ownAttempts(long userId);
}
