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

    AppointmentInfoDetailRespDto retrieveAppointmentInfoDetail(String appointmentCode, String email);

    AppointmentParticipationInfoRespDto retrieveAppointmentParticipationInfo(String appointmentCode, String email);

    void deleteAppointment(String appointmentCode, String email);

    void updateAppointment(UpdateAppointmentReqDto reqDto, String email);

    AppointmentRespDto retrieveAppointment(String appointmentCode);

    void confirmedAppointment(ConfirmedAppointmentReqDto reqDto, String email);
}
