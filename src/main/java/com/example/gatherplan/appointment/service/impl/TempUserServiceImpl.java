package com.example.gatherplan.appointment.service.impl;

import com.example.gatherplan.appointment.dto.CreateTempUserReqDto;
import com.example.gatherplan.appointment.exception.AppointmentException;
import com.example.gatherplan.appointment.exception.TempUserException;
import com.example.gatherplan.appointment.repository.AppointmentRepository;
import com.example.gatherplan.appointment.repository.CustomTempUserRepository;
import com.example.gatherplan.appointment.repository.entity.Appointment;
import com.example.gatherplan.appointment.service.TempUserService;
import com.example.gatherplan.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TempUserServiceImpl implements TempUserService {

    private final CustomTempUserRepository customTempUserRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    @Transactional
    public void validJoinTempUser(CreateTempUserReqDto reqDto) {

        Appointment appointment = appointmentRepository.findByAppointmentCode(reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        customTempUserRepository.findAllTempUserNameByAppointmentId(appointment.getId())
                .stream()
                .filter(nickname -> nickname.equals(reqDto.getNickname()))
                .findAny()
                .ifPresent(result -> {
                    throw new TempUserException(ErrorCode.TEMP_USER_CONFLICT);
                });
    }

}

