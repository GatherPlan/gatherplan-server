package com.example.gatherplan.appointment.service;

import com.example.gatherplan.appointment.dto.*;

import java.util.List;

public interface TempAppointmentService {
    String registerAppointment(CreateTempAppointmentReqDto reqDto);

    TempAppointmentInfoRespDto retrieveAppointmentInfo(TempAppointmentInfoReqDto reqDto);

    void updateAppointment(UpdateTempAppointmentReqDto reqDto);

    void deleteAppointment(DeleteTempAppointmentReqDto reqDto);

    void registerAppointmentJoin(CreateTempAppointmentJoinReqDto reqDto);

    List<TempAppointmentParticipantsRespDto> retrieveAppointmentParticipants(
            TempAppointmentParticipantsReqDto reqDto);

    void updateAppointmentJoin(UpdateTempAppointmentJoinReqDto reqDto);

    void deleteAppointmentJoin(DeleteTempAppointmentJoinReqDto reqDto);

    List<TempAppointmentCandidateInfoRespDto> retrieveCandidateInfo(TempAppointmentCandidateInfoReqDto reqDto);

    void confirmAppointment(TempConfirmAppointmentReqDto reqDto);

    boolean checkHost(TempCheckHostReqDto reqDto);

    boolean checkJoin(TempCheckJoinReqDto reqDto);

    boolean validJoin(CreateTempUserReqDto reqDto);
}
