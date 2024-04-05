package com.example.gatherplan.appointment.repository;

import com.example.gatherplan.appointment.dto.TempAppointmentInfoRespDto;
import com.example.gatherplan.appointment.dto.TempAppointmentParticipationInfoRespDto;
import com.example.gatherplan.appointment.enums.UserRole;

import java.util.Optional;

public interface CustomTempUserAppointmentMappingRepository {
    Optional<TempAppointmentInfoRespDto> findAppointmentInfo(String nickname, String password, String appointmentCode);

    Optional<TempAppointmentParticipationInfoRespDto> findAppointmentParticipationInfo(
            String nickname, String password, String appointmentCode);

    boolean existUserMappedToAppointment(String appointmentCode, String nickname, UserRole userRole);
}
