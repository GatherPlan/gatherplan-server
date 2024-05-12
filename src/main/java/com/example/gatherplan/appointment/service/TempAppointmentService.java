package com.example.gatherplan.appointment.service;

import com.example.gatherplan.appointment.dto.*;

public interface TempAppointmentService {
    String registerTempAppointment(CreateTempAppointmentReqDto reqDto);

    TempAppointmentInfoRespDto retrieveAppointmentInfo(TempAppointmentInfoReqDto reqDto);

    boolean validJoinTempUser(CreateTempUserReqDto reqDto);

    boolean login(TempUserLoginReqDto reqDto);

    boolean retrieveParticipationStatus(TempAppointmentParticipationStatusReqDto reqDto);

    void registerAppointmentParticipation(CreateTempAppointmentParticipationReqDto reqDto);

    TempAppointmentParticipationInfoRespDto retrieveAppointmentParticipationInfo(
            TempAppointmentParticipationInfoReqDto reqDto);

    void deleteAppointment(DeleteTempAppointmentReqDto reqDto);

    void updateAppointment(UpdateTempAppointmentReqDto reqDto);

    void confirmedAppointment(TempConfirmedAppointmentReqDto reqDto);

    TempAppointmentInfoDetailRespDto retrieveAppointmentInfoDetail(TempAppointmentInfoDetailReqDto reqDto);

    void deleteAppointmentParticipation(DeleteTempAppointmentParticipationReqDto reqDto);

    void updateAppointmentParticipation(UpdateTempAppointmentParticipationReqDto reqDto);
}
