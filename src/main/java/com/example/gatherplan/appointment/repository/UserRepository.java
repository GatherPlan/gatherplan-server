package com.example.gatherplan.appointment.repository;

import com.example.gatherplan.appointment.repository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    boolean existsByKakaoId(String kakaoId);

    Optional<User> findByKakaoId(String kakaoId);
}
