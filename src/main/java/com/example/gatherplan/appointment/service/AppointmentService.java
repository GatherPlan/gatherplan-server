package com.example.gatherplan.appointment.service;

import com.example.gatherplan.appointment.dto.*;

import java.util.List;

public interface AppointmentService {

    String registerAppointment(CreateAppointmentReqDto reqDto, Long userId);

    AppointmentInfoRespDto retrieveAppointmentInfo(String appointmentCode);

    boolean retrieveParticipationStatus(String appointmentCode, String email);

    boolean validateName(String appointmentCode, String name);

    boolean validateNickname(String appointmentCode, String nickname);

    void registerAppointmentParticipation(CreateAppointmentParticipationReqDto reqDto, Long userId);

    List<AppointmentWithHostByKeywordRespDto> retrieveAppointmentSearchList(String keyword, String email,
                                                                            String nickname);

    AppointmentInfoDetailRespDto retrieveAppointmentInfoDetail(String appointmentCode, Long userId);

    AppointmentParticipationInfoRespDto retrieveAppointmentParticipationInfo(String appointmentCode, Long userId);

    void deleteAppointment(String appointmentCode, Long userId);

    void updateAppointment(UpdateAppointmentReqDto reqDto, Long userId);

    AppointmentRespDto retrieveAppointment(String appointmentCode);

    void confirmedAppointment(ConfirmedAppointmentReqDto reqDto, Long userId);
}
