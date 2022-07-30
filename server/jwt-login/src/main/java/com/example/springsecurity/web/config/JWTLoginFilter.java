package com.example.springsecurity.web.config;

import com.example.springsecurity.user.domain.SpUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {

    private ObjectMapper objectMapper = new ObjectMapper();

    public JWTLoginFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager); // AuthenticationManager 에게 유저 검증을 위임해버림
        setFilterProcessesUrl("/login");
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException
    {

        UserLoginForm userLogin = objectMapper.readValue(request.getInputStream(), UserLoginForm.class);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                 userLogin.getUsername(), userLogin.getPassword(), null
        );
        // 이후 과정은 user details 처리...
        return getAuthenticationManager().authenticate(token);
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain, Authentication authResult) throws IOException, ServletException
    {
        SpUser user = (SpUser) authResult.getPrincipal();

        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer "+JWTUtil.makeAuthToken(user));
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE); // json 으로 반환
        response.getOutputStream().write(objectMapper.writeValueAsBytes(user));
    }
}
