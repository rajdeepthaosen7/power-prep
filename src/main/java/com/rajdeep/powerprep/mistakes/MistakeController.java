package com.rajdeep.powerprep.mistakes;

import com.rajdeep.powerprep.common.web.CurrentUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mistakes")
public class MistakeController {
    private final MistakeService mistakes;

    public MistakeController(MistakeService mistakes) { this.mistakes = mistakes; }

    @GetMapping
    public MistakeLog log(CurrentUser user) {
        return mistakes.log(user.requireId());
    }

    public record ReasonRequest(String reason) {}

    @PostMapping("/{questionId}/reason")
    public MistakeService.ReasonSaved reason(@PathVariable int questionId, @RequestBody ReasonRequest req, CurrentUser user) {
        long uid = user.requireId();
        return mistakes.setReason(uid, questionId, MistakeReason.parseOrNull(req.reason()));
    }
}
