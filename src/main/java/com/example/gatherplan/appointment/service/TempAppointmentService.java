package com.example.gatherplan.appointment.service;

import com.example.gatherplan.appointment.dto.*;

public interface TempAppointmentService {
    String registerTempAppointment(CreateTempAppointmentReqDto reqDto);

    TempAppointmentInfoRespDto retrieveAppointmentInfo(TempAppointmentInfoReqDto reqDto);

    TempAppointmentParticipationInfoRespDto retrieveAppointmentParticipationInfo(
            TempAppointmentParticipationInfoReqDto reqDto);

    void deleteAppointment(DeleteTempAppointmentReqDto reqDto);

    void updateAppointment(UpdateTempAppointmentReqDto reqDto);

    void registerAppointmentParticipation(CreateTempAppointmentParticipationReqDto reqDto);
}
