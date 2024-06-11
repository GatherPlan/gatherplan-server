package com.example.gatherplan.appointment.repository;

import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.repository.entity.UserAppointmentMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserAppointmentMappingRepository extends JpaRepository<UserAppointmentMapping, Long> {

    Optional<UserAppointmentMapping> findByAppointmentCodeAndUserRole(String appointmentCode, UserRole userRole);

    Optional<UserAppointmentMapping> findByAppointmentCodeAndUserRoleIn(String appointmentCode, Collection<UserRole> userRoles);

    Optional<UserAppointmentMapping> findByAppointmentCodeAndUserSeqAndUserRoleIn(String appointmentCode, Long userId, Collection<UserRole> userRoles);

    Optional<UserAppointmentMapping> findByAppointmentCodeAndNicknameAndTempPasswordAndUserRoleIn(String appointmentCode, String nickname, String tempPassword, Collection<UserRole> userRoles);

    List<UserAppointmentMapping> findAllByAppointmentCodeAndUserRoleIn(String appointmentCode, Collection<UserRole> userRoles);

    void deleteAllByAppointmentCode(String appointmentCode);

    void deleteAllByAppointmentCodeAndUserRole(String appointmentCode, UserRole userRole);

    boolean existsByAppointmentCodeAndNicknameAndTempPasswordAndUserRoleIn(String appointmentCode, String nickname, String tempPassword, Collection<UserRole> userRoles);

    boolean existsByAppointmentCodeAndUserSeqAndUserRoleIn(String appointmentCode, Long userId, Collection<UserRole> userRoles);

    Optional<UserAppointmentMapping> findUserAppointmentMappingByAppointmentCodeAndNicknameAndUserRoleIn(String appointmentCode, String nickname, Collection<UserRole> userRoles);

}
