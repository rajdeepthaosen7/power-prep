package com.rajdeep.powerprep.config;

import com.rajdeep.powerprep.common.web.CurrentUserArgumentResolver;
import java.time.Clock;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final CurrentUserArgumentResolver currentUser;

    public WebConfig(CurrentUserArgumentResolver currentUser) { this.currentUser = currentUser; }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(currentUser);
    }

    /** One clock for "now", so time rules (revision due dates) can be tested. */
    @Bean
    Clock clock() { return Clock.systemUTC(); }
}
