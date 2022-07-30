package com.example.springsecurity.web.config;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerifyResult {

    private boolean success;
    private String username;
}
