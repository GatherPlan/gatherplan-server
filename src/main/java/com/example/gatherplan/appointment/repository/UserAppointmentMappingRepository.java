package com.example.gatherplan.appointment.repository;

import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.repository.entity.UserAppointmentMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserAppointmentMappingRepository extends JpaRepository<UserAppointmentMapping, Long> {

    List<UserAppointmentMapping> findByUserSeq(Long userId);

    List<UserAppointmentMapping> findAllByAppointmentSeq(Long appointmentId);

    Optional<UserAppointmentMapping> findByAppointmentSeqAndUserRole(Long appointmentId, UserRole userRole);

    List<UserAppointmentMapping> findAllByAppointmentSeqAndUserRole(Long appointmentId, UserRole userRole);

    Optional<UserAppointmentMapping> findByAppointmentSeqAndUserSeqAndUserRole(
            Long appointmentId, Long userId, UserRole userRole);
}
