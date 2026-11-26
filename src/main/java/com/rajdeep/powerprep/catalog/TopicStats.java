package com.rajdeep.powerprep.catalog;

/** A topic with its exam-question counts. */
public record TopicStats(int id, String name, long questions, long stateExams, long gateQuestions) {}
