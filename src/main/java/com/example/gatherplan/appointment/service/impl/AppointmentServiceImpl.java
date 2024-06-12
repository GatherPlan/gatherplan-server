package com.example.gatherplan.appointment.service.impl;

import com.example.gatherplan.appointment.dto.*;
import com.example.gatherplan.appointment.enums.AppointmentState;
import com.example.gatherplan.appointment.enums.UserAuthType;
import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.exception.AppointmentException;
import com.example.gatherplan.appointment.exception.UserException;
import com.example.gatherplan.appointment.mapper.AppointmentMapper;
import com.example.gatherplan.appointment.repository.AppointmentRepository;
import com.example.gatherplan.appointment.repository.CustomAppointmentRepository;
import com.example.gatherplan.appointment.repository.CustomUserRepository;
import com.example.gatherplan.appointment.repository.UserAppointmentMappingRepository;
import com.example.gatherplan.appointment.repository.entity.Appointment;
import com.example.gatherplan.appointment.repository.entity.UserAppointmentMapping;
import com.example.gatherplan.appointment.service.AppointmentService;
import com.example.gatherplan.appointment.utils.AppointmentCandidateInfo;
import com.example.gatherplan.appointment.utils.AppointmentUtils;
import com.example.gatherplan.appointment.validator.AppointmentValidator;
import com.example.gatherplan.common.exception.ErrorCode;
import com.example.gatherplan.common.unit.UserParticipationInfo;
import com.example.gatherplan.common.utils.UuidUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentMapper appointmentMapper;

    private final AppointmentRepository appointmentRepository;
    private final UserAppointmentMappingRepository userAppointmentMappingRepository;

    private final CustomUserRepository customUserRepository;
    private final CustomAppointmentRepository customAppointmentRepository;

    @Override
    @Transactional
    public String registerAppointment(CreateAppointmentReqDto reqDto, Long userId, String name) {
        String appointmentCode = UuidUtils.generateRandomString(12);

        Appointment appointment = appointmentMapper.to(reqDto, AppointmentState.UNCONFIRMED, appointmentCode);

        appointmentRepository.save(appointment);

        UserAppointmentMapping userAppointmentMapping = UserAppointmentMapping.builder()
                .appointmentCode(appointmentCode)
                .userSeq(userId)
                .userRole(UserRole.HOST)
                .nickname(name)
                .userAuthType(UserAuthType.LOCAL)
                .build();

        userAppointmentMappingRepository.save(userAppointmentMapping);

        return appointmentCode;
    }

    @Override
    public AppointmentInfoRespDto retrieveAppointmentInfo(String appointmentCode, Long userId) {
        Appointment appointment = customAppointmentRepository.findByAppointmentCodeAndUserSeq(appointmentCode, userId)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        String hostName = userAppointmentMappingRepository.findByAppointmentCodeAndUserRoleIn(appointmentCode, List.of(UserRole.MASTER, UserRole.HOST))
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_HOST))
                .getNickname();

        boolean isParticipated = userAppointmentMappingRepository
                .existsByAppointmentCodeAndUserSeqAndUserRoleIn(appointmentCode, userId, List.of(UserRole.MASTER, UserRole.GUEST));

        boolean isHost = userAppointmentMappingRepository
                .existsByAppointmentCodeAndUserSeqAndUserRoleIn(appointmentCode, userId, List.of(UserRole.MASTER, UserRole.HOST));

        List<UserAppointmentMapping> userAppointmentMappingList =
                userAppointmentMappingRepository.findAllByAppointmentCodeAndUserRoleIn(appointmentCode, List.of(UserRole.MASTER, UserRole.GUEST));

        List<UserParticipationInfo> userParticipationInfoList = userAppointmentMappingList.stream()
                .map(appointmentMapper::toUserParticipationInfo)
                .toList();

        return appointmentMapper.toAppointmentInfoDetailRespDto(appointment, hostName, isParticipated, isHost, userParticipationInfoList);
    }

    @Override
    @Transactional
    public void updateAppointment(UpdateAppointmentReqDto reqDto, Long userId) {
        Appointment appointment = customAppointmentRepository
                .findByAppointmentCodeAndUserSeqAndAppointmentStateAndUserRoleIn(reqDto.getAppointmentCode(), userId, AppointmentState.UNCONFIRMED,
                        List.of(UserRole.MASTER, UserRole.HOST))
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        userAppointmentMappingRepository.deleteAllByAppointmentCodeAndUserRole(appointment.getAppointmentCode(), UserRole.GUEST);

        userAppointmentMappingRepository
                .findByAppointmentCodeAndUserSeqAndUserRoleIn(reqDto.getAppointmentCode(), userId, List.of(UserRole.MASTER))
                .ifPresent(mapping -> mapping.updateUserRole(UserRole.HOST));

        appointment.update(reqDto.getAppointmentName(), reqDto.getAddress(), reqDto.getCandidateDateList(), reqDto.getNotice());
    }

    @Override
    @Transactional
    public void deleteAppointment(String appointmentCode, Long userId) {
        Appointment appointment = customAppointmentRepository
                .findByAppointmentCodeAndUserSeqAndAppointmentStateAndUserRoleIn(appointmentCode, userId, AppointmentState.UNCONFIRMED,
                        List.of(UserRole.MASTER, UserRole.HOST))
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        userAppointmentMappingRepository.deleteAllByAppointmentCode(appointmentCode);
        appointmentRepository.deleteById(appointment.getId());
    }

    @Override
    @Transactional
    public void registerAppointmentJoin(CreateAppointmentJoinReqDto reqDto, Long userId) {
        Appointment appointment = appointmentRepository.findByAppointmentCode(reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        userAppointmentMappingRepository
                .findByAppointmentCodeAndUserSeqAndUserRoleIn(reqDto.getAppointmentCode(), userId, List.of(UserRole.MASTER, UserRole.GUEST))
                .ifPresent(mapping -> {
                    throw new AppointmentException(ErrorCode.APPOINTMENT_ALREADY_PARTICIPATE);
                });

        AppointmentValidator.retrieveInvalidSelectedDateTime(appointment.getCandidateDateList(), reqDto.getSelectedDateTimeList())
                .ifPresent(result -> {
                    throw new AppointmentException(ErrorCode.PARAMETER_VALIDATION_FAIL,
                            String.format("후보 날짜에서 벗어난 입력 값 입니다. %s", result));
                });

        userAppointmentMappingRepository.findByAppointmentCodeAndUserSeqAndUserRoleIn(
                        reqDto.getAppointmentCode(), userId, List.of(UserRole.HOST))
                .ifPresentOrElse(
                        mapping -> mapping.updateUserRoleAndSelectedDateTimeList(UserRole.MASTER, reqDto.getSelectedDateTimeList()),
                        () -> {
                            UserAppointmentMapping userAppointmentMapping = UserAppointmentMapping.builder()
                                    .appointmentCode(reqDto.getAppointmentCode())
                                    .userSeq(userId)
                                    .userRole(UserRole.GUEST)
                                    .userAuthType(UserAuthType.LOCAL)
                                    .isAvailable(false)
                                    .selectedDateTimeList(List.copyOf(reqDto.getSelectedDateTimeList()))
                                    .nickname(reqDto.getNickname())
                                    .build();

                            userAppointmentMappingRepository.save(userAppointmentMapping);
                        }
                );
    }

    @Override
    public List<AppointmentParticipantsRespDto> retrieveAppointmentParticipants(String appointmentCode, Long userId) {
        Appointment appointment = customAppointmentRepository.findByAppointmentCodeAndUserSeq(appointmentCode, userId)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        List<UserAppointmentMapping> participationInfoList =
                userAppointmentMappingRepository.findAllByAppointmentCodeAndUserRoleIn(
                        appointment.getAppointmentCode(), List.of(UserRole.MASTER, UserRole.GUEST));

        return participationInfoList.stream().map(appointmentMapper::to).toList();
    }

    @Override
    public AppointmentParticipantRespDto retrieveAppointmentParticipant(String appointmentCode, Long userId) {
        Appointment appointment = customAppointmentRepository.findByAppointmentCodeAndUserSeq(appointmentCode, userId)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        UserAppointmentMapping userAppointmentMapping =
                userAppointmentMappingRepository.findByAppointmentCodeAndUserSeqAndUserRoleIn(appointment.getAppointmentCode(),
                                userId, List.of(UserRole.MASTER, UserRole.GUEST))
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        return appointmentMapper.toAppointmentParticipantRespDto(userAppointmentMapping);
    }

    @Override
    @Transactional
    public void updateAppointmentJoin(UpdateAppointmentJoinReqDto reqDto, Long userId) {
        Appointment appointment = appointmentRepository.findByAppointmentCodeAndAppointmentState(reqDto.getAppointmentCode(), AppointmentState.UNCONFIRMED)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        UserAppointmentMapping userAppointmentMapping = userAppointmentMappingRepository
                .findByAppointmentCodeAndUserSeqAndUserRoleIn(reqDto.getAppointmentCode(), userId, List.of(UserRole.MASTER, UserRole.GUEST))
                .orElseThrow(() -> new AppointmentException(ErrorCode.APPOINTMENT_NOT_PARTICIPATE));

        AppointmentValidator.retrieveInvalidSelectedDateTime(appointment.getCandidateDateList(), reqDto.getSelectedDateTimeList())
                .ifPresent(result -> {
                    throw new AppointmentException(ErrorCode.PARAMETER_VALIDATION_FAIL,
                            String.format("후보 날짜에서 벗어난 입력 값 입니다. %s", result));
                });

        userAppointmentMapping.update(reqDto.getSelectedDateTimeList());
    }

    @Override
    @Transactional
    public void deleteAppointmentJoin(String appointmentCode, Long userId) {
        UserAppointmentMapping userAppointmentMapping = userAppointmentMappingRepository
                .findByAppointmentCodeAndUserSeqAndUserRoleIn(appointmentCode, userId, List.of(UserRole.MASTER, UserRole.GUEST))
                .orElseThrow(() -> new AppointmentException(ErrorCode.APPOINTMENT_NOT_PARTICIPATE));

        userAppointmentMappingRepository.deleteById(userAppointmentMapping.getId());
    }

    @Override
    public List<AppointmentCandidateInfoRespDto> retrieveCandidateInfo(String appointmentCode, Long userId) {
        Appointment appointment = customAppointmentRepository
                .findByAppointmentCodeAndUserSeqAndAppointmentStateAndUserRoleIn(appointmentCode, userId, AppointmentState.UNCONFIRMED,
                        List.of(UserRole.MASTER, UserRole.HOST))
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        List<LocalDate> candidateDateList = appointment.getCandidateDateList();

        List<UserAppointmentMapping> participationInfoList =
                userAppointmentMappingRepository.findAllByAppointmentCodeAndUserRoleIn(appointmentCode, List.of(UserRole.MASTER, UserRole.GUEST));

        List<AppointmentCandidateInfo> appointmentCandidateInfos =
                AppointmentUtils.retrieveCandidateInfoList(candidateDateList, participationInfoList);

        return appointmentCandidateInfos.stream()
                .map(appointmentMapper::to)
                .toList();
    }

    @Override
    @Transactional
    public void confirmAppointment(ConfirmAppointmentReqDto reqDto, Long userId) {
        Appointment appointment = customAppointmentRepository
                .findByAppointmentCodeAndUserSeqAndAppointmentStateAndUserRoleIn(reqDto.getAppointmentCode(), userId, AppointmentState.UNCONFIRMED,
                        List.of(UserRole.MASTER, UserRole.HOST))
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        appointment.confirmed(reqDto.getConfirmedDateTime());
    }

    @Override
    public boolean checkHost(String appointmentCode, Long userId) {
        return userAppointmentMappingRepository
                .existsByAppointmentCodeAndUserSeqAndUserRoleIn(appointmentCode, userId, List.of(UserRole.MASTER, UserRole.HOST));
    }

    @Override
    public boolean checkJoin(String appointmentCode, Long userId) {
        return userAppointmentMappingRepository
                .existsByAppointmentCodeAndUserSeqAndUserRoleIn(appointmentCode, userId, List.of(UserRole.MASTER, UserRole.GUEST));
    }

    @Override
    public boolean checkName(String appointmentCode, String name) {
        return customUserRepository.findAllUserNameByAppointmentCode(appointmentCode).stream()
                .noneMatch(findNickname -> StringUtils.equals(findNickname, name));
    }

    @Override
    public boolean checkNickname(String appointmentCode, String nickname) {
        return customUserRepository.findAllUserNameByAppointmentCode(appointmentCode).stream()
                .noneMatch(findNickname -> StringUtils.equals(findNickname, nickname));
    }

    @Override
    public List<AppointmentSearchListRespDto> retrieveAppointmentSearchList(String keyword, Long userId, String name) {
        return customAppointmentRepository.findAppointmentSearchListRespDtoListByKeywordAndUserSeqAndName(keyword, userId, name);
    }

    @Override
    public AppointmentPreviewRespDto retrieveAppointmentPreview(String appointmentCode) {
        Appointment appointment = appointmentRepository.findByAppointmentCode(appointmentCode).orElseThrow(()
                -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        String hostName = userAppointmentMappingRepository.findByAppointmentCodeAndUserRoleIn(appointmentCode, List.of(UserRole.MASTER, UserRole.HOST))
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND))
                .getNickname();

        return appointmentMapper.to(appointment, hostName);
    }

}
