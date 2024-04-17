package com.example.gatherplan.appointment.service;

import com.example.gatherplan.appointment.dto.*;

import java.util.List;

public interface AppointmentService {

    String registerAppointment(CreateAppointmentReqDto reqDto, Long userId);

    boolean isUserParticipated(String appointmentCode, String email);


    List<AppointmentWithHostByKeywordRespDto> retrieveAppointmentSearchList(String keyword, String email,
                                                                            String nickname);

    AppointmentInfoRespDto retrieveAppointmentInfo(String appointmentCode, String email);

    AppointmentParticipationInfoRespDto retrieveAppointmentParticipationInfo(String appointmentCode, String email);

    void deleteAppointment(String appointmentCode, String email);

    void updateAppointment(UpdateAppointmentReqDto reqDto, String email);

    AppointmentRespDto retrieveAppointment(String appointmentCode);

    void registerAppointmentParticipation(CreateAppointmentParticipationReqDto reqDto, Long userId);

    List<String> retrieveEligibleParticipantsList(ConfirmedAppointmentParticipantsReqDto reqDto, String email);
}
