package com.example.userservice.service;

import com.example.userservice.client.OrderServiceClient;
import com.example.userservice.domain.UserEntity;
import com.example.userservice.dto.UserDto;
import com.example.userservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    final UserRepository userRepository;
    final BCryptPasswordEncoder passwordEncoder;
    /**
     * PropertySourceBootstrapConfiguration의 insertPropertySources에서 springCloudConfig에서 가져온 설정이 environment에 세팅됨.
     * 이렇게 세팅된 environment는 AbstractApplicationContext에서 아래 코드로 등록됨.
     *  if (!beanFactory.containsLocalBean("environment")) {
     *             beanFactory.registerSingleton("environment", this.getEnvironment());
     *         }
     * */
    final Environment env;
    final RestTemplate restTemplate;
    final OrderServiceClient orderServiceClient;
    final CircuitBreakerFactory circuitBreakerFactory;
    final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, Environment env, RestTemplate restTemplate, OrderServiceClient orderServiceClient, CircuitBreakerFactory circuitBreakerFactory, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.env = env;
        this.restTemplate = restTemplate;
        this.orderServiceClient = orderServiceClient;
        this.circuitBreakerFactory = circuitBreakerFactory;
        this.modelMapper = modelMapper;
    }

    @Transactional
    @Override
    public UserEntity createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = mapper.map(userDto, UserEntity.class);
        userEntity.setEncryptedPwd(passwordEncoder.encode(userDto.getPwd()));


        log.info(userEntity.getUserId());
        log.info(userEntity.getEmail());
        log.info(userEntity.getEncryptedPwd());
        log.info(userEntity.getName());
        userRepository.save(userEntity);

        return userEntity;
    }

    public List<UserDto> findAllUser(){
        List<UserDto> result = new ArrayList<>();
        ModelMapper mapper = new ModelMapper();
        userRepository.findAll().stream().forEach(user ->{
            UserDto target = mapper.map(user , UserDto.class);
            result.add(target);
        });
        return result;
    }

    public UserDto findUser(String userId){
        CircuitBreaker orderCircuitBreaker = circuitBreakerFactory.create("orderCircuitBreaker");
        UserDto result = userRepository.findByUserId(userId).map(userEntity -> modelMapper.map(userEntity, UserDto.class))
                .orElseThrow(() -> new IllegalArgumentException("[UserService] user not exist. UID: " + userId));
//        String orderUrl = String.format(env.getProperty("order_service.url"), userId);

        /** restTemplate을 사용해서 서비스간 통신을 하는 방법*/
//        ResponseEntity<List<ResponseOrder>> orderListResponse =
//            restTemplate.exchange(orderUrl, HttpMethod.GET, null
//                    , new ParameterizedTypeReference<List<ResponseOrder>>() {
//                    });
//        List<ResponseOrder> orderList = orderListResponse.getBody();

        /** feignClient를 사용해서 서비스간 통신을 하는 방법법*/
//        List<ResponseOrder> orderList = orderServiceClient.getOrders(userId);

        /** feign Exception Handling*/

        /** FeignErrorDecoder이 대신해줌*/
//        List<ResponseOrder> orderList = null;
//        try{
//            orderList = orderServiceClient.getOrders(userId);
//        }catch (FeignException ex){
//            log.error(ex.getMessage());
//        }

        log.info("[UserService] Before call orderService");
        //CircuitBreaker사용해서 orderService에서 요청을 처리할 수 없는 경우 요청을 보내지 않고 List.of()를 반환함.
        result.setOrders(orderCircuitBreaker.run(() -> orderServiceClient.getOrders(userId), throwable -> List.of()));
        log.info("[UserService] After call orderService");
        return result;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(s).orElseThrow(() -> new UsernameNotFoundException(s));

        return new User(userEntity.getEmail() , userEntity.getEncryptedPwd(),
                true, true,true, true,
                new ArrayList<>());
    }

    @Override
    public UserDto getUserByEmail(String userName) {
        UserEntity byEmail = userRepository.findByEmail(userName).orElseThrow(() -> new UsernameNotFoundException(userName));
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper.map(byEmail, UserDto.class);
    }
}
