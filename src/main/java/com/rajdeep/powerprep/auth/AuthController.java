package com.rajdeep.powerprep.auth;

import com.rajdeep.powerprep.common.web.CurrentUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {
    private final AuthService auth;

    public AuthController(AuthService auth) { this.auth = auth; }

    public record GoogleLogin(String credential) {}

    /** The browser sends the Google ID token; we answer with our own session token. */
    @PostMapping("/auth/google")
    public AuthService.Login google(@RequestBody GoogleLogin body) {
        return auth.signIn(body == null ? null : body.credential());
    }

    @GetMapping("/me")
    public UserView me(CurrentUser user) {
        return auth.me(user.requireId());
    }
}
