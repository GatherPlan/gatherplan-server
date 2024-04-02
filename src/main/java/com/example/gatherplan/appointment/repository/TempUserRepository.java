package com.example.gatherplan.appointment.repository;

import com.example.gatherplan.appointment.repository.entity.TempUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TempUserRepository extends JpaRepository<TempUser, Long> {
}
