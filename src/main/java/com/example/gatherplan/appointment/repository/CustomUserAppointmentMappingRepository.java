package com.example.gatherplan.appointment.repository;

import com.example.gatherplan.appointment.dto.AppointmentWithHostDto;
import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.repository.entity.UserAppointmentMapping;

import java.util.List;
import java.util.Optional;

public interface CustomUserAppointmentMappingRepository {

    List<AppointmentWithHostDto> findAllAppointmentWithHost(List<String> appointmentCodeList);

    Optional<UserAppointmentMapping> findByAppointmentCodeAndUserSeqAndUserRole(
            String appointmentCode, Long userId, UserRole userRole);

    Optional<UserAppointmentMapping> findByAppointmentCodeAndTempInfoAndUserRole(
            String appointmentCode, String nickname, String password, UserRole userRole);
}
