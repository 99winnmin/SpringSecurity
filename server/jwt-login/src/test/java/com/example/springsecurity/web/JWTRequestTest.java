package com.example.springsecurity.web;

import com.example.springsecurity.user.domain.SpUser;
import com.example.springsecurity.user.repository.SpUserRepository;
import com.example.springsecurity.user.service.SpUserService;
import com.example.springsecurity.web.config.UserLoginForm;
import com.example.springsecurity.web.test.WebIntegrationTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JWTRequestTest extends WebIntegrationTest {

    private SpUserRepository userRepository;
    @Autowired
    private SpUserService userService;

    @BeforeEach
    void before(){
//        userRepository.deleteAll();

        SpUser user = userService.save(SpUser.builder()
                .email("user1")
                .password("1111")
                .enabled(true)
                .build());
        userService.addAuthority(user.getUserId(),"ROLE_USER");
    }

    private TokenBox getToken(){
        RestTemplate client = new RestTemplate();

        HttpEntity<UserLoginForm> body = new HttpEntity<>( // 모바일 환경에서 username 과 password 를 쳤음(POST)
                UserLoginForm.builder().username("user1").password("1111").build()
        );

        ResponseEntity<SpUser> resp1 = client.exchange(uri("/login"), HttpMethod.POST, body, SpUser.class);
        return TokenBox.builder().authToken(resp1.getHeaders().get("auth_token").get(0))
                .refreshToken(resp1.getHeaders().get("refresh_token").get(0))
                .build();
    }

    private TokenBox getRefreshToken(String refreshToken){
        RestTemplate client = new RestTemplate();

        HttpEntity<UserLoginForm> body = new HttpEntity<>( // 모바일 환경에서 username 과 password 를 쳤음(POST)
                UserLoginForm.builder().refreshToken(refreshToken).build()
        );

        ResponseEntity<SpUser> resp1 = client.exchange(uri("/login"), HttpMethod.POST, body, SpUser.class);
        return TokenBox.builder().authToken(resp1.getHeaders().get("auth_token").get(0))
                .refreshToken(resp1.getHeaders().get("refresh_token").get(0))
                .build();
    }

    @DisplayName("1. hello 메세지를 받아온다...")
    @Test
    void test_1(){
        TokenBox token = getToken();

        RestTemplate client = new RestTemplate();

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.AUTHORIZATION, "Bearer "+token.getAuthToken());
        HttpEntity<Object> body = new HttpEntity<>(null, header);
        // JWT 가지고 /greeting url 실행
        ResponseEntity<String> resp2 = client.exchange(uri("/greeting"), HttpMethod.GET, body, String.class);

        System.out.println(resp2);
        assertEquals("hello",resp2.getBody());
    }

    @DisplayName("2. 토큰 만료 테스트")
    @Test
    void test_2() throws InterruptedException {
        TokenBox token = getToken();

        Thread.sleep(3000);
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.AUTHORIZATION, "Bearer "+token.getAuthToken());

        RestTemplate client = new RestTemplate();
        assertThrows(Exception.class, () -> {
            HttpEntity<Object> body = new HttpEntity<>(null, header);
            // JWT 가지고 /greeting url 실행
            ResponseEntity<String> resp2 = client.exchange(uri("/greeting"), HttpMethod.GET, body, String.class);
        });

        token = getRefreshToken(token.getRefreshToken());
        // token 을 다시 받아와서 request 날리기
        HttpHeaders header2 = new HttpHeaders();
        header2.add(HttpHeaders.AUTHORIZATION, "Bearer "+token.getAuthToken());
        HttpEntity<Object> body = new HttpEntity<>(null, header2);
        ResponseEntity<String> resp3 = client.exchange(uri("/greeting"), HttpMethod.GET, body, String.class);

        assertEquals("hello",resp3.getBody());
    }
}
