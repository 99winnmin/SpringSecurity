package com.example.springsecurity.web.controller;

import lombok.*;
import org.springframework.security.core.Authentication;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SecurityMessage {

    private Authentication auth;
    private String message;
}
