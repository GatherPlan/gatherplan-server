package com.example.gatherplan.appointment.service;

import com.example.gatherplan.appointment.dto.*;

import java.util.List;

public interface AppointmentService {

    String registerAppointment(CreateAppointmentReqDto reqDto, Long userId, String name);

    AppointmentInfoRespDto retrieveAppointmentInfo(String appointmentCode, Long userId);

    void updateAppointment(UpdateAppointmentReqDto reqDto, Long userId);

    void deleteAppointment(String appointmentCode, Long userId);

    void registerAppointmentParticipation(CreateAppointmentParticipationReqDto reqDto, Long userId);

    List<AppointmentParticipationInfoRespDto> retrieveAppointmentParticipationInfo(String appointmentCode, Long userId);

    void updateAppointmentParticipation(UpdateAppointmentParticipationReqDto reqDto, Long userId);

    void deleteAppointmentParticipation(String appointmentCode, Long userId);

    List<AppointmentCandidateDateInfoRespDto> retrieveAppointmentCandidateDate(String appointmentCode, Long userId);

    void confirmedAppointment(ConfirmedAppointmentReqDto reqDto, Long userId);

    boolean retrieveHostStatus(String appointmentCode, Long userId);

    boolean retrieveParticipationStatus(String appointmentCode, Long userId);

    boolean validateName(String appointmentCode, String name);

    boolean validateNickname(String appointmentCode, String nickname);

    List<AppointmentWithHostByKeywordRespDto> retrieveAppointmentSearchList(String keyword, Long userId,
                                                                            String nickname);

    AppointmentRespDto retrieveAppointment(String appointmentCode);

    AppointmentPreviewRespDto retrieveAppointmentPreview(String appointmentCode);
}
