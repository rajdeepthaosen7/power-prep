package com.rajdeep.powerprep.auth;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Duration;
import java.time.Instant;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

/** Issues and checks our own session tokens (HS256 JWT, subject = user id). */
@Service
public class TokenService {
    static final String ISSUER = "power-prep";
    private final JwtEncoder encoder;
    private final JwtDecoder decoder;
    private final Duration ttl;

    public TokenService(AuthProperties props) {
        if (props.jwtSecret() == null || props.jwtSecret().length() < 32) {
            throw new IllegalStateException("JWT_SECRET must be set (at least 32 characters)");
        }
        SecretKey key = deriveKey(props.jwtSecret());
        this.encoder = new NimbusJwtEncoder(new ImmutableSecret<>(key));
        NimbusJwtDecoder d = NimbusJwtDecoder.withSecretKey(key).macAlgorithm(MacAlgorithm.HS256).build();
        d.setJwtValidator(JwtValidators.createDefaultWithIssuer(ISSUER));
        this.decoder = d;
        this.ttl = Duration.ofDays(props.tokenDays());
    }

    public String issue(AppUser user) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(ISSUER).subject(Long.toString(user.id()))
                .issuedAt(now).expiresAt(now.plus(ttl)).build();
        return encoder.encode(JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims))
                .getTokenValue();
    }

    public JwtDecoder decoder() { return decoder; }

    private static SecretKey deriveKey(String secret) {
        try {
            byte[] bytes = MessageDigest.getInstance("SHA-256").digest(secret.getBytes(StandardCharsets.UTF_8));
            return new SecretKeySpec(bytes, "HmacSHA256");
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
