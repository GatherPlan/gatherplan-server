package com.example.gatherplan.appointment.repository;

import com.example.gatherplan.appointment.dto.TempAppointmentInfoRespDto;

import java.util.Optional;

public interface CustomTempUserAppointmentMappingRepository {
    Optional<TempAppointmentInfoRespDto> findAppointmentInfo(String nickname, String password, String appointmentCode);
}
