package com.example.aws.demo.entity;

import com.example.aws.demo.enums.RoleType;
//import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;



@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUser extends BaseEntity {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
//    private Long id;
    private String name;
    private String email;
    private  String password;
    private RoleType roleType;


}
