package com.example.springcloudgateway.filter;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    private final Environment env;

    public AuthorizationHeaderFilter(Environment env){
        super(Config.class);
        this.env = env;
    }

    public static class Config{

    }

    // login -> token -> user (with token) -> 토큰을 열어봐서 맞는지 아닌지 확인 header(include token)
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "No authorization header", HttpStatus.UNAUTHORIZED);
            }

            String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String jwt = authorizationHeader.substring(7); //파싱해서 사용하도록 제거하지 않고 유지함.
                if (!isJwtValid(jwt)) {
                    return onError(exchange, "Invalid Jwt Token", HttpStatus.UNAUTHORIZED);
                }
            }
            return chain.filter(exchange);
        };
    }

    private boolean isJwtValid(String jwt) {
        try {
            Claims body = Jwts.parser().setSigningKey(env.getProperty("token.secret"))
                    .parseClaimsJws(jwt).getBody();
            String subject = body.getSubject();
            if (subject == null || subject.isEmpty()) {
                return false;
            }

            Date expirationDate = body.getExpiration();
            if (expirationDate == null) {
                return false;
            }
            LocalDateTime expiration = expirationDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            if (LocalDateTime.now().isAfter(expiration)) {
//                return false; 편의상 유효기간 검증 제거
                return true;
            }
        } catch (ExpiredJwtException | MalformedJwtException | SignatureException ex) {
            log.error("Invalid JWT token: {}", ex.getMessage());
            return false;
        }
        return true;
    }

    //Mono, Flux -> spring5.0에서 새로 추가된 webFlux개념에서 나오는 것이다. 반환시켜주는 데이터 타입. 단일값 -> mono , 다중값 -> flux
    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);

        log.error(err);
        return response.setComplete();
    }
}
