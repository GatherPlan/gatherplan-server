package com.example.gatherplan.appointment.service;

import com.example.gatherplan.appointment.dto.CreateAppointmentReqDto;
import com.example.gatherplan.appointment.dto.CreateTempAppointmentReqDto;
import com.example.gatherplan.appointment.dto.MemberInfoReqDto;

public interface AppointmentService {
    void registerAppointment(CreateAppointmentReqDto createAppointmentReqDto, MemberInfoReqDto memberInfoReqDto);

    void registerTempAppointment(CreateTempAppointmentReqDto createTempAppointmentReqDto);
}
