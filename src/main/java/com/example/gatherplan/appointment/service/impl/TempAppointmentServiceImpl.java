package com.example.gatherplan.appointment.service.impl;

import com.example.gatherplan.appointment.dto.*;
import com.example.gatherplan.appointment.enums.AppointmentState;
import com.example.gatherplan.appointment.enums.UserAuthType;
import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.exception.AppointmentException;
import com.example.gatherplan.appointment.exception.UserException;
import com.example.gatherplan.appointment.mapper.TempAppointmentMapper;
import com.example.gatherplan.appointment.repository.*;
import com.example.gatherplan.appointment.repository.entity.Appointment;
import com.example.gatherplan.appointment.repository.entity.UserAppointmentMapping;
import com.example.gatherplan.appointment.service.TempAppointmentService;
import com.example.gatherplan.appointment.validator.AppointmentValidator;
import com.example.gatherplan.common.exception.ErrorCode;
import com.example.gatherplan.common.utils.UuidUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TempAppointmentServiceImpl implements TempAppointmentService {

    private final TempAppointmentMapper tempAppointmentMapper;
    private final AppointmentRepository appointmentRepository;
    private final UserAppointmentMappingRepository userAppointmentMappingRepository;
    private final CustomAppointmentRepository customAppointmentRepository;
    private final CustomUserAppointmentMappingRepository customUserAppointmentMappingRepository;
    private final CustomUserRepository customUserRepository;

    @Override
    @Transactional
    public String registerTempAppointment(CreateTempAppointmentReqDto reqDto) {
        String appointmentCode = UuidUtils.generateRandomString(12);

        Appointment appointment = tempAppointmentMapper.to(reqDto, AppointmentState.UNCONFIRMED, appointmentCode);

        Long appointmentId = appointmentRepository.save(appointment).getId();

        UserAppointmentMapping userAppointmentMapping = UserAppointmentMapping.builder()
                .appointmentSeq(appointmentId)
                .userRole(UserRole.HOST)
                .nickname(reqDto.getTempUserInfo().getNickname())
                .tempPassword(reqDto.getTempUserInfo().getPassword())
                .userAuthType(UserAuthType.TEMPORARY)
                .build();

        userAppointmentMappingRepository.save(userAppointmentMapping);

        return appointmentCode;
    }

    @Override
    public TempAppointmentInfoRespDto retrieveAppointmentInfo(TempAppointmentInfoReqDto reqDto) {
        Appointment appointment = appointmentRepository.findByAppointmentCode(reqDto.getAppointmentCode()).orElseThrow(()
                -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        String hostName = Optional.ofNullable(customUserAppointmentMappingRepository.findHostName(appointment.getId()))
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        return tempAppointmentMapper.to(appointment, hostName);
    }

    @Override
    @Transactional
    public boolean validJoinTempUser(CreateTempUserReqDto reqDto) {
        Appointment appointment = appointmentRepository.findByAppointmentCode(reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        return customUserRepository.findAllUserNameByAppointmentId(appointment.getId()).stream()
                .noneMatch(nickname -> StringUtils.equals(nickname, reqDto.getTempUserInfo().getNickname()));
    }

    @Override
    public boolean login(TempUserLoginReqDto reqDto) {
        Appointment appointment = appointmentRepository.findByAppointmentCode(reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        return userAppointmentMappingRepository
                .existsByAppointmentSeqAndNicknameAndTempPasswordAndUserRole(
                        appointment.getId(), reqDto.getTempUserInfo().getNickname(), reqDto.getTempUserInfo().getPassword(), UserRole.HOST);
    }

    @Override
    public boolean retrieveParticipationStatus(TempAppointmentParticipationStatusReqDto reqDto) {
        Appointment appointment = appointmentRepository.findByAppointmentCode(reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        return userAppointmentMappingRepository
                .existsByAppointmentSeqAndNicknameAndTempPasswordAndUserRole(
                        appointment.getId(), reqDto.getTempUserInfo().getNickname(), reqDto.getTempUserInfo().getPassword(), UserRole.GUEST);
    }

    @Override
    public void registerAppointmentParticipation(CreateTempAppointmentParticipationReqDto reqDto) {
        Appointment appointment = appointmentRepository.findByAppointmentCode(reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        AppointmentValidator.retrieveInvalidSelectedDateTime(appointment, reqDto.getSelectedDateTimeList())
                .ifPresent(result -> {
                    throw new AppointmentException(ErrorCode.PARAMETER_VALIDATION_FAIL,
                            String.format("후보 날짜에서 벗어난 입력 값 입니다. %s", result));
                });

        UserAppointmentMapping userAppointmentMapping = UserAppointmentMapping.builder()
                .appointmentSeq(appointment.getId())
                .userRole(UserRole.GUEST)
                .nickname(reqDto.getTempUserInfo().getNickname())
                .tempPassword(reqDto.getTempUserInfo().getPassword())
                .userAuthType(UserAuthType.TEMPORARY)
                .selectedDateTimeList(List.copyOf(reqDto.getSelectedDateTimeList()))
                .build();

        userAppointmentMappingRepository.save(userAppointmentMapping);
    }

    @Override
    public TempAppointmentInfoDetailRespDto retrieveAppointmentInfoDetail(TempAppointmentInfoDetailReqDto reqDto) {
        Appointment appointment = customAppointmentRepository.findByAppointmentCodeAndTempUserInfo(reqDto.getAppointmentCode(),
                        reqDto.getTempUserInfo().getNickname(), reqDto.getTempUserInfo().getPassword())
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        String hostName = Optional.ofNullable(customUserAppointmentMappingRepository.findHostName(appointment.getId()))
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        return tempAppointmentMapper.toTempAppointmentInfoDetailRespDto(appointment, hostName);
    }

    @Override
    public TempAppointmentParticipationInfoRespDto retrieveAppointmentParticipationInfo(TempAppointmentParticipationInfoReqDto reqDto) {
        Appointment appointment = customAppointmentRepository.findByAppointmentCodeAndTempUserInfo(reqDto.getAppointmentCode(),
                        reqDto.getTempUserInfo().getNickname(), reqDto.getTempUserInfo().getPassword())
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        List<UserAppointmentMapping> participationInfoList =
                userAppointmentMappingRepository.findAllByAppointmentSeqAndUserRole(appointment.getId(), UserRole.GUEST);

        return tempAppointmentMapper.to(appointment, participationInfoList);
    }

    @Override
    @Transactional
    public void deleteAppointment(DeleteTempAppointmentReqDto reqDto) {
        Long appointmentId = customAppointmentRepository.findByAppointmentCodeAndTempUserInfoAndUserRole(reqDto.getAppointmentCode(),
                        reqDto.getTempUserInfo().getNickname(), reqDto.getTempUserInfo().getPassword(), UserRole.HOST)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT))
                .getId();

        userAppointmentMappingRepository.deleteAllByAppointmentSeq(appointmentId);
        appointmentRepository.deleteById(appointmentId);
    }

    @Override
    @Transactional
    public void deleteAppointmentParticipation(DeleteTempAppointmentParticipationReqDto reqDto) {
        Long appointmentId = customAppointmentRepository.findByAppointmentCodeAndTempUserInfoAndUserRole(reqDto.getAppointmentCode(),
                        reqDto.getTempUserInfo().getNickname(), reqDto.getTempUserInfo().getPassword(), UserRole.GUEST)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT))
                .getId();

        userAppointmentMappingRepository.deleteByAppointmentSeqAndNicknameAndTempPasswordAndUserRole(
                appointmentId, reqDto.getTempUserInfo().getNickname(), reqDto.getTempUserInfo().getPassword(), UserRole.GUEST);
    }

    @Override
    @Transactional
    public void updateAppointment(UpdateTempAppointmentReqDto reqDto) {
        Appointment appointment = customAppointmentRepository.findByAppointmentCodeAndTempUserInfoAndUserRole(reqDto.getAppointmentCode(),
                        reqDto.getTempUserInfo().getNickname(), reqDto.getTempUserInfo().getPassword(), UserRole.HOST)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        appointment.update(reqDto.getAppointmentName(),
                reqDto.getAddress(), reqDto.getCandidateDateList(), reqDto.getNotice());
    }


    @Override
    @Transactional
    public void confirmedAppointment(TempConfirmedAppointmentReqDto reqDto) {
        Appointment appointment = customAppointmentRepository.findByAppointmentCodeAndTempUserInfoAndUserRole(reqDto.getAppointmentCode(),
                        reqDto.getTempUserInfo().getNickname(), reqDto.getTempUserInfo().getPassword(), UserRole.HOST)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        appointment.confirmed(reqDto.getConfirmedDateTime());
    }
}
