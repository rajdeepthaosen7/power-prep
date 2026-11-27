package com.rajdeep.powerprep.question;

/**
 * Where a question is taught in the Study tab. {@code hasNotes} is false while its chapter is not written yet;
 * the link to the notes is shown only when it is true.
 */
public record TopicLink(String subjectSlug, String chapterSlug, String chapterName, String topicSlug, String topicName,
                        boolean hasNotes) {}
