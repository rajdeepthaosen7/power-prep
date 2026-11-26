package com.rajdeep.powerprep.revision;

import com.rajdeep.powerprep.common.web.CurrentUser;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ChapterProgressController {
    private final RevisionService revision;

    public ChapterProgressController(RevisionService revision) { this.revision = revision; }

    public record ProgressRequest(String action) {}

    @PostMapping("/chapters/{id}/progress")
    public ProgressView progress(@PathVariable int id, @RequestBody ProgressRequest req, CurrentUser user) {
        long uid = user.requireId();
        return revision.apply(uid, id, ProgressAction.parse(req.action()));
    }
}
