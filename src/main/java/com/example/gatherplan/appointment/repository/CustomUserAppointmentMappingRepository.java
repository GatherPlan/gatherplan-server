package com.example.gatherplan.appointment.repository;

import com.example.gatherplan.appointment.dto.AppointmentInfoDto;
import com.example.gatherplan.appointment.dto.AppointmentParticipationInfoDto;
import com.example.gatherplan.appointment.dto.UserAppointmentInfoDto;
import com.example.gatherplan.appointment.dto.UserAppointmentKeywordInfoDto;
import com.example.gatherplan.appointment.enums.UserRole;

import java.util.List;
import java.util.Optional;

public interface CustomUserAppointmentMappingRepository {

    Boolean isUserMappedToAppointment(String email, String appointmentCode, UserRole userRole);

    List<UserAppointmentInfoDto> findAllAppointmentsWithHostByEmail(String email);

    List<UserAppointmentKeywordInfoDto> findAllAppointmentsWithHostByEmailAndKeyword(String email, String keyword);

    Optional<AppointmentInfoDto> findAppointmentInfoDto(String email, String appointmentCode);

    List<AppointmentParticipationInfoDto.UserParticipationInfo> findAppointmentParticipationInfoList(
            String email, String appointmentCode);
}
