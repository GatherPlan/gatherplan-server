package com.example.gatherplan.appointment.repository;

import java.util.List;

public interface CustomUserRepository {

    List<String> findAllUserNameByAppointmentCode(String appointmentCode);
}
