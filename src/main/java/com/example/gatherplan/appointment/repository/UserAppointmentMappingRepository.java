package com.example.gatherplan.appointment.repository;

import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.repository.entity.UserAppointmentMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAppointmentMappingRepository extends JpaRepository<UserAppointmentMapping, Long> {

    void deleteAllByAppointmentSeq(Long appointmentId);

    Optional<UserAppointmentMapping> findUserAppointmentMappingByAppointmentSeqAndUserSeqAndUserRole(
            Long appointmentId, Long userId, UserRole userRole);

    Optional<UserAppointmentMapping> findByAppointmentSeqAndNicknameAndTempPasswordAndUserRole(
            Long appointmentId, String nickname, String tempPassword, UserRole userRole
    );
}
