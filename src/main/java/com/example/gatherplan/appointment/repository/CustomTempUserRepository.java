package com.example.gatherplan.appointment.repository;

import java.util.List;

public interface CustomTempUserRepository {

    void deleteAllByAppointmentId(Long appointmentId);

    List<String> findAllTempUserNameByAppointmentId(Long appointmentId);
}
