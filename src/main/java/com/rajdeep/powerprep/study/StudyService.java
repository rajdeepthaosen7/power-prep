package com.rajdeep.powerprep.study;

import com.rajdeep.powerprep.attempt.AttemptRepository;
import com.rajdeep.powerprep.attempt.LastAttempt;
import com.rajdeep.powerprep.common.error.NotFoundException;
import com.rajdeep.powerprep.common.web.CurrentUser;
import com.rajdeep.powerprep.question.OptionRow;
import com.rajdeep.powerprep.question.OptionView;
import com.rajdeep.powerprep.question.QuestionBank;
import com.rajdeep.powerprep.question.SourceView;
import com.rajdeep.powerprep.revision.RevisionService;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 * Study tab: chapters with notes, and exam questions the user attempts first.
 * The answer and analysis of a question are included only after the signed-in user has attempted it.
 */
@Service
public class StudyService {
    private static final long NO_USER = -1L;

    private final StudyRepository study;
    private final QuestionBank bank;
    private final AttemptRepository attempts;
    private final RevisionService revision;

    public StudyService(StudyRepository study, QuestionBank bank, AttemptRepository attempts, RevisionService revision) {
        this.study = study;
        this.bank = bank;
        this.attempts = attempts;
        this.revision = revision;
    }

    public List<ChapterSummary> chapters(String subjectSlug, CurrentUser user) {
        List<TopicSummary> topics = study.topicSummaries(subjectSlug, user.idOr(NO_USER));
        return study.chaptersOf(subjectSlug).stream()
            .map(c -> ChapterSummary.of(c, topics.stream().filter(t -> t.chapterId() == c.id()).toList()))
            .toList();
    }

    public ChapterDetail chapter(String subjectSlug, String chapterSlug, CurrentUser user) {
        ChapterRow c = study.chapter(subjectSlug, chapterSlug).orElseThrow(() -> new NotFoundException("chapter not found"));
        List<NoteSection> sections = study.publishedSections(c.id());
        List<StudyQuestion> questions = withDetails(study.readyExamQuestions(c.id()), user);
        List<ChapterDetail.Topic> topics = study.topicsOf(c.id()).stream()
            .map(t -> new ChapterDetail.Topic(t.id(), t.slug(), t.name(),
                sections.stream().filter(s -> s.topicId() == t.id()).toList(),
                questions.stream().filter(q -> q.question().topicId() == t.id()).toList()))
            .toList();
        return new ChapterDetail(c.id(), c.slug(), c.name(), c.description(), c.sortOrder(), c.subjectName(), topics,
            user.isSignedIn() ? revision.progressOf(user.id(), c.id()) : null,
            study.figuresOfChapter(c.id()));
    }

    private List<StudyQuestion> withDetails(List<QuestionRow> rows, CurrentUser user) {
        List<Integer> ids = rows.stream().map(QuestionRow::id).toList();
        Map<Integer, List<SourceView>> sources = bank.sources(ids);
        Map<Integer, List<OptionRow>> options = bank.options(ids);
        Map<Integer, LastAttempt> last = user.isSignedIn() ? attempts.latest(user.id(), ids) : Map.of();
        return rows.stream().map(q -> {
            LastAttempt a = last.get(q.id());
            return new StudyQuestion(q, sources.getOrDefault(q.id(), List.of()),
                options.getOrDefault(q.id(), List.of()).stream().map(OptionView::of).toList(),
                a, a == null ? null : bank.review(q.id()));
        }).toList();
    }
}
