package com.example.springsecurity.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.example.springsecurity.config",
        "com.example.springsecurity.web"
})
public class JwtLogin {
    public static void main(String[] args) {
        SpringApplication.run(JwtLogin.class, args);
    }
}
