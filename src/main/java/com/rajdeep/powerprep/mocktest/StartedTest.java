package com.rajdeep.powerprep.mocktest;

import com.rajdeep.powerprep.question.OptionView;
import java.util.List;
import java.util.Map;

/** A running test: questions without answers. {@code serverNow} lets the browser correct its clock for the timer. */
public record StartedTest(long attemptId, String startedAt, String serverNow, TestMeta test, List<Question> questions,
                          Map<String, String> figures) {
    public record Question(int id, int position, String stemMd, List<OptionView> options) {}
    record QuestionRow(int id, int position, String stemMd) {}
}
