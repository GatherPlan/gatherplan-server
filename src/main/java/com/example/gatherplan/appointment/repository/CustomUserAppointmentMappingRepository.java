package com.example.gatherplan.appointment.repository;

import com.example.gatherplan.appointment.dto.AppointmentInfoDto;
import com.example.gatherplan.appointment.dto.AppointmentParticipationInfoDto;
import com.example.gatherplan.appointment.dto.AppointmentSearchListRespDto;
import com.example.gatherplan.appointment.dto.AppointmentWithHostRespDto;
import com.example.gatherplan.appointment.enums.UserRole;

import java.util.List;
import java.util.Optional;

public interface CustomUserAppointmentMappingRepository {

    boolean existUserMappedToAppointment(String email, String appointmentCode, UserRole userRole);

    List<AppointmentWithHostRespDto> findAllAppointmentsWithHostByEmail(String email);

    List<AppointmentSearchListRespDto> findAllAppointmentsWithHostByEmailAndKeyword(String email, String keyword);

    Optional<AppointmentInfoDto> findAppointmentInfoDto(String email, String appointmentCode);

    List<AppointmentParticipationInfoDto.UserParticipationInfo> findAppointmentParticipationInfoList(
            String email, String appointmentCode);
}
