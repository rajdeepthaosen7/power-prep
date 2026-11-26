package com.rajdeep.powerprep.catalog;

import java.math.BigDecimal;

/** A question in a topic list: its first source, and all its sources as one line ("GATE 2010 Q5, APGCL 2023 Q36"). */
public record TopicQuestion(int id, String source, String qNo, BigDecimal marks, String type, String summary,
                            String body, String status, String sources) {}
