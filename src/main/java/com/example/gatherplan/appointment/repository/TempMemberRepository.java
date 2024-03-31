package com.example.gatherplan.appointment.repository;

import com.example.gatherplan.appointment.repository.entity.TempMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TempMemberRepository extends JpaRepository<TempMember, Long> {

    Optional<TempMember> findById(Long id);
}
