package com.rajdeep.powerprep.pyq;

import com.rajdeep.powerprep.common.error.BadRequestException;

/**
 * What the user wants to practise: one paper ({@code paper} = "GATE-2014-S2"), or one subject (optionally one chapter)
 * across a range of years. {@code exam} is GATE (default) or ESE. {@code mode} TEST / PREPARE, {@code style} APSC / GATE.
 */
public record PyqSelection(String exam, String paper, String subject, String chapter, Integer yearFrom, Integer yearTo,
                           String mode, String style) {
    static final int FIRST_YEAR = 1991, LAST_YEAR = 2100;

    /** Checks the selection and fills defaults (GATE, all years). */
    public PyqSelection normalised() {
        String ex = examOf(exam);
        boolean hasPaper = paper != null && !paper.isBlank(), hasSubject = subject != null && !subject.isBlank();
        if (hasPaper == hasSubject) throw new BadRequestException("choose either one paper or one subject");
        if (hasPaper) {
            PaperRef p = PaperRef.fromKey(paper);
            if (!p.exam().equals(ex)) throw new BadRequestException("paper " + paper + " is not a " + ex + " paper");
            return new PyqSelection(ex, p.key(), null, null, null, null, mode, style);
        }
        if (!subject.matches("[a-z0-9-]+")) throw new BadRequestException("unknown subject " + subject);
        String ch = chapter == null || chapter.isBlank() ? null : chapter.trim();
        if (ch != null && !ch.matches("[a-z0-9-]+")) throw new BadRequestException("unknown chapter " + chapter);
        int from = yearFrom == null ? FIRST_YEAR : yearFrom, to = yearTo == null ? LAST_YEAR : yearTo;
        if (from > to) throw new BadRequestException("yearFrom must not be after yearTo");
        return new PyqSelection(ex, null, subject.trim(), ch, from, to, mode, style);
    }

    /** GATE (default) or ESE; anything else is refused. */
    public static String examOf(String exam) {
        String ex = exam == null || exam.isBlank() ? "GATE" : exam.trim().toUpperCase();
        if (!ex.equals("GATE") && !ex.equals("ESE")) throw new BadRequestException("exam must be GATE or ESE");
        return ex;
    }

    /** Stable text key of the selection, part of the test code (no ':' inside). */
    public String key() {
        if (paper != null) return "paper=" + paper;
        return "exam=" + exam + ",subject=" + subject + (chapter == null ? "" : ",chapter=" + chapter) + ",years=" + yearFrom + "-" + yearTo;
    }

    public boolean isPaper() { return paper != null; }
}
