package com.example.gatherplan.appointment.service.impl;

import com.example.gatherplan.appointment.dto.CreateTempUserReqDto;
import com.example.gatherplan.appointment.dto.TempUserLoginReqDto;
import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.exception.AppointmentException;
import com.example.gatherplan.appointment.exception.UserException;
import com.example.gatherplan.appointment.repository.AppointmentRepository;
import com.example.gatherplan.appointment.repository.CustomUserRepository;
import com.example.gatherplan.appointment.repository.UserAppointmentMappingRepository;
import com.example.gatherplan.appointment.repository.entity.Appointment;
import com.example.gatherplan.appointment.service.TempUserService;
import com.example.gatherplan.common.exception.ErrorCode;
import com.example.gatherplan.common.unit.TempUserInfo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TempUserServiceImpl implements TempUserService {

    private final CustomUserRepository customUserRepository;
    private final AppointmentRepository appointmentRepository;
    private final UserAppointmentMappingRepository userAppointmentMappingRepository;

    @Override
    @Transactional
    public boolean validJoinTempUser(CreateTempUserReqDto reqDto) {
        TempUserInfo tempUserInfo = reqDto.getTempUserInfo();

        Appointment appointment = appointmentRepository.findByAppointmentCode(reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        return Stream.concat(customUserRepository.findAllUserNameByAppointmentId(appointment.getId()).stream(),
                        customUserRepository.findAllUserNameByAppointmentId(appointment.getId()).stream())
                .noneMatch(nickname -> StringUtils.equals(nickname, tempUserInfo.getNickname()));
    }

    @Override
    public void login(TempUserLoginReqDto reqDto) {
        TempUserInfo tempUserInfo = reqDto.getTempUserInfo();

        Appointment appointment = appointmentRepository.findByAppointmentCode(reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        userAppointmentMappingRepository.findByAppointmentSeqAndNicknameAndTempPasswordAndUserRole(
                appointment.getId(), tempUserInfo.getNickname(), tempUserInfo.getPassword(),
                UserRole.HOST).orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
    }

}

