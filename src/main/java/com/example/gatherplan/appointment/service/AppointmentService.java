package com.example.gatherplan.appointment.service;

import com.example.gatherplan.appointment.dto.AppointmentFormDto;
import jakarta.servlet.http.HttpServletRequest;

public interface AppointmentService {
    void setInformation(AppointmentFormDto appointmentFormDto, HttpServletRequest httpServletRequest);
}
