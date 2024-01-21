package com.example.configservice.config;

import org.springframework.cloud.config.server.environment.NativeEnvironmentProperties;
import org.springframework.cloud.config.server.environment.NativeEnvironmentRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * Created by kyeongha55@linecorp.com on 2024/01/21
 */
@Configuration
@Profile({"local", "alpha"})
public class LocalRepositoryConfiguration {
    @Bean
    public NativeEnvironmentRepository nativeEnvironmentRepository(ConfigurableEnvironment environment, NativeEnvironmentProperties properties) {
        return new NativeEnvironmentRepository(environment, properties);
    }
}
