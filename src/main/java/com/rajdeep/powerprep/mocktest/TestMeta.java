package com.rajdeep.powerprep.mocktest;

import java.math.BigDecimal;

/** A published test: marking scheme, time, and where it belongs. kind CHAPTER = chapter mock, PYQ_SET = the user's own PYQ selection. */
public record TestMeta(int id, String code, String title, String kind, String variant, String description, Integer durationMin,
                       BigDecimal marksCorrect, BigDecimal marksWrong, String chapterSlug, String chapterName,
                       String subjectSlug, String subjectName, long questions) {}
