package com.example.configservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.eureka.EurekaClientConfigBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kyeongha55@linecorp.com on 2024/01/22
 */
@Configuration
public class EurekaConfig {
    @Value("${spring.profiles.active}")
    private String springProfileActive;
    @Bean
    public EurekaClientConfigBean eurekaClientConfigBean() {
        EurekaClientConfigBean client = new EurekaClientConfigBean();
        Map<String, String> serviceUrl = new HashMap<>();
        if(springProfileActive.equals("local")) {
            serviceUrl.put("defaultZone", "http://localhost:8000/eureka");
        } else {
            serviceUrl.put("defaultZone", "http://service-discovery:8000/eureka");
        }
        client.setServiceUrl(serviceUrl);
        return client;
    }
}