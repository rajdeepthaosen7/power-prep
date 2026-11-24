package com.rajdeep.powerprep.auth;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.jwt.BadJwtException;

/**
 * Stateless API security.
 * Authorities come from the database on every request, so approving or disabling a user works at once.
 *   ROLE_USER / ROLE_ADMIN  and  APPROVED (only for approved accounts)
 */
@Configuration
@EnableConfigurationProperties(AuthProperties.class)
public class SecurityConfig {

    @Bean
    SecurityFilterChain api(HttpSecurity http, TokenService tokens, UserRepository users, AuthProperties props) throws Exception {
        http.csrf(c -> c.disable())
            .cors(c -> {})
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(a -> {
                a.requestMatchers("/actuator/health/**", "/error").permitAll();
                a.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
                a.requestMatchers(HttpMethod.POST, "/api/auth/google").permitAll();
                a.requestMatchers("/api/me").authenticated();
                a.requestMatchers("/api/admin/**").access((auth, ctx) -> new org.springframework.security.authorization.AuthorizationDecision(
                        auth.get().getAuthorities().stream().map(Object::toString).toList().containsAll(List.of("ROLE_ADMIN", "APPROVED"))));
                if (props.contentRequiresApproval()) {
                    a.requestMatchers("/api/**").hasAuthority("APPROVED");
                } else {
                    a.requestMatchers("/api/**").permitAll();
                }
                a.anyRequest().denyAll();
            })
            .oauth2ResourceServer(o -> o.jwt(j -> j.decoder(tokens.decoder())
                    .jwtAuthenticationConverter(converter(users))));
        return http.build();
    }

    private Converter<Jwt, AbstractAuthenticationToken> converter(UserRepository users) {
        return jwt -> {
            AppUser u = users.findById(Long.parseLong(jwt.getSubject()))
                    .orElseThrow(() -> new BadJwtException("user not found"));
            List<SimpleGrantedAuthority> auths = new ArrayList<>();
            auths.add(new SimpleGrantedAuthority("ROLE_" + u.role()));
            if (u.isApproved()) auths.add(new SimpleGrantedAuthority("APPROVED"));
            return new JwtAuthenticationToken(jwt, auths, jwt.getSubject());
        };
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration c = new CorsConfiguration();
        c.setAllowedOrigins(List.of("https://power.rajdeepthaosen.com",
                "https://preview-power-prep-web.rajdeepthaosen7.workers.dev",
                "http://localhost:5173", "http://localhost:3000", "http://localhost:5500", "http://127.0.0.1:5500"));
        c.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        c.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        c.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
        src.registerCorsConfiguration("/api/**", c);
        return src;
    }
}
