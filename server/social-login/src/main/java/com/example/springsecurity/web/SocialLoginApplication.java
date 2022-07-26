package com.example.springsecurity.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;

@SpringBootApplication(scanBasePackages = {
        "com.example.springsecurity.web",
        "com.example.springsecurity.config"
})
public class SocialLoginApplication {

    CommonOAuth2Provider commonOAuth2Provider;

    public static void main(String[] args) {
        SpringApplication.run(SocialLoginApplication.class, args);
    }
}
