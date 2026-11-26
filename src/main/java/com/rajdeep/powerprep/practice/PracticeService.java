package com.rajdeep.powerprep.practice;

import com.rajdeep.powerprep.common.error.BadRequestException;
import java.util.List;
import org.springframework.stereotype.Service;

/** Practice tab: exam-question counts as a pivot table, and the question list behind each cell. */
@Service
public class PracticeService {
    static final int MAX_LIST = 500;
    private final PracticeRepository practice;

    public PracticeService(PracticeRepository practice) { this.practice = practice; }

    public PivotTable pivot(PivotDimension rows, PivotDimension cols, QuestionFilter filter) {
        if (rows == cols) throw new BadRequestException("rows and cols must differ");
        return new PivotTable(practice.headers(rows, filter), practice.headers(cols, filter),
                practice.cells(rows, cols, filter), practice.total(filter));
    }

    public List<PracticeQuestion> questions(QuestionFilter filter, int limit) {
        return practice.questions(filter, Math.min(Math.max(limit, 1), MAX_LIST));
    }
}
