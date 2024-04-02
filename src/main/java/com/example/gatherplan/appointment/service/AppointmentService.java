package com.example.gatherplan.appointment.service;

import com.example.gatherplan.appointment.dto.CheckAppointmentReqDto;
import com.example.gatherplan.appointment.dto.CreateAppointmentReqDto;
import com.example.gatherplan.appointment.dto.GetAppointmentListRespDto;

import java.util.List;

public interface AppointmentService {

    String registerAppointment(CreateAppointmentReqDto reqDto, String email);

    void checkParticipation(CheckAppointmentReqDto checkAppointmentReqDto, String email);

    List<GetAppointmentListRespDto> getAppointmentsList(String email);
}
