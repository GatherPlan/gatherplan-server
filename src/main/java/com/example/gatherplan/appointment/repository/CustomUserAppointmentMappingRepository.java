package com.example.gatherplan.appointment.repository;

import com.example.gatherplan.appointment.dto.AppointmentWithHostDto;
import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.repository.entity.UserAppointmentMapping;
import com.example.gatherplan.common.unit.ParticipationInfo;

import java.util.List;
import java.util.Optional;

public interface CustomUserAppointmentMappingRepository {

    boolean existUserMappedToAppointment(String email, String appointmentCode, UserRole userRole);

    List<ParticipationInfo> findAppointmentParticipationInfo(Long appointmentId);

    String findHostName(Long appointmentId);

    List<AppointmentWithHostDto> findAllAppointmentWithHost(List<Long> appointmentIdList);

    Optional<UserAppointmentMapping> findIsHost(String email, Long appointmentId);
}
