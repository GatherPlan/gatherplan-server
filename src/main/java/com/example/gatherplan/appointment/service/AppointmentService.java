package com.example.gatherplan.appointment.service;

import com.example.gatherplan.appointment.dto.*;

import java.util.List;

public interface AppointmentService {

    String registerAppointment(CreateAppointmentReqDto reqDto, String email);

    void checkParticipation(ParticipationStatusReqDto reqDto, String email);

    List<AppointmentListRespDto> getAppointmentList(String email);

    List<AppointmentSearchListRespDto> getAppointmentSearchList(
            AppointmentSearchListReqDto reqDto, String email);

    AppointmentInfoRespDto getAppointmentInfo(AppointmentInfoReqDto reqDto, String email);

    AppointmentParticipationInfoRespDto getAppointmentParticipationInfo(
            AppointmentParticipationInfoReqDto reqDto, String email);

    void deleteAppointment(DeleteAppointmentReqDto reqDto, String email);

    void updateAppointment(UpdateAppointmentReqDto reqDto, String email);
}
