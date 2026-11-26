package com.rajdeep.powerprep.study;

import java.util.List;

/** A chapter in the subject's chapter list; the counts are the sums over its topics. */
public record ChapterSummary(int id, String slug, String name, String description, int sortOrder,
                             List<TopicSummary> topics, long notes, long questions, long ready, long attempted) {

    static ChapterSummary of(ChapterRow c, List<TopicSummary> topics) {
        return new ChapterSummary(c.id(), c.slug(), c.name(), c.description(), c.sortOrder(), topics,
                topics.stream().mapToLong(TopicSummary::notes).sum(),
                topics.stream().mapToLong(TopicSummary::questions).sum(),
                topics.stream().mapToLong(TopicSummary::ready).sum(),
                topics.stream().mapToLong(TopicSummary::attempted).sum());
    }
}
