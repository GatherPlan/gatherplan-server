package com.example.gatherplan.appointment.repository;

import com.example.gatherplan.appointment.dto.AppointmentSearchRespDto;
import com.example.gatherplan.appointment.enums.AppointmentState;
import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.repository.entity.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

public interface CustomAppointmentRepository {

    Optional<Appointment> findByAppointmentCodeAndUserSeq(String appointmentCode, Long userId);

    Optional<Appointment> findByAppointmentCodeAndTempUserInfoAndUserRole(String appointmentCode
            , String nickname, String password, UserRole userRole);

    Optional<Appointment> findByAppointmentCodeAndTempUserInfo(String appointmentCode
            , String nickname, String password);

    Optional<Appointment> findByAppointmentCodeAndUserSeqAndUserRole(String appointmentCode
            , Long userId, UserRole userRole);

    Page<AppointmentSearchRespDto> findAppointmentSearchListRespDtoListByKeywordAndUserSeq(String keyword, Long userId, PageRequest pageable);

    Optional<Appointment> findByAppointmentCodeAndUserSeqAndUserRoleAndAppointmentState(String appointmentCode, Long userId, UserRole userRole, AppointmentState appointmentState);
}
