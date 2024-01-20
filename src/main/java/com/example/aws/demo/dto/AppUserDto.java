package com.example.aws.demo.dto;

import com.example.aws.demo.enums.RoleType;
import lombok.Data;

@Data
public class AppUserDto {

    private String name;
    private String email;
    private  String password;
    private RoleType roleType;
}
