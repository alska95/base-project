package com.example.catalogservice.controller;

import com.example.catalogservice.domain.CatalogEntity;
import com.example.catalogservice.dto.CatalogDto;
import com.example.catalogservice.service.CatalogService;
import com.example.catalogservice.vo.ResponseCatalog;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/catalog-service")
public class CatalogController {
    Environment env;
    CatalogService catalogService;

    public CatalogController(Environment env, CatalogService catalogService) {
        this.env = env;
        this.catalogService = catalogService;
    }

    @GetMapping("/health-check")
    public String status(HttpServletRequest request){
        return "It's working in user service on Port : " + env.getProperty("local.server.port") + " from request : " + request.getLocalPort();
    }

    @GetMapping("/welcome")
    public String welcome(){
        return env.getProperty("greeting.message");
    }

    @GetMapping("/user")
    public ResponseEntity<List<ResponseCatalog>> findAllUsers(){
        ModelMapper mapper = new ModelMapper();
        List<ResponseCatalog> result = new ArrayList<>();
        List<CatalogEntity> target = catalogService.getAllCategories();
        target.stream().forEach(userDto -> {
            result.add(mapper.map(userDto , ResponseCatalog.class));
        });
        return ResponseEntity.status(200).body(result);
    }

}
