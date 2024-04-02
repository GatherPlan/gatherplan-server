package com.example.gatherplan.appointment.repository;

import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.repository.entity.UserAppointmentMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserAppointmentMappingRepository extends JpaRepository<UserAppointmentMapping, Long> {
    List<UserAppointmentMapping> findByAppointmentSeq(Long appointmentId);

    List<UserAppointmentMapping> findByUserSeq(Long userId);

    UserAppointmentMapping findByAppointmentSeqAndUserRole(Long appointmentId, UserRole userRole);

    Optional<UserAppointmentMapping> findByAppointmentSeqAndUserSeq(Long appointmentId, Long userId);


}
