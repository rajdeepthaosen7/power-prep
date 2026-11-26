package com.rajdeep.powerprep.catalog;

import java.util.List;

public interface CatalogRepository {
    List<SubjectView> subjects();
    List<TopicStats> topicsOf(String subjectSlug);
    List<TopicQuestion> questionsOf(int topicId);
}
