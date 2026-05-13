package com.securitylab.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TestController {


    @GetMapping("/me")
    public String me(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("[Controller] name:{}",authentication.getName());
        log.info("[Controller] name:{}",authentication.getAuthorities());
        log.info("[Controller] name:{}",authentication.getClass().getName());

        return authentication.getName();

    }
}
