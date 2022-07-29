package com.example.springsecurity.web.teacher;

import com.example.springsecurity.web.Student.Student;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Teacher {

    private String id;
    private String username;

    @JsonIgnore
    private Set<GrantedAuthority> role; // domain의 Principal

    private List<Student> studentList;

}
