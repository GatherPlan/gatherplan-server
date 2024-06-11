package com.example.gatherplan.appointment.repository;

import com.example.gatherplan.appointment.dto.AppointmentSearchListRespDto;
import com.example.gatherplan.appointment.enums.AppointmentState;
import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.repository.entity.Appointment;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CustomAppointmentRepository {

    Optional<Appointment> findByAppointmentCodeAndUserSeq(String appointmentCode, Long userId);

    Optional<Appointment> findByAppointmentCodeAndAppointmentStateAndTempUserInfoAndUserRoleIn(String appointmentCode, AppointmentState appointmentState
            , String nickname, String password, Collection<UserRole> userRoles);

    Optional<Appointment> findByAppointmentCodeAndTempUserInfo(String appointmentCode
            , String nickname, String password);
    
    List<AppointmentSearchListRespDto> findAppointmentSearchListRespDtoListByKeywordAndUserSeqAndName(
            String keyword, Long userId, String name);

    Optional<Appointment> findByAppointmentCodeAndUserSeqAndAppointmentStateAndUserRoleIn(String appointmentCode, Long userId, AppointmentState appointmentState, Collection<UserRole> userRoles);
}
