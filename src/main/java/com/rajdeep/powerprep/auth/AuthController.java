package com.rajdeep.powerprep.auth;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class AuthController {
    private final GoogleTokenVerifier google;
    private final UserRepository users;
    private final TokenService tokens;
    private final AuthProperties props;

    public AuthController(GoogleTokenVerifier google, UserRepository users, TokenService tokens, AuthProperties props) {
        this.google = google; this.users = users; this.tokens = tokens; this.props = props;
    }

    public record GoogleLogin(String credential) {}

    /** The browser sends the Google ID token; we answer with our own session token. */
    @PostMapping("/auth/google")
    public Map<String, Object> google(@RequestBody GoogleLogin body) {
        if (body == null || body.credential() == null || body.credential().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "credential is required");
        }
        Jwt g;
        try {
            g = google.verify(body.credential());
        } catch (JwtException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Google sign-in could not be verified");
        }
        String email = g.getClaimAsString("email");
        boolean admin = props.adminEmail() != null && !props.adminEmail().isBlank()
                && props.adminEmail().equalsIgnoreCase(email);
        AppUser u = users.upsertGoogleUser(g.getSubject(), email,
                g.getClaimAsString("name"), g.getClaimAsString("picture"), admin);
        return Map.of("token", tokens.issue(u), "user", view(u));
    }

    @GetMapping("/me")
    public Map<String, Object> me(JwtAuthenticationToken auth) {
        AppUser u = users.findById(Long.parseLong(auth.getName()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        return view(u);
    }

    static Map<String, Object> view(AppUser u) {
        return Map.of("id", u.id(), "email", u.email(), "name", u.name() == null ? "" : u.name(),
                "avatarUrl", u.avatarUrl() == null ? "" : u.avatarUrl(), "role", u.role(), "status", u.status());
    }
}
