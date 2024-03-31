package com.example.gatherplan.appointment.service;

import com.example.gatherplan.appointment.dto.CreateTempAppointmentReqDto;

public interface AppointmentTempService {
    String registerTempAppointment(CreateTempAppointmentReqDto createTempAppointmentReqDto);
}
