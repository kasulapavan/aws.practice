package com.example.aws.demo.service;

import com.example.aws.demo.dto.AppUserDto;
import com.example.aws.demo.entity.AppUser;
import com.nimbusds.jose.JOSEException;

public interface AppUserService {

    String signUp(AppUserDto appUserDto);

    String loginIn(AppUserDto appUserDto) throws JOSEException;
}
