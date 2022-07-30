package com.example.springsecurity.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan("com.example.springsecurity.user")
@EntityScan(basePackages = {
        "com.sp.fc.user.domain"
})
@EnableJpaRepositories(basePackages = {
        "com.example.springsecurity.user.repository"
})
public class UserAdminModule {
}
