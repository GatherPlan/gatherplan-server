package com.example.gatherplan.appointment.service;

import com.example.gatherplan.appointment.dto.*;
import org.springframework.data.domain.Page;

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

    Page<TempAppointmentCandidateInfoRespDto> retrieveCandidateInfo(TempAppointmentCandidateInfoReqDto reqDto);

    void confirmAppointment(TempConfirmAppointmentReqDto reqDto);

    TempAppointmentMyParticipantRespDto retrieveAppointmentMyParticipant(TempAppointmentMyParticipantReqDto reqDto);
}
