package com.rajdeep.powerprep.syllabus;

import java.util.List;

public interface SyllabusRepository {
    record ItemRow(int id, String code, Integer parentId, String text, String note, String subjectSlug, String subjectName) {
        boolean isSection() { return parentId == null; }
    }
    /** Per item (or per section, with sections = true): distinct exam questions, APSC ones, written ones, solved by the user. */
    record Counts(int id, long questions, long apsc, long written, long solved) {
        static final Counts NONE = new Counts(0, 0, 0, 0, 0);
    }
    record ItemChapter(int itemId, String slug, String name, int sortOrder, String subjectSlug, boolean notes) {}

    /** Sections first, then items, each in syllabus order. */
    List<ItemRow> items(String exam);
    List<Counts> itemCounts(String exam, long userId);
    List<Counts> sectionCounts(String exam, long userId);
    List<ItemChapter> chapters(String exam);
}
