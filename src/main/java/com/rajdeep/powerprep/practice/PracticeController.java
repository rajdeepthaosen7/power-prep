package com.rajdeep.powerprep.practice;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/practice")
public class PracticeController {
    private final PracticeService practice;

    public PracticeController(PracticeService practice) { this.practice = practice; }

    @GetMapping("/pivot")
    public PivotTable pivot(@RequestParam(defaultValue = "subject") String rows,
                            @RequestParam(defaultValue = "exam") String cols,
                            @RequestParam(required = false) String subject,
                            @RequestParam(required = false) Integer topic,
                            @RequestParam(required = false) String exam,
                            @RequestParam(required = false) Integer year,
                            @RequestParam(required = false) String kind,
                            @RequestParam(required = false) String paper) {
        return practice.pivot(PivotDimension.parse(rows), PivotDimension.parse(cols),
                new QuestionFilter(subject, topic, exam, year, kind, paper));
    }

    @GetMapping("/questions")
    public List<PracticeQuestion> questions(@RequestParam(required = false) String subject,
                                            @RequestParam(required = false) Integer topic,
                                            @RequestParam(required = false) String exam,
                                            @RequestParam(required = false) Integer year,
                                            @RequestParam(required = false) String kind,
                                            @RequestParam(required = false) String paper,
                                            @RequestParam(defaultValue = "500") int limit) {
        return practice.questions(new QuestionFilter(subject, topic, exam, year, kind, paper), limit);
    }
}
