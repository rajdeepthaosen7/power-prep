package com.rajdeep.powerprep.today;

/** A chapter that has notes, with the user's progress on its exam questions. */
public record TrackChapter(String subjectSlug, String subjectName, int id, String slug, String name, int sortOrder,
                           long ready, long attempted, boolean studied) {}
