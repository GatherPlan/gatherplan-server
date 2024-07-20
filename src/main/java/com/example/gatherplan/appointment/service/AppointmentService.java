package com.example.gatherplan.appointment.service;

import com.example.gatherplan.appointment.dto.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AppointmentService {

    String registerAppointment(CreateAppointmentReqDto reqDto, Long userId, String name);

    AppointmentInfoRespDto retrieveAppointmentInfo(String appointmentCode, Long userId);

    void updateAppointment(UpdateAppointmentReqDto reqDto, Long userId);

    void deleteAppointment(String appointmentCode, Long userId);

    void registerAppointmentJoin(CreateAppointmentJoinReqDto reqDto, Long userId);

    List<AppointmentParticipantsRespDto> retrieveAppointmentParticipants(String appointmentCode, Long userId);

    void updateAppointmentJoin(UpdateAppointmentJoinReqDto reqDto, Long userId);

    void deleteAppointmentJoin(String appointmentCode, Long userId);

    Page<AppointmentCandidateInfoRespDto> retrieveCandidateInfo(AppointmentCandidateInfoReqDto reqDto, Long userId);

    void confirmAppointment(ConfirmAppointmentReqDto reqDto, Long userId);

    Page<AppointmentSearchRespDto> retrieveAppointmentSearchList(AppointmentSearchReqDto reqDto, Long userId);

    AppointmentPreviewRespDto retrieveAppointmentPreview(String appointmentCode);

    AppointmentMyParticipantRespDto retrieveAppointmentMyParticipant(String appointmentCode, Long userId);
}
