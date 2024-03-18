package com.example.gatherplan.appointment.service;

import com.example.gatherplan.appointment.dto.CreateAppointmentReqDto;
import com.example.gatherplan.appointment.dto.CreateAppointmentRespDto;
import com.example.gatherplan.appointment.dto.CreateTempAppointmentReqDto;
import com.example.gatherplan.appointment.dto.CreateTempAppointmentRespDto;

public interface AppointmentService {
    CreateAppointmentRespDto registerAppointment(CreateAppointmentReqDto createAppointmentReqDto);

    CreateTempAppointmentRespDto registerTempAppointment(CreateTempAppointmentReqDto createTempAppointmentReqDto);
}
