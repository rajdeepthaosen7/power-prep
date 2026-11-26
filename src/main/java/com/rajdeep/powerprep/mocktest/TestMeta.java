package com.rajdeep.powerprep.mocktest;

import java.math.BigDecimal;

/** A published mock test: marking scheme, time, and where it belongs. */
public record TestMeta(int id, String code, String title, String variant, String description, Integer durationMin,
                       BigDecimal marksCorrect, BigDecimal marksWrong, String chapterSlug, String chapterName,
                       String subjectSlug, String subjectName, long questions) {}
