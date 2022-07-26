package com.example.springsecurity.web.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;

@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomAuthDetails customAuthDetails;

    public SecurityConfig(CustomAuthDetails customAuthDetails) {
        this.customAuthDetails = customAuthDetails;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication() // 해당 설정을 하게되면 .yaml의 user는 더이상 접속 불가
                .withUser(User.withDefaultPasswordEncoder()
                        .username("user1")
                        .password("1111")
                        .roles("USER")
                )
                .withUser(User.withDefaultPasswordEncoder()
                        .username("admin")
                        .password("2222")
                        .roles("ADMIN")
                );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests(request->{
                    request
                            .antMatchers("/").permitAll() // 메인화면은 권한 필요x
                            .anyRequest().authenticated();
                })
                .formLogin(// 기본 로그인/로그아웃 화면 나오게 해줌
                        login->login.loginPage("/login")
                                .permitAll() // 로그인화면을 만든 것으로 바꿈
                                .defaultSuccessUrl("/",false) // 로그인하고 메인 페이지로 안가겠다
                                .failureUrl("/login-error")
                                .authenticationDetailsSource(customAuthDetails) // 필요에 따라서 유용하게 쓰일 수 있음
                )
                .logout(logout -> logout.logoutSuccessUrl("/"))
                .exceptionHandling(exception -> exception.accessDeniedPage("/access-denied"))
        ;
    }

    @Bean
    RoleHierarchy roleHierarchy(){ // admin은 유저가 할 수 있는 기능을 다 할 수 있음
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");
        return roleHierarchy;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .requestMatchers( // resource 파일들은 일단 적용되게 해주는 메서드
                        PathRequest.toStaticResources().atCommonLocations()
                );
    }
}