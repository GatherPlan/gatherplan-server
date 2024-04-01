package com.example.gatherplan.appointment.service;

import com.example.gatherplan.appointment.dto.CreateTempAppointmentReqDto;

public interface TempAppointmentService {
    String registerTempAppointment(CreateTempAppointmentReqDto reqDto);
}
