package com.example.gatherplan.appointment.repository;

import com.example.gatherplan.appointment.repository.entity.Appointment;

public interface AppointmentRepository {
    void saveAppointment(Appointment appointment);
}
