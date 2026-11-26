package com.rajdeep.powerprep.catalog;

public record SubjectView(int id, String name, String slug, boolean inSyllabus, long topics, long questions) {}
