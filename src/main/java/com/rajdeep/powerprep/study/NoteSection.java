package com.rajdeep.powerprep.study;

/** One published section of a topic's notes (markdown). */
public record NoteSection(int id, int topicId, String slug, String title, String bodyMd) {}
