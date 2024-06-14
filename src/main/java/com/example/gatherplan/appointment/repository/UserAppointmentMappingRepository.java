package com.example.gatherplan.appointment.repository;

import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.repository.entity.UserAppointmentMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserAppointmentMappingRepository extends JpaRepository<UserAppointmentMapping, Long> {


    Optional<UserAppointmentMapping> findByAppointmentCodeAndUserRole(String appointmentCode, UserRole userRole);

    Optional<UserAppointmentMapping> findByAppointmentCodeAndUserSeqAndUserRole(String appointmentCode, Long userId, UserRole userRole);


    Optional<UserAppointmentMapping> findByAppointmentCodeAndNicknameAndTempPasswordAndUserRole(String appointmentCode, String nickname, String tempPassword, UserRole userRole);

    List<UserAppointmentMapping> findAllByAppointmentCodeAndUserRole(String appointmentCode, UserRole userRole);

    List<UserAppointmentMapping> findAllByAppointmentCodeAndUserSeq(String appointmentCode, Long userId);

    List<UserAppointmentMapping> findAllByAppointmentCode(String appointmentCode);

    void deleteAllByAppointmentCode(String appointmentCode);

    void deleteAllByAppointmentCodeAndUserRole(String appointmentCode, UserRole userRole);

    boolean existsByAppointmentCodeAndUserSeqAndUserRole(String appointmentCode, Long userId, UserRole userRole);

    boolean existsByAppointmentCodeAndNicknameAndTempPasswordAndUserRole(String appointmentCode, String nickname, String tempPassword, UserRole userRole);

    Optional<UserAppointmentMapping> findUserAppointmentMappingByAppointmentCodeAndNicknameAndUserRole(String appointmentCode, String nickname, UserRole userRole);

    Optional<UserAppointmentMapping> findByAppointmentCodeAndNicknameAndUserRole(String appointmentCode, String nickname, UserRole userRole);
}
