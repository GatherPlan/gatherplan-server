package com.example.gatherplan.appointment.repository;

import com.example.gatherplan.appointment.dto.AppointmentParticipationInfoRespDto;
import com.example.gatherplan.appointment.dto.AppointmentWithHostByKeywordDto;
import com.example.gatherplan.appointment.dto.AppointmentWithHostDto;
import com.example.gatherplan.appointment.enums.UserRole;

import java.util.List;

public interface CustomUserAppointmentMappingRepository {

    boolean existUserMappedToAppointment(String email, String appointmentCode, UserRole userRole);

    List<AppointmentParticipationInfoRespDto.UserParticipationInfo> findAppointmentParticipationInfo(Long appointmentId);

    String findHostName(Long appointmentId);

    List<AppointmentWithHostDto> findAllAppointmentWithHost(List<Long> appointmentIdList);

    List<AppointmentWithHostByKeywordDto> findAllAppointmentWithHostByKeyword(List<Long> appointmentIdList);
}
