package com.rajdeep.powerprep.syllabus;

import java.util.List;

/**
 * Syllabus tab: the exam's syllabus, item by item, with how many exam questions (and APSC questions) each item has,
 * which chapters teach it, and how many of its questions the user has solved.
 * Section counts are distinct questions: an item can share topics with another, so item counts do not add up.
 */
public record SyllabusView(String exam, List<Section> sections) {

    public record Section(String code, String text, String note, String subjectSlug, String subjectName, List<Item> items,
                          long questions, long apsc, long solved) {}

    public record Item(String code, String text, String note, long questions, long apsc, long written, long solved,
                       List<ChapterLink> chapters) {}

    public record ChapterLink(String slug, String name, int sortOrder, boolean notes, String subjectSlug) {}
}
