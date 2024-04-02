package com.example.gatherplan.appointment.service;

import com.example.gatherplan.appointment.dto.*;

import java.util.List;

public interface AppointmentService {

    String registerAppointment(CreateAppointmentReqDto reqDto, String email);

    void checkParticipation(CheckAppointmentReqDto checkAppointmentReqDto, String email);

    List<GetAppointmentListRespDto> getAppointmentList(String email);

    List<GetAppointmentSearchListRespDto> getAppointmentSearchList(
            GetAppointmentSearchListReqDto getAppointmentSearchListReqDto, String email);
}
