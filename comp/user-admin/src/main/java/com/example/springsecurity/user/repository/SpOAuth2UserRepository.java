package com.example.springsecurity.user.repository;

import com.example.springsecurity.user.domain.SpOAuth2User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpOAuth2UserRepository extends JpaRepository<SpOAuth2User, String> {
}
