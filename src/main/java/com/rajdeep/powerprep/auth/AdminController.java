package com.rajdeep.powerprep.auth;

import com.rajdeep.powerprep.common.web.CurrentUser;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Only approved admins reach this (see SecurityConfig). */
@RestController
@RequestMapping("/api/admin/users")
public class AdminController {
    private final AdminUserService admin;

    public AdminController(AdminUserService admin) { this.admin = admin; }

    @GetMapping
    public List<AdminUserView> list(@RequestParam(required = false) String status) {
        return admin.list(status);
    }

    public record StatusChange(String status) {}
    public record RoleChange(String role) {}

    @PostMapping("/{id}/status")
    public UserView setStatus(@PathVariable long id, @RequestBody StatusChange body, CurrentUser me) {
        return admin.setStatus(me.requireId(), id, body == null ? null : body.status());
    }

    @PostMapping("/{id}/role")
    public UserView setRole(@PathVariable long id, @RequestBody RoleChange body, CurrentUser me) {
        return admin.setRole(me.requireId(), id, body == null ? null : body.role());
    }
}
