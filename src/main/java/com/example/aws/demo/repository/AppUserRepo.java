package com.example.aws.demo.repository;

import com.example.aws.demo.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepo extends JpaRepository<AppUser,Long> {
    AppUser findByEmail(String username);
}
