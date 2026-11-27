package com.rajdeep.powerprep.pyq;

import com.rajdeep.powerprep.mocktest.TestList;
import java.math.BigDecimal;
import java.util.List;

/**
 * Every paper of an exam. {@code questions}: technical questions of the paper we know of; {@code ready}: those written
 * in the portal (text, options, solution), which are the ones a test asks. {@code attempts}: the user's attempts, newest first.
 */
public record PaperList(String exam, List<Paper> papers) {
    public record Paper(String key, String title, int year, Integer set, int questions, int ready, BigDecimal readyMarks,
                        List<TestList.Attempt> attempts) {}
}
