package com.example.gatherplan.appointment.repository;

import com.example.gatherplan.appointment.dto.AppointmentInfoRespDto;
import com.example.gatherplan.appointment.dto.AppointmentParticipationInfoRespDto;
import com.example.gatherplan.appointment.dto.AppointmentWithHostRespDto;
import com.example.gatherplan.appointment.enums.UserRole;

import java.util.List;
import java.util.Optional;

public interface CustomUserAppointmentMappingRepository {

    boolean existUserMappedToAppointment(String email, String appointmentCode, UserRole userRole);

    List<AppointmentWithHostRespDto> findAllAppointmentsWithHostByEmail(String email);

    List<AppointmentWithHostRespDto> findAllAppointmentsWithHostByEmailAndKeyword(String email, String keyword);

    Optional<AppointmentInfoRespDto> findAppointmentInfo(String email, String appointmentCode);

    Optional<AppointmentParticipationInfoRespDto> findAppointmentParticipationInfo(
            String email, String appointmentCode);
}
