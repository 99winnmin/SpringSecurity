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
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @DisplayName("1. hello 메세지를 받아온다...")
    @Test
    void test_1(){

        RestTemplate client = new RestTemplate();

        HttpEntity<UserLoginForm> body = new HttpEntity<>( // 모바일 환경에서 username 과 password 를 쳤음(POST)
                UserLoginForm.builder().username("user1").password("1111").build()
        );

        ResponseEntity<SpUser> resp1 = client.exchange(uri("/login"), HttpMethod.POST, body, SpUser.class);
        System.out.println(resp1.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0));
        System.out.println(resp1.getBody());

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.AUTHORIZATION, resp1.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0));
        body = new HttpEntity<>(null, header);
        // JWT 가지고 /greeting url 실행
        ResponseEntity<String> resp2 = client.exchange(uri("/greeting"), HttpMethod.GET, body, String.class);

        System.out.println(resp2);
        assertEquals("hello",resp2.getBody());
    }
}
