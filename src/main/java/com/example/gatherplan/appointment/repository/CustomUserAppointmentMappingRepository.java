package com.example.gatherplan.appointment.repository;

import com.example.gatherplan.appointment.dto.AppointmentParticipationInfoRespDto;
import com.example.gatherplan.appointment.dto.AppointmentWithHostRespDto;
import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.repository.entity.Appointment;

import java.util.List;
import java.util.Optional;

public interface CustomUserAppointmentMappingRepository {

    boolean existUserMappedToAppointment(String email, String appointmentCode, UserRole userRole);

    List<AppointmentWithHostRespDto> findAllAppointmentsWithHostByEmail(String email);

    List<AppointmentWithHostRespDto> findAllAppointmentsWithHostByEmailAndKeyword(String email, String keyword);

    Optional<AppointmentParticipationInfoRespDto> findAppointmentParticipationInfo(Appointment appointment);

    String findHostName(Long appointmentId);
}
