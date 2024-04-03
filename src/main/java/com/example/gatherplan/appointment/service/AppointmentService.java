package com.example.gatherplan.appointment.service;

import com.example.gatherplan.appointment.dto.*;

import java.util.List;

public interface AppointmentService {

    String registerAppointment(CreateAppointmentReqDto reqDto, String email);

    void retrieveParticipationStatus(ParticipationStatusReqDto reqDto, String email);

    List<AppointmentListRespDto> retrieveAppointmentList(String email);

    List<AppointmentSearchListRespDto> retrieveAppointmentSearchList(
            AppointmentSearchListReqDto reqDto, String email);

    AppointmentInfoRespDto retrieveAppointmentInfo(AppointmentInfoReqDto reqDto, String email);

    AppointmentParticipationInfoRespDto retrieveAppointmentParticipationInfo(
            AppointmentParticipationInfoReqDto reqDto, String email);

    void deleteAppointment(DeleteAppointmentReqDto reqDto, String email);

    void updateAppointment(UpdateAppointmentReqDto reqDto, String email);
}
