package com.example.gatherplan.appointment.repository;

import com.example.gatherplan.appointment.repository.entity.EmailAuth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailAuthRepository extends JpaRepository<EmailAuth, Long> {

    void deleteByEmail(String email);

    Optional<EmailAuth> findByEmail(String email);
}
