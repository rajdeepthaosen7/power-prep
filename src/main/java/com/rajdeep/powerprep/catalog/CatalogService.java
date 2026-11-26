package com.rajdeep.powerprep.catalog;

import java.util.List;
import org.springframework.stereotype.Service;

/** Read-only catalog: subjects, topics and the exam questions of a topic. */
@Service
public class CatalogService {
    private final CatalogRepository catalog;

    public CatalogService(CatalogRepository catalog) { this.catalog = catalog; }

    public List<SubjectView> subjects() { return catalog.subjects(); }
    public List<TopicStats> topics(String subjectSlug) { return catalog.topicsOf(subjectSlug); }
    public List<TopicQuestion> questions(int topicId) { return catalog.questionsOf(topicId); }
}
