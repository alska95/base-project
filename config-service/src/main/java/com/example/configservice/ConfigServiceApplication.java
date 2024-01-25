package com.example.configservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

//Config서버를 사용하면 설정이 변경되어도 서비스를 다시 빌드하지 않고 적용이 가능하다.
@EnableConfigServer
@SpringBootApplication
//@EnableEurekaClient 도커 스웜으로 대체
public class ConfigServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigServiceApplication.class, args);
    }

}
