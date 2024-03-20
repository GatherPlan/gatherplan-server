package com.example.gatherplan.appointment.repository;

import com.example.gatherplan.appointment.repository.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

}
