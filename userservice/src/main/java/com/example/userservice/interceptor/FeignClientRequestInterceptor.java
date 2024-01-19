package com.example.userservice.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Created by kyeongha55@linecorp.com on 2024/01/17
 */
public class FeignClientRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }
        String token = attributes.getRequest().getHeader(HttpHeaders.AUTHORIZATION);
        if (token != null) {
            template.header(HttpHeaders.AUTHORIZATION, token); //내부 호출 시에도 jwt토큰 같이 전달하도록 헤더 설정
        }
    }
}
