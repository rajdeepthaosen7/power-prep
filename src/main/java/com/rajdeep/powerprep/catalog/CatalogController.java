package com.rajdeep.powerprep.catalog;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CatalogController {
    private final CatalogService catalog;

    public CatalogController(CatalogService catalog) { this.catalog = catalog; }

    @GetMapping("/subjects")
    public List<SubjectView> subjects() { return catalog.subjects(); }

    @GetMapping("/subjects/{slug}/topics")
    public List<TopicStats> topics(@PathVariable String slug) { return catalog.topics(slug); }

    @GetMapping("/topics/{id}/questions")
    public List<TopicQuestion> questions(@PathVariable int id) { return catalog.questions(id); }
}
