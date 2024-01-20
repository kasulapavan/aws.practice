package com.example.aws.demo.utils;

import com.example.aws.demo.dto.AppUserDto;
import com.example.aws.demo.entity.AppUser;

public class AppUserUtils {



    public static AppUser dtoToEntity(AppUserDto appUserDto){
        AppUser appUser = new AppUser();
        appUser.setName(appUserDto.getName());
        appUser.setEmail(appUserDto.getEmail());
        appUser.setPassword(appUserDto.getPassword());
        appUser.setRoleType(appUserDto.getRoleType());
        return appUser;
    }

    public AppUserDto entityToDto(AppUser appUser){
        AppUserDto appUserDto1 = new AppUserDto();
        appUserDto1.setName(appUser.getName());
        appUserDto1.setEmail(appUser.getEmail());
        appUserDto1.setPassword(appUser.getPassword());
        appUserDto1.setRoleType(appUser.getRoleType());
        return appUserDto1;
    }
}
