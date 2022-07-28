package com.example.springsecurity.web.teacher;

import com.example.springsecurity.web.Student.Student;
import com.example.springsecurity.web.Student.StudentAuthenticationToken;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Set;

@Component
public class TeacherManager implements AuthenticationProvider, InitializingBean {
    // 통행증을 다룰 객체(통행증을 발급해주는 역할)
    private HashMap<String, Teacher> teacherDB = new HashMap<>();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        TeacherAuthenticationToken token = (TeacherAuthenticationToken) authentication;
        if(teacherDB.containsKey(token.getCredentials())){
            Teacher teacher = teacherDB.get(token.getCredentials());
            return TeacherAuthenticationToken.builder()
                    .principal(teacher)
                    .details(teacher.getUsername())
                    .authenticated(true)
                    .build();
        }
        // 처리할 수 없는 token을 false로해서 넘기면 handling했다는 것이 때문에 문제가 됨
        return null; // 처리할 수 없는 Authentication은 null로 넘김
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // UsernamePasswordAuthenticationFilter가 Token을 발행해서 인증을 해줄 provider를 찾는데
        // authentication이 그 토큰이라면?
        // 해당 메서드로 provider로 동작을 하겠다!라고 선언
        // 즉 인증을 위임하겠다!
        return authentication == TeacherAuthenticationToken.class;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Set.of(
                new Teacher("ryuT","류선생", Set.of(new SimpleGrantedAuthority("ROLE_TEACHER"))),
                new Teacher("leeT","이선생", Set.of(new SimpleGrantedAuthority("ROLE_TEACHER"))),
                new Teacher("parkT","박선생", Set.of(new SimpleGrantedAuthority("ROLE_TEACHER")))
        ).forEach(t ->
                teacherDB.put(t.getId(),t));
    }
}
