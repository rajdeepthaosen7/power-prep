package com.rajdeep.powerprep.today;

import com.rajdeep.powerprep.common.web.CurrentUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TodayController {
    private final TodayService today;

    public TodayController(TodayService today) { this.today = today; }

    @GetMapping("/today")
    public TodayPlan today(CurrentUser user) {
        return today.plan(user.requireId());
    }
}
