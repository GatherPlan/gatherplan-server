package com.example.gatherplan.appointment.repository;

public interface CustomTempUserRepository {

    void deleteAllByAppointmentId(Long appointmentId);
}
