package com.rajdeep.powerprep.study;

/** A topic in the chapter list, with note and question counts ({@code attempted} = by this user). */
public record TopicSummary(int id, String slug, String name, int chapterId, int sortOrder,
                           long notes, long questions, long ready, long attempted) {}
