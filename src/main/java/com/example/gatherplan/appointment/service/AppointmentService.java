package com.example.gatherplan.appointment.service;

import com.example.gatherplan.appointment.dto.*;

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

    List<AppointmentCandidateDatesRespDto> retrieveCandidateDates(String appointmentCode, Long userId);

    void confirmedAppointment(ConfirmedAppointmentReqDto reqDto, Long userId);

    boolean checkHost(String appointmentCode, Long userId);

    boolean checkJoin(String appointmentCode, Long userId);

    boolean checkName(String appointmentCode, String name);

    boolean checkNickname(String appointmentCode, String nickname);

    List<AppointmentSearchListRespDto> retrieveAppointmentSearchList(String keyword, Long userId,
                                                                     String nickname);

    AppointmentPreviewRespDto retrieveAppointmentPreview(String appointmentCode);
}
