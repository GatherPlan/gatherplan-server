package com.example.gatherplan.appointment.service;

import com.example.gatherplan.appointment.dto.*;

public interface AppointmentService {
    CreateAppointmentRespDto registerAppointment(CreateAppointmentReqDto createAppointmentReqDto, MemberInfoReqDto memberInfoReqDto);

    CreateTempAppointmentRespDto registerTempAppointment(CreateTempAppointmentReqDto createTempAppointmentReqDto);
}
