package com.example.gatherplan.appointment.repository;

import com.example.gatherplan.appointment.dto.AppointmentSearchListRespDto;
import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.repository.entity.Appointment;

import java.util.List;
import java.util.Optional;

public interface CustomAppointmentRepository {

    Optional<Appointment> findByAppointmentCodeAndUserSeq(String appointmentCode, Long userId);

    Optional<Appointment> findByAppointmentCodeAndTempUserInfoAndUserRole(String appointmentCode
            , String nickname, String password, UserRole userRole);

    Optional<Appointment> findByAppointmentCodeAndTempUserInfo(String appointmentCode
            , String nickname, String password);

    Optional<Appointment> findByAppointmentCodeAndUserSeqAndUserRole(String appointmentCode
            , Long userId, UserRole userRole);

    List<AppointmentSearchListRespDto> findAppointmentSearchListRespDtoList(
            List<String> appointmentCodeList, String name, String keyword);
}
