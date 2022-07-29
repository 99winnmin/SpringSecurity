package com.example.springsecurity.web.Student;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class StudentManager implements AuthenticationProvider, InitializingBean {
    // 통행증을 다룰 객체(통행증을 발급해주는 역할)
    // StudentManager가 AuthenticationProvider가 되어서 존재하는 id가 들어오면
    // Student객체가 담긴 AuthenticationToken(통행증)을 발급하게 됨
    private HashMap<String, Student> studentDB = new HashMap<>();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication instanceof UsernamePasswordAuthenticationToken){
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
            if(studentDB.containsKey(token.getName())){
                return getAuthenticationToken(token.getName());
            }
            return null;
        }
        StudentAuthenticationToken token = (StudentAuthenticationToken) authentication;
        if(studentDB.containsKey(token.getCredentials())){
            return getAuthenticationToken(token.getCredentials());
        }
        // 처리할 수 없는 token을 false로해서 넘기면 handling했다는 것이 때문에 문제가 됨
        return null; // 처리할 수 없는 Authentication은 null로 넘김
    }

    private StudentAuthenticationToken getAuthenticationToken(String id) {
        Student student = studentDB.get(id);
        return StudentAuthenticationToken.builder()
                .principal(student)
                .details(student.getUsername())
                .authenticated(true)
                .build();
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // UsernamePasswordAuthenticationFilter가 Token을 발행해서 인증을 해줄 provider를 찾는데
        // authentication이 그 토큰이라면?
        // 해당 메서드로 provider로 동작을 하겠다!라고 선언
        // 즉 인증을 위임하겠다!
        return authentication == StudentAuthenticationToken.class ||
                authentication == UsernamePasswordAuthenticationToken.class;
    }

    public List<Student> myStudentList(String teacherId){
        return studentDB.values().stream().filter(s -> s.getTeacherId().equals(teacherId))
                .collect(Collectors.toList());
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        Set.of(
                new Student("ryu","류승민", Set.of(new SimpleGrantedAuthority("ROLE_STUDENT")), "ryuT"),
                new Student("lee","이준규", Set.of(new SimpleGrantedAuthority("ROLE_STUDENT")), "ryuT"),
                new Student("park","박성규", Set.of(new SimpleGrantedAuthority("ROLE_STUDENT")), "ryuT")
        ).forEach(s ->
                studentDB.put(s.getId(),s));
    }
}
