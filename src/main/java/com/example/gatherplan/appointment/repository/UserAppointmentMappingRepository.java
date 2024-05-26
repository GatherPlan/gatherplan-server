package com.example.gatherplan.appointment.repository;

import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.repository.entity.UserAppointmentMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserAppointmentMappingRepository extends JpaRepository<UserAppointmentMapping, Long> {

    List<UserAppointmentMapping> findAllByAppointmentSeqAndUserRole(Long userId, UserRole userRole);

    void deleteAllByAppointmentSeq(Long appointmentId);

    void deleteAllByAppointmentSeqAndUserRole(Long appointmentId, UserRole userRole);

    boolean existsByAppointmentSeqAndNicknameAndTempPasswordAndUserRole(Long appointmentId, String nickname, String tempPassword, UserRole userRole);

    boolean existsByAppointmentSeqAndUserSeqAndUserRole(Long appointmentId, Long userId, UserRole userRole);

    Optional<UserAppointmentMapping> findUserAppointmentMappingByAppointmentSeqAndNicknameAndUserRole(Long appointmentId, String nickname, UserRole userRole);
}
