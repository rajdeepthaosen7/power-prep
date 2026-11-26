package com.rajdeep.powerprep.question;

/** An option as shown to the user (no answer flag). */
public record OptionView(String label, String bodyMd) {
    public static OptionView of(OptionRow o) { return new OptionView(o.label(), o.bodyMd()); }
}
