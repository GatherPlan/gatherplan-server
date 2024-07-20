package com.example.gatherplan.appointment.service.impl;

import com.example.gatherplan.appointment.dto.TempUserCreateValidReqDto;
import com.example.gatherplan.appointment.dto.TempUserExistCheckReqDto;
import com.example.gatherplan.appointment.dto.TempUserHostCheckReqDto;
import com.example.gatherplan.appointment.dto.TempUserJoinCheckReqDto;
import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.exception.UserException;
import com.example.gatherplan.appointment.repository.UserAppointmentMappingRepository;
import com.example.gatherplan.appointment.repository.entity.UserAppointmentMapping;
import com.example.gatherplan.appointment.service.TempUserService;
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

    @Override
    public boolean checkHost(TempUserHostCheckReqDto reqDto) {
        UserAppointmentMapping userAppointmentMapping = userAppointmentMappingRepository
                .findByAppointmentCodeAndNicknameAndTempPasswordAndUserRole(
                        reqDto.getAppointmentCode(), reqDto.getTempUserInfo().getNickname(), reqDto.getTempUserInfo().getPassword(), UserRole.HOST)
                .orElseThrow(() -> new UserException(ErrorCode.RESOURCE_NOT_FOUND));

        return UserRole.HOST.equals(userAppointmentMapping.getUserRole());
    }

    @Override
    public boolean checkJoin(TempUserJoinCheckReqDto reqDto) {
        UserAppointmentMapping userAppointmentMapping = userAppointmentMappingRepository
                .findByAppointmentCodeAndNicknameAndTempPasswordAndUserRole(
                        reqDto.getAppointmentCode(), reqDto.getTempUserInfo().getNickname(), reqDto.getTempUserInfo().getPassword(), UserRole.GUEST)
                .orElseThrow(() -> new UserException(ErrorCode.RESOURCE_NOT_FOUND));

        return UserRole.GUEST.equals(userAppointmentMapping.getUserRole());
    }

    @Override
    public boolean checkUser(TempUserExistCheckReqDto reqDto) {
        UserAppointmentMapping userAppointmentMapping = userAppointmentMappingRepository.findByAppointmentCodeAndNicknameAndTempPassword(
                        reqDto.getAppointmentCode(), reqDto.getTempUserInfo().getNickname(), reqDto.getTempUserInfo().getPassword())
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        return UserRole.GUEST.equals(userAppointmentMapping.getUserRole()) ||
                UserRole.HOST.equals(userAppointmentMapping.getUserRole());
    }

    @Override
    public boolean validJoin(TempUserCreateValidReqDto reqDto) {
        List<UserAppointmentMapping> userAppointmentMappingList =
                userAppointmentMappingRepository.findAllByAppointmentCode(reqDto.getAppointmentCode());

        return userAppointmentMappingList.stream().noneMatch(mapping -> StringUtils.equals(mapping.getNickname(), reqDto.getTempUserInfo().getNickname()));
    }
}
