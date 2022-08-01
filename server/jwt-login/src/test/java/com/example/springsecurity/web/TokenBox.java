package com.example.springsecurity.web;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenBox {

    private String authToken;
    private String refreshToken;
}
