package com.example.springsecurity.user.domain;

import org.springframework.security.core.GrantedAuthority;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="sp_user_authority")
@IdClass(SpAuthority.class) // 두 필드가 모두 unique해야함
public class SpAuthority implements GrantedAuthority {

    @Id
    private Long userId;

    @Id
    private String authority;

}
