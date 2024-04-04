package com.example.gatherplan.appointment.repository;

public interface CustomTempUserAppointmentMappingRepository {

    void deleteAllByAppointmentId(Long appointmentId);
}
