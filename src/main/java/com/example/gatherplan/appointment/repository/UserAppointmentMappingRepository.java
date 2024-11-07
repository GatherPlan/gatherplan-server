package com.example.gatherplan.appointment.repository;

import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.repository.entity.UserAppointmentMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAppointmentMappingRepository extends JpaRepository<UserAppointmentMapping, Long> {

    List<UserAppointmentMapping> findByAppointmentCodeInAndUserRole(List<String> appointmentCodeList, UserRole userRole);

    List<UserAppointmentMapping> findAllByUserSeqAndUserRole(Long userSeq, UserRole userRole);

    void deleteAllByUserSeqAndUserRole(Long userSeq, UserRole userRole);

    List<UserAppointmentMapping> findAllByAppointmentCodeAndUserRole(String appointmentCode, UserRole userRole);

    List<UserAppointmentMapping> findAllByAppointmentCode(String appointmentCode);

    void deleteAllByAppointmentCode(String appointmentCode);

    void deleteAllByAppointmentCodeAndUserRole(String appointmentCode, UserRole userRole);

    void deleteAllByAppointmentCodeIn(List<String> appointmentCodeList);
}