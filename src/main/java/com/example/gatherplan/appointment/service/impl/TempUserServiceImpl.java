package com.example.gatherplan.appointment.service.impl;

import com.example.gatherplan.appointment.dto.CreateTempUserReqDto;
import com.example.gatherplan.appointment.exception.AppointmentException;
import com.example.gatherplan.appointment.repository.AppointmentRepository;
import com.example.gatherplan.appointment.repository.CustomTempUserRepository;
import com.example.gatherplan.appointment.repository.CustomUserRepository;
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

    private final CustomTempUserRepository customTempUserRepository;
    private final CustomUserRepository customUserRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    @Transactional
    public boolean validJoinTempUser(CreateTempUserReqDto reqDto) {
        TempUserInfo tempUserInfo = reqDto.getTempUserInfo();

        Appointment appointment = appointmentRepository.findByAppointmentCode(reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        return Stream.concat(customTempUserRepository.findAllTempUserNameByAppointmentId(appointment.getId()).stream(),
                        customUserRepository.findAllUserNameByAppointmentId(appointment.getId()).stream())
                .noneMatch(nickname -> StringUtils.equals(nickname, tempUserInfo.getNickname()));
    }

}

