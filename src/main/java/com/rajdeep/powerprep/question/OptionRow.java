package com.rajdeep.powerprep.question;

/** One option of a question, with the answer flag (never send this to a user who has not attempted). */
public record OptionRow(int questionId, String label, String bodyMd, boolean correct) {}
