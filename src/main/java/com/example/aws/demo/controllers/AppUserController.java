package com.example.aws.demo.controllers;

import com.example.aws.demo.dto.AppUserDto;
import com.example.aws.demo.service.AppUserService;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppUserController {


    @Autowired
    private AppUserService appUserService;

    @PostMapping("/signUp")
    public String signUp(AppUserDto appUserDto){
        return appUserService.signUp(appUserDto);
    }

    @PostMapping("/login-in")
    public String loginIn(AppUserDto appUserDto) throws JOSEException{
        return appUserService.loginIn(appUserDto);
    }
}
