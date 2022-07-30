package com.example.springsecurity.web.config;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoginForm {

    private String username;
    private String password;
}
