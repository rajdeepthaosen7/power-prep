package com.rajdeep.powerprep.practice;

import java.util.List;

public interface PracticeRepository {
    List<PivotTable.Header> headers(PivotDimension dim, QuestionFilter filter);
    List<PivotTable.Cell> cells(PivotDimension rows, PivotDimension cols, QuestionFilter filter);
    long total(QuestionFilter filter);
    List<PracticeQuestion> questions(QuestionFilter filter, int limit);
}
