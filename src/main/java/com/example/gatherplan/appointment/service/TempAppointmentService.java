package com.example.gatherplan.appointment.service;

import com.example.gatherplan.appointment.dto.*;

import java.util.List;

public interface TempAppointmentService {
    String registerTempAppointment(CreateTempAppointmentReqDto reqDto);

    boolean validJoinTempUser(CreateTempUserReqDto reqDto);

    boolean login(TempUserLoginReqDto reqDto);

    boolean retrieveParticipationStatus(TempAppointmentParticipationStatusReqDto reqDto);

    void registerAppointmentParticipation(CreateTempAppointmentParticipationReqDto reqDto);

    List<TempAppointmentParticipationInfoRespDto> retrieveAppointmentParticipationInfo(
            TempAppointmentParticipationInfoReqDto reqDto);

    void deleteAppointment(DeleteTempAppointmentReqDto reqDto);

    void updateAppointment(UpdateTempAppointmentReqDto reqDto);

    void confirmedAppointment(TempConfirmedAppointmentReqDto reqDto);

    TempAppointmentInfoDetailRespDto retrieveAppointmentInfoDetail(TempAppointmentInfoDetailReqDto reqDto);

    void deleteAppointmentParticipation(DeleteTempAppointmentParticipationReqDto reqDto);

    void updateAppointmentParticipation(UpdateTempAppointmentParticipationReqDto reqDto);

    List<TempAppointmentCandidateDateInfoRespDto> retrieveAppointmentCandidateDate(TempAppointmentCandidateDateInfoReqDto reqDto);
}
