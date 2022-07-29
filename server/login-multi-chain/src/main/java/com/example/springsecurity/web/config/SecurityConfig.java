package com.example.springsecurity.web.config;

import com.example.springsecurity.web.Student.StudentManager;
import com.example.springsecurity.web.teacher.TeacherManager;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Order(2)
@EnableWebSecurity(debug = false)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final StudentManager studentManager;
    private final TeacherManager teacherManager;

    public SecurityConfig(StudentManager studentManager, TeacherManager teacherManager) {
        this.studentManager = studentManager;
        this.teacherManager = teacherManager;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(studentManager); // studentManager를 provider로 지정
        auth.authenticationProvider(teacherManager); // teacherManager를 provider로 지정
        // authenticationProvider는 manager를 쌓아둘 수 있음
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomLoginFilter filter = new CustomLoginFilter(authenticationManager());
        http
                .csrf().disable()
                .authorizeRequests(request->
                        request.antMatchers("/","/login").permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(
                        // 로그인 페이지를 통해 UsernamePasswordAuthenticationFilter가 동작
                        // UsernamePasswordAuthenticationFilter 는 UsernamePasswordAuthenticationToken을 발행
                        // UsernamePasswordAuthenticationToken을 대상으로한 StudentManager가 동작
                        login -> login.loginPage("/login")
                                .permitAll()
                                .defaultSuccessUrl("/",false)
                                .failureUrl("/login-error")
                )
                .addFilterAt(filter, UsernamePasswordAuthenticationFilter.class) // 내가 커스텀한 filter
                .logout(logout -> logout.logoutSuccessUrl("/"))
                .exceptionHandling(e -> e.accessDeniedPage("/access-denied"));
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
}
