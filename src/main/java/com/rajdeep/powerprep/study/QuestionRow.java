package com.rajdeep.powerprep.study;

public record QuestionRow(int id, int topicId, String key, String stemMd, String answerType, String nature,
                          Integer difficulty, String answerStatus) {}
