package com.example.gatherplan.appointment.service.impl;

import com.example.gatherplan.appointment.dto.TempUserCreateValidReqDto;
import com.example.gatherplan.appointment.dto.TempUserExistCheckReqDto;
import com.example.gatherplan.appointment.dto.TempUserHostCheckReqDto;
import com.example.gatherplan.appointment.dto.TempUserJoinCheckReqDto;
import com.example.gatherplan.appointment.exception.AppointmentException;
import com.example.gatherplan.appointment.repository.AppointmentRepository;
import com.example.gatherplan.appointment.repository.UserAppointmentMappingRepository;
import com.example.gatherplan.appointment.repository.entity.UserAppointmentMapping;
import com.example.gatherplan.appointment.service.TempUserService;
import com.example.gatherplan.appointment.validator.AppointmentValidator;
import com.example.gatherplan.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class TempUserServiceImpl implements TempUserService {

    private final UserAppointmentMappingRepository userAppointmentMappingRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    public boolean checkHost(TempUserHostCheckReqDto reqDto) {
        if (!appointmentRepository.existsByAppointmentCode(reqDto.getAppointmentCode()))
            throw new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT);

        List<UserAppointmentMapping> userAppointmentMappingList =
                userAppointmentMappingRepository.findAllByAppointmentCode(reqDto.getAppointmentCode());

        return AppointmentValidator.isUserHost(reqDto.getTempUserInfo(), userAppointmentMappingList);
    }

    @Override
    public boolean checkJoin(TempUserJoinCheckReqDto reqDto) {
        if (!appointmentRepository.existsByAppointmentCode(reqDto.getAppointmentCode()))
            throw new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT);

        List<UserAppointmentMapping> userAppointmentMappingList =
                userAppointmentMappingRepository.findAllByAppointmentCode(reqDto.getAppointmentCode());

        return AppointmentValidator.isUserParticipated(reqDto.getTempUserInfo(), userAppointmentMappingList);
    }

    @Override
    public boolean checkUser(TempUserExistCheckReqDto reqDto) {
        if (!appointmentRepository.existsByAppointmentCode(reqDto.getAppointmentCode()))
            throw new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT);

        List<UserAppointmentMapping> userAppointmentMappingList =
                userAppointmentMappingRepository.findAllByAppointmentCode(reqDto.getAppointmentCode());

        return AppointmentValidator.isUserHostOrParticipated(reqDto.getTempUserInfo(), userAppointmentMappingList);
    }

    @Override
    public boolean validJoin(TempUserCreateValidReqDto reqDto) {
        if (!appointmentRepository.existsByAppointmentCode(reqDto.getAppointmentCode()))
            throw new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT);

        List<UserAppointmentMapping> userAppointmentMappingList =
                userAppointmentMappingRepository.findAllByAppointmentCode(reqDto.getAppointmentCode());

        if (userAppointmentMappingList.isEmpty()) {
            throw new AppointmentException(ErrorCode.NOT_FOUND_USER_APPOINTMENT_MAPPING);
        }

        return userAppointmentMappingList.stream().noneMatch(mapping -> StringUtils.equals(mapping.getNickname(), reqDto.getTempUserInfo().getNickname()));
    }
}
