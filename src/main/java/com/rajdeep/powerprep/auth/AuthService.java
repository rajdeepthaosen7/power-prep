package com.rajdeep.powerprep.auth;

import com.rajdeep.powerprep.common.error.BadRequestException;
import com.rajdeep.powerprep.common.error.UnauthorizedException;
import org.springframework.stereotype.Service;

/** Sign-in: verify the identity provider's token, create or update the user, issue our own session token. */
@Service
public class AuthService {
    private final IdentityTokenVerifier verifier;
    private final UserRepository users;
    private final TokenService tokens;
    private final AuthProperties props;

    public AuthService(IdentityTokenVerifier verifier, UserRepository users, TokenService tokens, AuthProperties props) {
        this.verifier = verifier;
        this.users = users;
        this.tokens = tokens;
        this.props = props;
    }

    public record Login(String token, UserView user) {}

    public Login signIn(String idToken) {
        if (idToken == null || idToken.isBlank()) throw new BadRequestException("credential is required");
        IdentityToken who;
        try {
            who = verifier.verify(idToken);
        } catch (IdentityTokenVerifier.InvalidIdentityTokenException e) {
            throw new UnauthorizedException("Google sign-in could not be verified");
        }
        AppUser u = users.upsertSignedIn(who, isAdminEmail(who.email()));
        return new Login(tokens.issue(u), UserView.of(u));
    }

    public UserView me(long userId) {
        return users.findById(userId).map(UserView::of).orElseThrow(() -> new UnauthorizedException("sign in first"));
    }

    private boolean isAdminEmail(String email) {
        return props.adminEmail() != null && !props.adminEmail().isBlank() && props.adminEmail().equalsIgnoreCase(email);
    }
}
