package com.example.gatherplan.appointment.repository;

import com.example.gatherplan.appointment.repository.entity.TempMemberAppointmentMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TempMemberAppointmentMappingRepository extends JpaRepository<TempMemberAppointmentMapping, Long> {
    Optional<TempMemberAppointmentMapping> findTempMemberAppointmentMappingByAppointmentSeq(Long appointmentId);
}
