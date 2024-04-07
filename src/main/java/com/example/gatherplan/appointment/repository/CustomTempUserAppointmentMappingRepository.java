package com.example.gatherplan.appointment.repository;

import com.example.gatherplan.appointment.dto.TempAppointmentParticipationInfoRespDto;

import java.util.List;

public interface CustomTempUserAppointmentMappingRepository {
    List<TempAppointmentParticipationInfoRespDto.UserParticipationInfo> findAppointmentParticipationInfo(Long appointmentId);

    String findHostName(Long appointmentId);
}
