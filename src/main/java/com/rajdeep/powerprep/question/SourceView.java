package com.rajdeep.powerprep.question;

import java.math.BigDecimal;

/** Where a question was asked: paper code, exam, year, question number. */
public record SourceView(String code, String kind, String exam, Integer year, String setLabel, String qNo,
                         BigDecimal marks, boolean primary, String url) {}
