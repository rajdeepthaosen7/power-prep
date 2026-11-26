package com.rajdeep.powerprep.practice;

import java.util.List;

/** Question counts by two dimensions. {@code total} counts distinct questions (cells can share a question). */
public record PivotTable(List<Header> rows, List<Header> cols, List<Cell> cells, long total) {
    public record Header(String key, String label, long total) {}
    public record Cell(String row, String col, long count) {}
}
