package com.rajdeep.powerprep.auth;

import java.util.List;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

/** Checks the ID token that Google Sign-In gives the browser. */
@Component
public class GoogleTokenVerifier implements IdentityTokenVerifier {
    private static final List<String> ISSUERS = List.of("accounts.google.com", "https://accounts.google.com");
    private final NimbusJwtDecoder decoder;

    public GoogleTokenVerifier(AuthProperties props) {
        if (props.googleClientId() == null || props.googleClientId().isBlank()) {
            throw new IllegalStateException("GOOGLE_CLIENT_ID must be set");
        }
        decoder = NimbusJwtDecoder.withJwkSetUri(props.googleJwkSetUri()).build();
        OAuth2TokenValidator<Jwt> issuer = jwt -> ISSUERS.contains(jwt.getClaimAsString("iss"))
                ? OAuth2TokenValidatorResult.success()
                : OAuth2TokenValidatorResult.failure(new OAuth2Error("invalid_token", "wrong issuer", null));
        OAuth2TokenValidator<Jwt> audience = jwt -> jwt.getAudience() != null && jwt.getAudience().contains(props.googleClientId())
                ? OAuth2TokenValidatorResult.success()
                : OAuth2TokenValidatorResult.failure(new OAuth2Error("invalid_token", "wrong audience", null));
        OAuth2TokenValidator<Jwt> verified = jwt -> Boolean.TRUE.equals(jwt.getClaimAsBoolean("email_verified"))
                ? OAuth2TokenValidatorResult.success()
                : OAuth2TokenValidatorResult.failure(new OAuth2Error("invalid_token", "email not verified", null));
        decoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(
                new JwtTimestampValidator(), issuer, audience, verified));
    }

    @Override
    public IdentityToken verify(String idToken) {
        try {
            Jwt g = decoder.decode(idToken);
            return new IdentityToken(g.getSubject(), g.getClaimAsString("email"), g.getClaimAsString("name"),
                    g.getClaimAsString("picture"));
        } catch (JwtException e) {
            throw new InvalidIdentityTokenException(e);
        }
    }
}
