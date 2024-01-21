package com.example.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;


/**
 * 유레카는 서비스 디스커버리의 역할을 수행하며, 전화번호부와 같은 기능을 한다.
 * 유레카는 이름과 키 값이 포함된 상태 정보를 저장하고, 어떤 서버가 어떤 위치에 있는지 알려주는 역할을 한다.
 *
 * 각 서비스 정보는 유레카에 등록된다.
 * 클라이언트가 마이크로서비스를 사용하고자 할 때, 우선 클라이언트는 필요한 요청을 LB에 전달한다.
 * 그 다음 로드 밸런서는 서비스 디스커버리에 요청을 보낸다. 로드 밸런서는 호출해야 하는 서버에 대한 응답을 받아, 알맞은 서버로 요청을 전송한다.
 * 유레카를 통해 서버의 위치를 신속하게 찾아 요청을 처리하고, 서비스의 확장성과 가용성을 향상시킬 수 있다.
 *
 * 웹뷰도 제공해준다.
 * */
@SpringBootApplication
@EnableEurekaServer //디스커버리 서버 자격으로서 등록해준다.
public class EcommerceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommerceApplication.class, args);
    }

}
