package com.rajdeep.powerprep.syllabus;

import com.rajdeep.powerprep.common.web.CurrentUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SyllabusController {
    private final SyllabusService syllabus;

    public SyllabusController(SyllabusService syllabus) { this.syllabus = syllabus; }

    @GetMapping("/syllabus")
    public SyllabusView syllabus(@RequestParam(defaultValue = SyllabusService.DEFAULT_EXAM) String exam, CurrentUser user) {
        return syllabus.syllabus(exam, user);
    }
}
