package com.sinchan.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // enable Spring Security's web security support
@EnableMethodSecurity // allows method-level security using annotations like @PreAuthorize
public class ApiSecurityConfigs {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .authorizeHttpRequests(request -> {
                    request.requestMatchers("/login").permitAll()
                            .anyRequest().authenticated();
                })
                .csrf((csrf) -> csrf.disable())
                .httpBasic(Customizer.withDefaults())
                .build();
    }
}
