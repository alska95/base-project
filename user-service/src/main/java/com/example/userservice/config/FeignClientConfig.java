package com.example.userservice.config;

import com.example.userservice.interceptor.FeignClientRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by kyeongha55@linecorp.com on 2024/01/17
 */
@Configuration
public class FeignClientConfig {
    @Bean
    public FeignClientRequestInterceptor feignClientRequestInterceptor() {
        return new FeignClientRequestInterceptor();
    }
}
