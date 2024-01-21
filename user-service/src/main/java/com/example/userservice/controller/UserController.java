package com.example.userservice.controller;

import com.example.userservice.dto.UserDto;
import com.example.userservice.service.UserService;
import com.example.userservice.vo.Greeting;
import com.example.userservice.vo.RequestUser;
import com.example.userservice.vo.ResponseUser;
import io.micrometer.core.annotation.Timed;
import org.apache.catalina.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping
public class UserController {

    private final UserService userService;

//    private final Greeting greeting;
    private final Environment env;
    public UserController(Environment env, Greeting greeting, UserService userService) {
        this.env = env;
//        this.greeting = greeting;
        this.userService = userService;
    }

    @GetMapping("/health-check")
    @Timed(value = "users.status", longTask = true) //actuator metric, prometheus에서 모니터링 가능
    public String status(HttpServletRequest request){
        return "It's working in user service on Port : "
                + env.getProperty("local.server.port") + " from request : "
                + request.getLocalPort()
                + ", port(local.server.port) = " + env.getProperty("local.server.port")
                + ", port(server.port)=" + env.getProperty("server.port")
                + ", with token secret=" + env.getProperty("token.secret")
                + ", with token time=" + env.getProperty("token.expiration");
    }

    @GetMapping("/")
    @Timed(value = "users.welcome", longTask = true)
    public String welcome(){
        return env.getProperty("greeting.message");
    }

    @PostMapping("/user")
    public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser requestUser){
        UserDto userDto = new UserDto();
        userDto.setPwd(requestUser.getPwd());
        userDto.setName(requestUser.getName());
        userDto.setEmail(requestUser.getEmail());

        userService.createUser(userDto);

        ModelMapper mapper = new ModelMapper();
        ResponseUser responseUser = mapper.map(userDto, ResponseUser.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }

    @GetMapping("/user")
    @Timed(value = "users.findAllUsers", longTask = true)
    public ResponseEntity<List<ResponseUser>> findAllUsers(){
        ModelMapper mapper = new ModelMapper();
        List<ResponseUser> result = new ArrayList<>();
        List<UserDto> target = userService.findAllUser();
        target.stream().forEach(userDto -> {
            result.add(mapper.map(userDto , ResponseUser.class));
        });
        return ResponseEntity.status(200).body(result);
    }

    @GetMapping("/user/{userId}")
    @Timed(value = "users.findUser", longTask = true)
    public ResponseEntity<ResponseUser> findUser(@PathVariable String userId){
        UserDto user = userService.findUser(userId);
        ModelMapper mapper = new ModelMapper();
        ResponseUser result = mapper.map(user , ResponseUser.class);
        return ResponseEntity.status(200).body(result);
    }
}
