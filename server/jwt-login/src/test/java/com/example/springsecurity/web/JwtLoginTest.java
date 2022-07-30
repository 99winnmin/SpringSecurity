package com.example.springsecurity.web;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.*;

import javax.xml.bind.DatatypeConverter;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JwtLoginTest {

    private void printToken(String token){
        String[] tokens = token.split("\\.");
        System.out.println("header: "+new String(Base64.getDecoder().decode(tokens[0])));
        System.out.println("body: "+new String(Base64.getDecoder().decode(tokens[1])));
    }

    @DisplayName("1. jjwt 를 이용한 토큰 테스트")
    @Test
    void test_1(){
        String okta_token = Jwts.builder().addClaims(
                Map.of("name","seungmin","price",3000)
                ).signWith(SignatureAlgorithm.HS256, "seungmin") // key가 너무 짧으면 안된다...
                .compact();
        System.out.println(okta_token);
        printToken(okta_token);

        Jws<Claims> tokenInfo = Jwts.parser().setSigningKey("seungmin").parseClaimsJws(okta_token);
        System.out.println(tokenInfo);
    }

    @DisplayName("2. java-jwt 를 이용한 토큰 테스트")
    @Test
    void test_2(){

        byte[] SEC_KEY = DatatypeConverter.parseBase64Binary("seungmin");

        String oauth0_token = JWT.create().withClaim("name", "seungmin").withClaim("price", 3000)
                .sign(Algorithm.HMAC256(SEC_KEY));
        System.out.println(oauth0_token);
        printToken(oauth0_token);

        DecodedJWT verifier = JWT.require(Algorithm.HMAC256(SEC_KEY)).build().verify(oauth0_token);
        System.out.println(verifier.getClaims());

        Jws<Claims> tokenInfo = Jwts.parser().setSigningKey(SEC_KEY).parseClaimsJws(oauth0_token);
        System.out.println(tokenInfo);
        // okta와 oauth는 비밀키를 관리하는 방식이 다른데 같은 방식으로 key를 만들어서 서명하면 다른 라이브러리를 동시에 사용할 수 있음
    }

    @DisplayName("3. 만료 시간 테스트")
    @Test
    void test_3() throws InterruptedException {
        final Algorithm AL = Algorithm.HMAC256(("seungmin"));
        String token = JWT.create().withSubject("ojysep")
                .withNotBefore(new Date(System.currentTimeMillis() + 1000)) // 토큰 유효 시작 시간
                .withExpiresAt(new Date(System.currentTimeMillis() + 3000)) // 토큰 유효기간
                .sign(AL);

//        Thread.sleep(2000);
        try {
            DecodedJWT verify = JWT.require(AL).build().verify(token);
            System.out.println(verify.getClaims());
        }catch(Exception e){
            System.out.println("유효하지 않은 토큰입니다...");
            DecodedJWT decode = JWT.decode(token);
            System.out.println(decode.getClaims());
        }
    }
}