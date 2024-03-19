package com.example.gatherplan.appointment.repository;

import com.example.gatherplan.appointment.repository.entity.EmailAuth;

import java.util.Optional;

public interface EmailAuthRepository {

    void save(EmailAuth emailAuth);

    void deleteById(Long id);

    void deleteByEmail(String email);

    Optional<EmailAuth> findEmailAuthByEmail(String email);
}
