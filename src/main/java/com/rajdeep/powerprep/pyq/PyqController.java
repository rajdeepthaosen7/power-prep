package com.rajdeep.powerprep.pyq;

import com.rajdeep.powerprep.common.web.CurrentUser;
import com.rajdeep.powerprep.mocktest.StartedTest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** PYQ practice: papers and subject / chapter sets of an exam, and starting one as a test (prepare or test mode). */
@RestController
@RequestMapping("/api/pyq")
public class PyqController {
    private final PyqService pyq;

    public PyqController(PyqService pyq) { this.pyq = pyq; }

    @GetMapping("/papers")
    public PaperList papers(@RequestParam(required = false) String exam, CurrentUser user) {
        return pyq.papers(exam, user.isSignedIn() ? user.id() : null);
    }

    @GetMapping("/sets")
    public PyqSets sets(@RequestParam(required = false) String exam, CurrentUser user) {
        return pyq.sets(exam, user.isSignedIn() ? user.id() : null);
    }

    /** Returns the started test; then the Tests screens run it through /api/test-attempts/... */
    @PostMapping("/start")
    public StartedTest start(@RequestBody PyqSelection selection, CurrentUser user) {
        return pyq.start(user.requireId(), selection);
    }
}
