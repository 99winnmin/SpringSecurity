package com.example.springsecurity.web.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.springsecurity.user.domain.SpUser;

import java.time.Instant;

public class JWTUtil { // JWT 토큰을 발행하고 verify 하는 util

    private static final Algorithm ALGORITHM = Algorithm.HMAC256("seungmin"); // 서명할 secret key 값
    private static final long AUTH_TIME = 1; // 1초
    private static final long REFRESH_TIME = 60*60*24*7; // 일주일

    public static String makeAuthToken(SpUser user){
        return JWT.create()
                .withSubject(user.getUsername())
                .withClaim("exp", Instant.now().getEpochSecond()+AUTH_TIME)
                .sign(ALGORITHM);
    }

    public static String makeRefreshToken(SpUser user){
        return JWT.create()
                .withSubject(user.getUsername())
                .withClaim("exp", Instant.now().getEpochSecond()+REFRESH_TIME)
                .sign(ALGORITHM);
    }

    public static VerifyResult verify(String token){ // 토큰을 verify 하는 메서드
        try {
            DecodedJWT verify = JWT.require(ALGORITHM).build().verify(token);
            return VerifyResult.builder().success(true)
                    .username(verify.getSubject()).build();
        } catch (Exception e) {
            DecodedJWT decode = JWT.decode(token);
            return VerifyResult.builder().success(false)
                    .username(decode.getSubject()).build();
        }
    }
}
