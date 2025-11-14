package com.gasmapp.gasmapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()          // desativa proteção CSRF, útil para APIs REST
                .authorizeHttpRequests()
                .anyRequest().permitAll(); // permite que qualquer usuário acesse todos os endpoints

        return http.build();
    }
}
