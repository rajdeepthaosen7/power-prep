package com.rajdeep.powerprep.pyq;

import com.rajdeep.powerprep.common.error.BadRequestException;
import com.rajdeep.powerprep.common.error.NotFoundException;
import com.rajdeep.powerprep.mocktest.MockTestService;
import com.rajdeep.powerprep.mocktest.StartedTest;
import com.rajdeep.powerprep.mocktest.TestList;
import com.rajdeep.powerprep.mocktest.TestMode;
import com.rajdeep.powerprep.mocktest.TestStyle;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HexFormat;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * PYQ practice (docs/pyq-plan.md): every paper of an exam, subject / chapter sets across years, and starting one of them
 * as a test. A started selection becomes the user's own test (kind PYQ_SET) holding a snapshot of the written questions;
 * the same selection with the same questions is the same test, so its earlier scores stay together. When more questions
 * are written, the selection gets a new test. Running the test is the job of {@link MockTestService}.
 */
@Service
public class PyqService {
    private static final Comparator<PyqRepository.Picked> PAPER_ORDER = Comparator
        .comparingInt((PyqRepository.Picked p) -> p.year())
        .thenComparing(p -> PaperRef.of(p.sourceCode(), p.qNo()).set() == null ? 0 : PaperRef.of(p.sourceCode(), p.qNo()).set())
        .thenComparing(p -> PaperRef.number(p.qNo()).orElse(Integer.MAX_VALUE))
        .thenComparing(PyqRepository.Picked::qNo);

    private final PyqRepository pyq;
    private final MockTestService tests;

    public PyqService(PyqRepository pyq, MockTestService tests) {
        this.pyq = pyq;
        this.tests = tests;
    }

    public PaperList papers(String exam, Long userId) {
        String ex = PyqSelection.examOf(exam);
        record Tally(PaperRef ref, int[] n, BigDecimal[] marks) {}
        Map<String, Tally> byPaper = new TreeMap<>();
        for (PyqRepository.SourceRow r : pyq.sourceRows(ex)) {
            PaperRef ref = PaperRef.of(r.sourceCode(), r.qNo());
            Tally t = byPaper.computeIfAbsent(ref.key(), k -> new Tally(ref, new int[2], new BigDecimal[]{BigDecimal.ZERO}));
            t.n()[0]++;
            if (r.ready()) {
                t.n()[1]++;
                t.marks()[0] = t.marks()[0].add(r.marks() == null ? BigDecimal.ONE : r.marks());
            }
        }
        Map<String, List<TestList.Attempt>> attempts = attemptsBy(userId, "paper=");
        List<PaperList.Paper> papers = byPaper.values().stream()
            .sorted(Comparator.comparingInt((Tally t) -> t.ref().year()).thenComparing(t -> t.ref().set() == null ? 0 : t.ref().set()))
            .map(t -> new PaperList.Paper(t.ref().key(), t.ref().title(), t.ref().year(), t.ref().set(), t.n()[0], t.n()[1],
                t.marks()[0], attempts.getOrDefault("paper=" + t.ref().key(), List.of())))
            .toList();
        return new PaperList(ex, papers);
    }

    public PyqSets sets(String exam, Long userId) {
        String ex = PyqSelection.examOf(exam);
        Map<String, List<PyqRepository.CountRow>> bySubject = pyq.countsByChapter(ex).stream()
            .collect(Collectors.groupingBy(PyqRepository.CountRow::subjectSlug, LinkedHashMap::new, Collectors.toList()));
        List<PyqRepository.OwnAttempt> own = userId == null ? List.of() : pyq.ownAttempts(userId);
        List<PyqSets.Subject> subjects = new ArrayList<>();
        bySubject.forEach((slug, rows) -> {
            int ready = rows.stream().mapToInt(PyqRepository.CountRow::ready).sum();
            int total = rows.stream().mapToInt(PyqRepository.CountRow::total).sum();
            Integer first = rows.stream().map(PyqRepository.CountRow::firstYear).filter(y -> y != null).min(Integer::compare).orElse(null);
            Integer last = rows.stream().map(PyqRepository.CountRow::lastYear).filter(y -> y != null).max(Integer::compare).orElse(null);
            List<PyqSets.Chapter> chapters = rows.stream().filter(r -> r.chapterSlug() != null)
                .map(r -> new PyqSets.Chapter(r.chapterSlug(), r.chapterName(), r.ready(), r.total(), r.firstYear(), r.lastYear()))
                .toList();
            List<PyqSets.SetAttempt> attempts = new ArrayList<>();
            for (PyqRepository.OwnAttempt a : own) {
                Map<String, String> k = selectionOf(a.code());
                if (!ex.equals(k.get("exam")) || !slug.equals(k.get("subject"))) continue;
                String[] years = k.getOrDefault("years", "-").split("-");
                attempts.add(new PyqSets.SetAttempt(a.title(), k.get("chapter"), intOrNull(years, 0), intOrNull(years, 1), a.attempt()));
            }
            subjects.add(new PyqSets.Subject(slug, rows.get(0).subjectName(), ready, total, first, last, chapters, attempts));
        });
        return new PyqSets(ex, subjects);
    }

    @Transactional
    public StartedTest start(long userId, PyqSelection request) {
        PyqSelection sel = request.normalised();
        TestMode mode = TestMode.parseOrTest(sel.mode());
        TestStyle style = TestStyle.parseOrApsc(sel.style());
        PaperRef paper = null;
        String title, description;
        if (sel.isPaper()) {
            paper = PaperRef.fromKey(sel.paper());
            String key = paper.key();
            PaperList.Paper p = papers(sel.exam(), null).papers().stream().filter(x -> x.key().equals(key)).findFirst()
                .orElseThrow(() -> new NotFoundException("paper " + key + " not found"));
            title = p.title();
            description = "Written in the portal so far: " + p.ready() + " of " + p.questions() + " questions of this paper.";
        } else {
            String subject = pyq.subjectName(sel.subject()).orElseThrow(() -> new NotFoundException("subject not found"));
            String chapter = sel.chapter() == null ? null
                : pyq.chapterName(sel.subject(), sel.chapter()).orElseThrow(() -> new NotFoundException("chapter not found"));
            title = examName(sel.exam()) + " PYQs: " + (chapter == null ? subject : chapter) + ", " + yearsText(sel);
            description = "Every written " + examName(sel.exam()) + " question of " + (chapter == null ? subject : chapter)
                + " from " + yearsText(sel) + ", oldest first.";
        }
        List<PyqRepository.Picked> picked = uniqueInOrder(pyq.pick(sel, paper));
        if (picked.isEmpty()) throw new NotFoundException("no questions are written for this selection yet");
        String code = "pyq:" + userId + ":" + sel.key() + ":" + hash(picked);
        int testId = pyq.testIdByCode(code).orElseGet(() -> pyq.createSetTest(code, title, description, userId, picked));
        return tests.start(userId, testId, mode, style);
    }

    // ------------------------------------------------------------------ helpers

    private static String examName(String exam) { return "GATE".equals(exam) ? "GATE EE" : "UPSC ESE"; }

    private static String yearsText(PyqSelection sel) {
        boolean all = sel.yearFrom() == PyqSelection.FIRST_YEAR && sel.yearTo() == PyqSelection.LAST_YEAR;
        return all ? "all years" : sel.yearFrom() + "-" + sel.yearTo();
    }

    private static List<PyqRepository.Picked> uniqueInOrder(List<PyqRepository.Picked> rows) {
        Set<Integer> seen = new LinkedHashSet<>();
        return rows.stream().sorted(PAPER_ORDER).filter(p -> seen.add(p.questionId())).toList();
    }

    private static String hash(List<PyqRepository.Picked> picked) {
        String ids = picked.stream().map(p -> String.valueOf(p.questionId())).collect(Collectors.joining(","));
        try {
            byte[] d = MessageDigest.getInstance("SHA-256").digest(ids.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(d, 0, 6);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    private Map<String, List<TestList.Attempt>> attemptsBy(Long userId, String prefix) {
        if (userId == null) return Map.of();
        Map<String, List<TestList.Attempt>> out = new LinkedHashMap<>();
        for (PyqRepository.OwnAttempt a : pyq.ownAttempts(userId)) {
            String key = a.code().split(":", 4).length == 4 ? a.code().split(":", 4)[2] : "";
            if (key.startsWith(prefix)) out.computeIfAbsent(key, k -> new ArrayList<>()).add(a.attempt());
        }
        return out;
    }

    /** "pyq:7:exam=GATE,subject=x,years=1991-2100:ab12" -> {exam=GATE, subject=x, years=1991-2100}. */
    static Map<String, String> selectionOf(String code) {
        Map<String, String> out = new LinkedHashMap<>();
        String[] parts = code.split(":", 4);
        if (parts.length != 4) return out;
        for (String kv : parts[2].split(",")) {
            int i = kv.indexOf('=');
            if (i > 0) out.put(kv.substring(0, i), kv.substring(i + 1));
        }
        return out;
    }

    private static Integer intOrNull(String[] a, int i) {
        try {
            return i < a.length ? Integer.valueOf(a[i]) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
