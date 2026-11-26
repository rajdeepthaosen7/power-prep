package com.rajdeep.powerprep.practice;

import java.util.LinkedHashMap;
import java.util.Map;

/** Optional filters of the Practice tab. {@link #where()} and {@link #params()} always go together. */
public record QuestionFilter(String subject, Integer topic, String exam, Integer year, String kind, String paper) {

    public String where() {
        StringBuilder w = new StringBuilder();
        if (subject != null) w.append(" AND s.slug = :subject");
        if (topic != null)   w.append(" AND t.id = :topic");
        if (exam != null)    w.append(" AND so.exam = :exam");
        if (year != null)    w.append(" AND so.year = :year");
        if (kind != null)    w.append(" AND so.kind = :kind");
        if (paper != null)   w.append(" AND so.code = :paper");
        return w.toString();
    }

    public Map<String, Object> params() {
        Map<String, Object> p = new LinkedHashMap<>();
        if (subject != null) p.put("subject", subject);
        if (topic != null)   p.put("topic", topic);
        if (exam != null)    p.put("exam", exam);
        if (year != null)    p.put("year", year);
        if (kind != null)    p.put("kind", kind);
        if (paper != null)   p.put("paper", paper);
        return p;
    }
}
