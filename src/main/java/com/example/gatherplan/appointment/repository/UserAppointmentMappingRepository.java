package com.example.gatherplan.appointment.repository;

import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.repository.entity.UserAppointmentMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserAppointmentMappingRepository extends JpaRepository<UserAppointmentMapping, Long> {
    Optional<UserAppointmentMapping> findByAppointmentCodeAndNicknameAndTempPassword(String appointmentCode, String nickname, String tempPassword);

    Optional<UserAppointmentMapping> findByAppointmentCodeAndNicknameAndTempPasswordAndUserRole(String appointmentCode, String nickname, String tempPassword, UserRole userRole);

    Optional<UserAppointmentMapping> findByAppointmentCodeAndUserSeqAndUserRole(String appointmentCode, Long userSeq, UserRole userRole);

    List<UserAppointmentMapping> findAllByAppointmentCodeAndUserRole(String appointmentCode, UserRole userRole);

    List<UserAppointmentMapping> findAllByAppointmentCode(String appointmentCode);

    void deleteAllByAppointmentCode(String appointmentCode);

    void deleteAllByAppointmentCodeAndUserRole(String appointmentCode, UserRole userRole);

    List<UserAppointmentMapping> findAllByUserSeq(Long userId);

    List<UserAppointmentMapping> findByAppointmentCodeInAndUserRole(List<String> appointmentCodeList, UserRole userRole);
}