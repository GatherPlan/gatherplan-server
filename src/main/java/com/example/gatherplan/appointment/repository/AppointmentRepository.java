package com.example.gatherplan.appointment.repository;

import com.example.gatherplan.appointment.repository.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    Optional<Appointment> findByAppointmentCode(String appointmentCode);

    boolean existsByAppointmentCode(String appointmentCode);

    List<Appointment> findAllByAppointmentCodeInAndAppointmentNameContaining(List<String> appointmentCodeList, String keyword);

    List<Appointment> findAllByAppointmentCodeIn(List<String> appointmentCodeList);

    void deleteAllByAppointmentCodeIn(List<String> appointmentCodeList);
}
