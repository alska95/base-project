package com.example.userservice.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

//스프링 security에서 login api제공
@Data
public class RequestLogin {

    @NotNull(message = "Email cannot be null" )
    @Size(min = 2 , message = "Email should be longer than 2")
    private String email;

    @NotNull(message = "password cannot be null")
    @Size(min = 8 , message = "Password must be longer than 8")
    @JsonProperty("pwd")
    private String password;

}
