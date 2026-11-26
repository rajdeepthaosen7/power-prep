package com.rajdeep.powerprep.syllabus;

import com.rajdeep.powerprep.common.web.CurrentUser;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class SyllabusService {
    public static final String DEFAULT_EXAM = "APGCL-AM-2026";
    private static final long NO_USER = -1L;

    private final SyllabusRepository syllabus;

    public SyllabusService(SyllabusRepository syllabus) { this.syllabus = syllabus; }

    public SyllabusView syllabus(String exam, CurrentUser user) {
        long uid = user.idOr(NO_USER);
        List<SyllabusRepository.ItemRow> rows = syllabus.items(exam);
        Map<Integer, SyllabusRepository.Counts> itemCounts = byId(syllabus.itemCounts(exam, uid));
        Map<Integer, SyllabusRepository.Counts> sectionCounts = byId(syllabus.sectionCounts(exam, uid));
        List<SyllabusRepository.ItemChapter> chapters = syllabus.chapters(exam);

        List<SyllabusView.Section> sections = rows.stream().filter(SyllabusRepository.ItemRow::isSection).map(s -> {
            List<SyllabusView.Item> items = rows.stream().filter(r -> s.id() == (r.parentId() == null ? -1 : r.parentId()))
                .map(r -> item(r, itemCounts.getOrDefault(r.id(), SyllabusRepository.Counts.NONE), chapters))
                .toList();
            SyllabusRepository.Counts c = sectionCounts.getOrDefault(s.id(), SyllabusRepository.Counts.NONE);
            return new SyllabusView.Section(s.code(), s.text(), s.note(), s.subjectSlug(), s.subjectName(), items,
                c.questions(), c.apsc(), c.solved());
        }).toList();
        return new SyllabusView(exam, sections);
    }

    private static SyllabusView.Item item(SyllabusRepository.ItemRow r, SyllabusRepository.Counts c,
                                          List<SyllabusRepository.ItemChapter> chapters) {
        List<SyllabusView.ChapterLink> links = chapters.stream().filter(ch -> ch.itemId() == r.id())
            .map(ch -> new SyllabusView.ChapterLink(ch.slug(), ch.name(), ch.sortOrder(), ch.notes(), ch.subjectSlug()))
            .toList();
        return new SyllabusView.Item(r.code(), r.text(), r.note(), c.questions(), c.apsc(), c.written(), c.solved(), links);
    }

    private static Map<Integer, SyllabusRepository.Counts> byId(List<SyllabusRepository.Counts> counts) {
        return counts.stream().collect(Collectors.toMap(SyllabusRepository.Counts::id, Function.identity()));
    }
}
