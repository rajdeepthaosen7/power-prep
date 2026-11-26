package com.rajdeep.powerprep.study;

import com.rajdeep.powerprep.revision.ProgressView;
import java.util.List;
import java.util.Map;

/** One chapter page: notes and questions of every topic, the figures they use, and the revision state. */
public record ChapterDetail(int id, String slug, String name, String description, int sortOrder, String subjectName,
                            List<Topic> topics, ProgressView progress, Map<String, String> figures) {

    public record Topic(int id, String slug, String name, List<NoteSection> sections, List<StudyQuestion> questions) {}
}
