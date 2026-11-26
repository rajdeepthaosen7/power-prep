package com.rajdeep.powerprep.practice;

import com.rajdeep.powerprep.common.error.BadRequestException;
import java.util.Locale;

/**
 * A dimension of the Practice pivot table. Each one knows its SQL key, label and sort expressions.
 * Only these fixed expressions ever reach the SQL text; user input only selects one of them.
 */
public enum PivotDimension {
    SUBJECT("s.slug", "s.name", "min(s.sort_order)"),
    TOPIC("t.id::text", "t.name", "min(t.name)"),
    EXAM("so.exam", "so.exam", "min(CASE so.kind WHEN 'STATE' THEN 0 ELSE 1 END || so.exam)"),
    YEAR("so.year::text", "so.year::text", "min(so.year)"),
    PAPER("so.code", "so.code", "min(so.kind || so.year || so.code)"),
    KIND("so.kind", "so.kind", "min(so.kind)"),
    NATURE("coalesce(q.nature,'UNTAGGED')", "coalesce(q.nature,'UNTAGGED')", "min(coalesce(q.nature,'UNTAGGED'))");

    final String key, label, sort;

    PivotDimension(String key, String label, String sort) {
        this.key = key; this.label = label; this.sort = sort;
    }

    public static PivotDimension parse(String name) {
        try {
            return valueOf(name.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new BadRequestException("unknown dimension: " + name);
        }
    }
}
