package com.example.aws.demo.configuration;

import com.example.aws.demo.repository.AppUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class WedSecurityConfig {


    @Autowired
    JwtTokenUtils jwtTokenUtils;

    @Autowired
    AppUserRepo appUserRepository;

    private String[] PUBLIC_RESOURCE_AND_URL = {"/",
            "/signUp",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v3/api-docs/**",
            "/v2/api-docs/**",
            "/api-docs/**",
            "/login-in"

    };

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {

        http.csrf()
                .disable()
                .authorizeHttpRequests()
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler()).and().addFilterBefore(
                        new JwtAuthenticationFilter(jwtTokenUtils, appUserRepository), BasicAuthenticationFilter.class);
//                addFilterBefore(new CustomCORSFilter(), ChannelProcessingFilter.class);

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.debug(false)
                .ignoring()
                .requestMatchers(PUBLIC_RESOURCE_AND_URL);
    }
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }
}
