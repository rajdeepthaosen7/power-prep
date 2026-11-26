package com.rajdeep.powerprep.study;

import com.rajdeep.powerprep.attempt.AttemptMode;
import com.rajdeep.powerprep.common.web.CurrentUser;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class StudyController {
    private final StudyService study;
    private final QuestionAttemptService attempts;

    public StudyController(StudyService study, QuestionAttemptService attempts) {
        this.study = study;
        this.attempts = attempts;
    }

    @GetMapping("/subjects/{slug}/chapters")
    public List<ChapterSummary> chapters(@PathVariable String slug, CurrentUser user) {
        return study.chapters(slug, user);
    }

    @GetMapping("/subjects/{slug}/chapters/{chapter}")
    public ChapterDetail chapter(@PathVariable String slug, @PathVariable String chapter, CurrentUser user) {
        return study.chapter(slug, chapter, user);
    }

    public record AttemptRequest(List<String> chosen, BigDecimal natValue, Integer timeMs, String mode) {}

    @PostMapping("/questions/{id}/attempts")
    public AttemptResult attempt(@PathVariable int id, @RequestBody AttemptRequest req, CurrentUser user) {
        return attempts.attempt(user.requireId(), id, req.chosen(), req.natValue(), req.timeMs(),
            AttemptMode.parseOrStudy(req.mode()));
    }
}
