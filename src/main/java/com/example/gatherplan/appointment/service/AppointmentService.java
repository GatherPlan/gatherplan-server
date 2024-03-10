package com.example.gatherplan.appointment.service;

import com.example.gatherplan.appointment.dto.CreateAppointmentReqDto;
import jakarta.servlet.http.HttpServletRequest;

public interface AppointmentService {
    void registerAppointment(CreateAppointmentReqDto createAppointmentReqDto, HttpServletRequest httpServletRequest);
}
