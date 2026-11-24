package com.rajdeep.powerprep.auth;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("app.auth")
public record AuthProperties(
        String googleClientId,
        String googleJwkSetUri,
        String adminEmail,
        String jwtSecret,
        int tokenDays,
        boolean contentRequiresApproval) {
}
