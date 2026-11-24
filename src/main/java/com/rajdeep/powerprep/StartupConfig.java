package com.rajdeep.powerprep;

import org.flywaydb.core.Flyway;
import org.springframework.boot.LazyInitializationExcludeFilter;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The app uses lazy initialization to start fast on the small free server.
 * Database migrations must still run at startup, so a bad migration fails the deploy
 * instead of failing the first user request.
 */
@Configuration
public class StartupConfig {
    @Bean
    static LazyInitializationExcludeFilter eagerFlyway() {
        return LazyInitializationExcludeFilter.forBeanTypes(Flyway.class, FlywayMigrationInitializer.class);
    }
}
