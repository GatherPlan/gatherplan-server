package com.example.gatherplan.appointment.service;

import com.example.gatherplan.appointment.dto.*;

import java.util.List;

public interface AppointmentService {

    String registerAppointment(CreateAppointmentReqDto reqDto, String email);

    boolean isUserParticipated(String appointmentCode, String email);

    List<AppointmentWithHostRespDto> retrieveAppointmentList(String email);

    List<AppointmentWithHostRespDto> retrieveAppointmentSearchList(String keyword, String email);

    AppointmentInfoRespDto retrieveAppointmentInfo(String appointmentCode, String email);

    AppointmentParticipationInfoRespDto retrieveAppointmentParticipationInfo(String appointmentCode, String email);

    void deleteAppointment(String appointmentCode, String email);

    void updateAppointment(UpdateAppointmentReqDto reqDto, String email);
}
