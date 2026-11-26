package com.rajdeep.powerprep.practice;

public record PracticeQuestion(int id, String subject, int topicId, String topic, String nature, String status,
                               String summary, String stem, String sources, Integer firstYear) {}
