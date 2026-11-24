package com.rajdeep.powerprep.auth;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/** Admin section: sign-in requests and user management. Only approved admins reach this. */
@RestController
@RequestMapping("/api/admin/users")
public class AdminController {
    private static final Set<String> STATUSES = Set.of("PENDING", "APPROVED", "REJECTED", "DISABLED");
    private static final Set<String> ROLES = Set.of("USER", "ADMIN");
    private final UserRepository users;

    public AdminController(UserRepository users) { this.users = users; }

    @GetMapping
    public List<Map<String, Object>> list(@RequestParam(required = false) String status) {
        if (status != null && !STATUSES.contains(status)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "unknown status");
        }
        return users.list(status);
    }

    public record StatusChange(String status) {}
    public record RoleChange(String role) {}

    @PostMapping("/{id}/status")
    public Map<String, Object> setStatus(@PathVariable long id, @RequestBody StatusChange body, JwtAuthenticationToken auth) {
        long me = Long.parseLong(auth.getName());
        if (body == null || !STATUSES.contains(body.status()) || "PENDING".equals(body.status())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "status must be APPROVED, REJECTED or DISABLED");
        }
        if (id == me) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "you cannot change your own status");
        if (users.setStatus(id, body.status(), me) == 0) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return AuthController.view(users.findById(id).orElseThrow());
    }

    @PostMapping("/{id}/role")
    public Map<String, Object> setRole(@PathVariable long id, @RequestBody RoleChange body, JwtAuthenticationToken auth) {
        if (body == null || !ROLES.contains(body.role())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "role must be USER or ADMIN");
        }
        if (id == Long.parseLong(auth.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "you cannot change your own role");
        }
        if (users.setRole(id, body.role()) == 0) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return AuthController.view(users.findById(id).orElseThrow());
    }
}
