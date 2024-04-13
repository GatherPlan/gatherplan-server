package com.example.gatherplan.appointment.repository;

import com.example.gatherplan.appointment.dto.AppointmentWithHostDto;
import com.example.gatherplan.common.unit.ParticipationInfo;

import java.util.List;

public interface CustomTempUserAppointmentMappingRepository {

    List<ParticipationInfo> findAppointmentParticipationInfo(Long appointmentId);

    String findHostName(Long appointmentId);

    List<AppointmentWithHostDto> findAllAppointmentWithHost(List<Long> appointmentIdList);
}
