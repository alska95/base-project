package com.example.springcloudgateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class FilterConfig {
//    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder){
        return builder.routes()
                .route(r->r.path("/first-service/**")
                    .filters(f->f.addRequestHeader("first-request" , "first-request-header")
                        .addRequestHeader("first-response", "first-response-header"))
                    .uri("http://localhost:8001"))
                .route(r->r.path("/second-service/**")
                    .filters(f->f.addRequestHeader("second-request", "second-request-header")
                        .addRequestHeader("second-response", "second-response-header"))
                    .uri("http://localhost:8002"))
                .build();

        /*
        * 기본형
        * builder.routes()
        * .route()
        * .build();
        *
        * build.routes().
        * route(r->r.path().filter().uri()) r이라는 값에 path를 확인하고 필터를 적용해서 uri로 이동시켜준다.
        *
        * build.routes().
        * route(
        * r->r.path("/first-service/**)
        * .filter(f->f.addRequestHeader("key" , "value")
        * .addResponseHeader("key","value"))
        * .uri("http://localhost:8001"))
        * .build();
        * */

    }
}
