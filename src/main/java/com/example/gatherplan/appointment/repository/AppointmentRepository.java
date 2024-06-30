package com.example.gatherplan.appointment.repository;

import com.example.gatherplan.appointment.enums.AppointmentState;
import com.example.gatherplan.appointment.repository.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    Optional<Appointment> findByAppointmentCode(String appointmentCode);

    Optional<Appointment> findByAppointmentCodeAndAppointmentState(String appointmentCode, AppointmentState appointmentState);

}
