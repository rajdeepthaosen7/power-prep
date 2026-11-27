package com.rajdeep.powerprep.pyq;

import com.rajdeep.powerprep.common.error.BadRequestException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * One exam paper, worked out from a source code and question numbers.
 * Most GATE years are one source with plain numbers ("GATE-2012", q "14"). 2014-2016 keep their sets in the
 * question number ("GATE-2014", q "S2-Q14"), 2017 has one source per set ("GATE-2017_1"). Each set is its own paper.
 * The key is what the API uses: "GATE-2012", "GATE-2014-S2", "GATE-2017_1".
 */
public record PaperRef(String key, String sourceCode, String qPrefix, String exam, int year, Integer set) {
    private static final Pattern SOURCE = Pattern.compile("([A-Z]+)-(\\d{4})(?:_(\\d+))?");
    private static final Pattern KEY = Pattern.compile("([A-Z]+-\\d{4}(?:_\\d+)?)(?:-S(\\d+))?");
    private static final Pattern SET_Q = Pattern.compile("S(\\d+)-Q(\\d+)");
    private static final Pattern PLAIN_Q = Pattern.compile("\\d+");

    /** The paper a question belongs to: its source plus the set in its question number, if any. */
    public static PaperRef of(String sourceCode, String qNo) {
        Matcher q = SET_Q.matcher(qNo == null ? "" : qNo);
        return q.matches() ? fromKey(sourceCode + "-S" + q.group(1)) : fromKey(sourceCode);
    }

    public static PaperRef fromKey(String key) {
        Matcher k = KEY.matcher(key == null ? "" : key.trim());
        if (!k.matches()) throw new BadRequestException("unknown paper " + key);
        String source = k.group(1);
        Matcher s = SOURCE.matcher(source);
        if (!s.matches()) throw new BadRequestException("unknown paper " + key);
        Integer set = k.group(2) != null ? Integer.valueOf(k.group(2)) : s.group(3) != null ? Integer.valueOf(s.group(3)) : null;
        String prefix = k.group(2) != null ? "S" + k.group(2) + "-Q" : null;
        return new PaperRef(k.group(0), source, prefix, s.group(1), Integer.parseInt(s.group(2)), set);
    }

    /** "GATE EE 2014, Set 2". */
    public String title() {
        String exam = "GATE".equals(this.exam) ? "GATE EE" : "ESE".equals(this.exam) ? "UPSC ESE (Electrical)" : this.exam;
        return exam + " " + year + (set == null ? "" : ", Set " + set);
    }

    /** The question's number inside its paper (14 for "14" or "S2-Q14"); empty for old rows kept by name only. */
    public static Optional<Integer> number(String qNo) {
        if (qNo == null) return Optional.empty();
        Matcher q = SET_Q.matcher(qNo);
        if (q.matches()) return Optional.of(Integer.valueOf(q.group(2)));
        return PLAIN_Q.matcher(qNo).matches() ? Optional.of(Integer.valueOf(qNo)) : Optional.empty();
    }
}
