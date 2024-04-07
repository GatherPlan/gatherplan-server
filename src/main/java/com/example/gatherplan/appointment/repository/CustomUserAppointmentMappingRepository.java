package com.example.gatherplan.appointment.repository;

import com.example.gatherplan.appointment.dto.AppointmentParticipationInfoRespDto;
import com.example.gatherplan.appointment.dto.AppointmentWithHostByKeywordRespDto;
import com.example.gatherplan.appointment.dto.AppointmentWithHostRespDto;
import com.example.gatherplan.appointment.enums.UserRole;

import java.util.List;

public interface CustomUserAppointmentMappingRepository {

    boolean existUserMappedToAppointment(String email, String appointmentCode, UserRole userRole);

    List<AppointmentWithHostRespDto> findAllAppointmentsWithHostByEmail(String email);

    List<AppointmentWithHostByKeywordRespDto> findAllAppointmentsWithHostByEmailAndKeyword(String email, String keyword);

    List<AppointmentParticipationInfoRespDto.UserParticipationInfo> findAppointmentParticipationInfo(Long appointmentId);

    String findHostName(Long appointmentId);
}
