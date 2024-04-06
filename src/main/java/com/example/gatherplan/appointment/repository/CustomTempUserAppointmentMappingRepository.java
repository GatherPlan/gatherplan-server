package com.example.gatherplan.appointment.repository;

import com.example.gatherplan.appointment.dto.TempAppointmentParticipationInfoRespDto;
import com.example.gatherplan.appointment.repository.entity.Appointment;

import java.util.Optional;

public interface CustomTempUserAppointmentMappingRepository {
    Optional<TempAppointmentParticipationInfoRespDto> findAppointmentParticipationInfo(Appointment appointment);

    String findHostName(Long appointmentId);
}
