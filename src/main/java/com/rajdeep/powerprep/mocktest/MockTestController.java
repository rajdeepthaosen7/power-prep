package com.rajdeep.powerprep.mocktest;

import com.rajdeep.powerprep.common.web.CurrentUser;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MockTestController {
    private final MockTestService tests;

    public MockTestController(MockTestService tests) { this.tests = tests; }

    @GetMapping("/tests")
    public TestList list(@RequestParam String subject, CurrentUser user) {
        return tests.list(subject, user);
    }

    /** mode: TEST (default) or PREPARE; style: APSC (default) or GATE. */
    @PostMapping("/tests/{id}/start")
    public StartedTest start(@PathVariable int id, @RequestParam(required = false) String mode,
                             @RequestParam(required = false) String style, CurrentUser user) {
        return tests.start(user.requireId(), id, TestMode.parseOrTest(mode), TestStyle.parseOrApsc(style));
    }

    /** PREPARE mode: one answer, its solution comes back at once. */
    @PostMapping("/test-attempts/{id}/answers")
    public PreparedAnswer answer(@PathVariable long id, @RequestBody MockTestService.Answer answer, CurrentUser user) {
        return tests.answer(user.requireId(), id, answer);
    }

    public record SubmitRequest(List<MockTestService.Answer> answers, Long timeMs) {}

    @PostMapping("/test-attempts/{id}/submit")
    public TestResult submit(@PathVariable long id, @RequestBody SubmitRequest req, CurrentUser user) {
        return tests.submit(user.requireId(), id, req.answers(), req.timeMs());
    }

    @GetMapping("/test-attempts/{id}")
    public TestResult result(@PathVariable long id, CurrentUser user) {
        return tests.result(user.requireId(), id);
    }
}
