package com.rajdeep.powerprep.study;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface StudyRepository {
    List<ChapterRow> chaptersOf(String subjectSlug);
    List<TopicSummary> topicSummaries(String subjectSlug, long userId);
    Optional<ChapterRow> chapter(String subjectSlug, String chapterSlug);
    List<TopicRef> topicsOf(int chapterId);
    List<NoteSection> publishedSections(int chapterId);
    /** Written exam questions of the chapter (not our practice questions). */
    List<QuestionRow> readyExamQuestions(int chapterId);
    /** Figures of the chapter's notes and questions, by storage key. */
    Map<String, String> figuresOfChapter(int chapterId);
}
