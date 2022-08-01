package com.example.springsecurity.web.config;

import com.example.springsecurity.user.domain.SpOAuth2User;
import com.example.springsecurity.user.domain.SpUser;
import com.example.springsecurity.user.service.SpUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SpOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private SpUserService userService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException
    {
        Object principal = authentication.getPrincipal();
        if(principal instanceof OidcUser){
            // google
            SpOAuth2User oauth = SpOAuth2User.Provider.google.convert((OAuth2User) principal);
            SpUser user = userService.load(oauth);
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities())
            );
        }else if(principal instanceof OAuth2User){
            // naver
            SpOAuth2User oauth = SpOAuth2User.Provider.google.convert((OAuth2User) principal);
            SpUser user = userService.load(oauth);
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities())
            );
        }
        request.getRequestDispatcher("/").forward(request, response);
    }
}
