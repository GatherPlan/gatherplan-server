package com.example.gatherplan.appointment.service;

import com.example.gatherplan.appointment.dto.*;

import java.util.List;

public interface TempAppointmentService {
    String registerAppointment(CreateTempAppointmentReqDto reqDto);

    TempAppointmentInfoRespDto retrieveAppointmentInfo(TempAppointmentInfoReqDto reqDto);

    void updateAppointment(UpdateTempAppointmentReqDto reqDto);

    void deleteAppointment(DeleteTempAppointmentReqDto reqDto);

    void registerAppointmentParticipation(CreateTempAppointmentParticipationReqDto reqDto);

    List<TempAppointmentParticipationInfoRespDto> retrieveAppointmentParticipationInfo(
            TempAppointmentParticipationInfoReqDto reqDto);

    void updateAppointmentParticipation(UpdateTempAppointmentParticipationReqDto reqDto);

    void deleteAppointmentParticipation(DeleteTempAppointmentParticipationReqDto reqDto);

    List<TempAppointmentCandidateDateInfoRespDto> retrieveAppointmentCandidateDate(TempAppointmentCandidateDateInfoReqDto reqDto);

    void confirmedAppointment(TempConfirmedAppointmentReqDto reqDto);

    boolean checkHost(TempUserLoginReqDto reqDto);

    boolean checkParticipation(TempAppointmentParticipationStatusReqDto reqDto);

    boolean validJoinTempUser(CreateTempUserReqDto reqDto);
}
