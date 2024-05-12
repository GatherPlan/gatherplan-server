package com.example.gatherplan.appointment.repository;

import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.repository.entity.Appointment;

import java.util.List;
import java.util.Optional;

public interface CustomAppointmentRepository {

    Optional<Appointment> findByAppointmentCodeAndTempUserInfoAndUserRole(String appointmentCode
            , String nickname, String password, UserRole userRole);

    Optional<Appointment> findByAppointmentCodeAndTempUserInfo(String appointmentCode
            , String nickname, String password);

    Optional<Appointment> findByAppointmentCodeAndUserInfo(String appointmentCode, Long userId);

    Optional<Appointment> findByAppointmentCodeAndUserInfoAndUserRole(String appointmentCode, Long userId, UserRole userReole);

    List<Appointment> findAllByUserInfoAndKeyword(String email, UserRole userRole, String keyword);
}
