package com.example.gatherplan.appointment.repository;

import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.repository.entity.Appointment;

import java.util.Optional;

public interface CustomAppointmentRepository {

    Optional<Appointment> findByAppointmentCodeAndTempUserInfo(String appointmentCode
            , String nickname, String password, UserRole userRole);

    Optional<Appointment> findByAppointmentCodeAndUserInfo(String appointmentCode
            , String email, UserRole userRole);
}
