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
import com.example.gatherplan.appointment.repository.UserAppointmentMappingRepository;
import com.example.gatherplan.appointment.repository.entity.Appointment;
import com.example.gatherplan.appointment.repository.entity.UserAppointmentMapping;
import com.example.gatherplan.appointment.service.AppointmentService;
import com.example.gatherplan.appointment.utils.AppointmentCandidateInfo;
import com.example.gatherplan.appointment.utils.AppointmentUtils;
import com.example.gatherplan.appointment.validator.AppointmentValidator;
import com.example.gatherplan.common.exception.ErrorCode;
import com.example.gatherplan.common.unit.ParticipationInfo;
import com.example.gatherplan.common.unit.UserParticipationInfo;
import com.example.gatherplan.common.utils.UuidUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentMapper appointmentMapper;

    private final AppointmentRepository appointmentRepository;
    private final UserAppointmentMappingRepository userAppointmentMappingRepository;

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

        List<UserAppointmentMapping> userAppointmentMappingList =
                userAppointmentMappingRepository.findAllByAppointmentCode(appointmentCode);

        UserAppointmentMapping hostMapping = userAppointmentMappingList.stream()
                .filter(mapping -> UserRole.HOST.equals(mapping.getUserRole()))
                .findFirst().orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_HOST));

        String hostName = hostMapping.getNickname();
        
        boolean isHost = userId.equals(hostMapping.getUserSeq());

        boolean isParticipated = userAppointmentMappingList.stream()
                .anyMatch(mapping -> userId.equals(mapping.getUserSeq()) && UserRole.GUEST.equals(mapping.getUserRole()));

        List<UserParticipationInfo> userParticipationInfoList = userAppointmentMappingList.stream()
                .filter(mapping -> UserRole.GUEST.equals(mapping.getUserRole()))
                .map(mapping -> appointmentMapper.to(mapping, StringUtils.equals(
                        hostMapping.getNickname(), mapping.getNickname()) ? UserRole.HOST : UserRole.GUEST))
                .toList();

        return appointmentMapper.to(appointment, userParticipationInfoList, hostName, isHost, isParticipated);
    }

    @Override
    @Transactional
    public void updateAppointment(UpdateAppointmentReqDto reqDto, Long userId) {
        Appointment appointment = customAppointmentRepository
                .findByAppointmentCodeAndUserSeqAndUserRoleAndAppointmentState(reqDto.getAppointmentCode(), userId, UserRole.HOST, AppointmentState.UNCONFIRMED)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        userAppointmentMappingRepository.deleteAllByAppointmentCodeAndUserRole(appointment.getAppointmentCode(), UserRole.GUEST);

        appointment.update(reqDto.getAppointmentName(), reqDto.getAddress(), reqDto.getCandidateDateList(), reqDto.getNotice());
    }

    @Override
    @Transactional
    public void deleteAppointment(String appointmentCode, Long userId) {
        Appointment appointment = customAppointmentRepository
                .findByAppointmentCodeAndUserSeqAndUserRole(appointmentCode, userId, UserRole.HOST)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        userAppointmentMappingRepository.deleteAllByAppointmentCode(appointmentCode);
        appointmentRepository.deleteById(appointment.getId());
    }

    @Override
    @Transactional
    public void registerAppointmentJoin(CreateAppointmentJoinReqDto reqDto, Long userId) {
        userAppointmentMappingRepository
                .findByAppointmentCodeAndUserSeqAndUserRole(reqDto.getAppointmentCode(), userId, UserRole.GUEST)
                .ifPresent(mapping -> {
                    throw new AppointmentException(ErrorCode.APPOINTMENT_ALREADY_PARTICIPATE);
                });

        Appointment appointment = appointmentRepository.findByAppointmentCode(reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        AppointmentValidator.retrieveInvalidSelectedDateTime(appointment.getCandidateDateList(), reqDto.getSelectedDateTimeList())
                .ifPresent(result -> {
                    throw new AppointmentException(ErrorCode.PARAMETER_VALIDATION_FAIL,
                            String.format("후보 날짜에서 벗어난 입력 값 입니다. %s", result));
                });

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

    @Override
    public List<AppointmentParticipantsRespDto> retrieveAppointmentParticipants(String appointmentCode, Long userId) {
        Appointment appointment = customAppointmentRepository.findByAppointmentCodeAndUserSeq(appointmentCode, userId)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        String hostName = userAppointmentMappingRepository.findByAppointmentCodeAndUserRole(appointmentCode, UserRole.HOST)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_HOST))
                .getNickname();

        List<UserAppointmentMapping> userAppointmentMappingList =
                userAppointmentMappingRepository.findAllByAppointmentCodeAndUserRole(appointment.getAppointmentCode(), UserRole.GUEST);

        List<ParticipationInfo> participationInfoList = userAppointmentMappingList.stream()
                .map(mapping -> {
                    UserRole userRole = StringUtils.equals(hostName, mapping.getNickname()) ? UserRole.HOST : UserRole.GUEST;
                    return appointmentMapper.toParticipationInfo(mapping, userRole);
                })
                .toList();

        return participationInfoList.stream().map(appointmentMapper::to).toList();
    }

    @Override
    public AppointmentMyParticipantRespDto retrieveAppointmentMyParticipant(String appointmentCode, Long userId) {
        Appointment appointment = customAppointmentRepository.findByAppointmentCodeAndUserSeq(appointmentCode, userId)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        String hostName = userAppointmentMappingRepository.findByAppointmentCodeAndUserRole(appointmentCode, UserRole.HOST)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_HOST))
                .getNickname();

        UserAppointmentMapping userAppointmentMapping = userAppointmentMappingRepository.findByAppointmentCodeAndUserSeqAndUserRole(appointment.getAppointmentCode(),userId,UserRole.GUEST)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        ParticipationInfo participationInfo = appointmentMapper.toParticipationInfo(
                userAppointmentMapping, StringUtils.equals(hostName, userAppointmentMapping.getNickname()) ? UserRole.HOST : UserRole.GUEST);

        return appointmentMapper.toAppointmentParticipantRespDto(participationInfo);
    }

    @Override
    @Transactional
    public void updateAppointmentJoin(UpdateAppointmentJoinReqDto reqDto, Long userId) {
        Appointment appointment = appointmentRepository.findByAppointmentCodeAndAppointmentState(reqDto.getAppointmentCode(), AppointmentState.UNCONFIRMED)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        UserAppointmentMapping userAppointmentMapping = userAppointmentMappingRepository
                .findByAppointmentCodeAndUserSeqAndUserRole(reqDto.getAppointmentCode(), userId, UserRole.GUEST)
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
                .findByAppointmentCodeAndUserSeqAndUserRole(appointmentCode, userId, UserRole.GUEST)
                .orElseThrow(() -> new AppointmentException(ErrorCode.APPOINTMENT_NOT_PARTICIPATE));

        userAppointmentMappingRepository.deleteById(userAppointmentMapping.getId());
    }

    @Override
    public List<AppointmentCandidateInfoRespDto> retrieveCandidateInfo(String appointmentCode, Long userId) {
        Appointment appointment = customAppointmentRepository.findByAppointmentCodeAndUserSeqAndUserRole(appointmentCode,
                        userId, UserRole.HOST)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        List<LocalDate> candidateDateList = appointment.getCandidateDateList();

        List<UserAppointmentMapping> participationInfoList =
                userAppointmentMappingRepository.findAllByAppointmentCodeAndUserRole(appointmentCode, UserRole.GUEST);

        String hostNickname = userAppointmentMappingRepository.findByAppointmentCodeAndUserRole(appointmentCode, UserRole.HOST)
                .orElseThrow(()-> new UserException(ErrorCode.USER_NOT_FOUND))
                .getNickname();

        List<AppointmentCandidateInfo> appointmentCandidateInfos =
                AppointmentUtils.retrieveCandidateInfoList(candidateDateList, participationInfoList, hostNickname);

        return appointmentCandidateInfos.stream()
                .map(appointmentMapper::to)
                .toList();
    }

    @Override
    @Transactional
    public void confirmAppointment(ConfirmAppointmentReqDto reqDto, Long userId) {
        Appointment appointment = customAppointmentRepository.findByAppointmentCodeAndUserSeqAndUserRole(reqDto.getAppointmentCode(),
                        userId, UserRole.HOST)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        reqDto.getNicknameList().stream()
                .map(nickname -> userAppointmentMappingRepository
                        .findUserAppointmentMappingByAppointmentCodeAndNicknameAndUserRole(reqDto.getAppointmentCode(), nickname, UserRole.GUEST))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(mapping -> mapping.updateIsAvailable(true));

        appointment.confirmed(reqDto.getConfirmedDateTime());
    }

    @Override
    public boolean checkHost(String appointmentCode, Long userId) {
        UserAppointmentMapping userAppointmentMapping = userAppointmentMappingRepository
                .findByAppointmentCodeAndUserSeqAndUserRole(appointmentCode, userId, UserRole.HOST)
                .orElseThrow(() -> new UserException(ErrorCode.RESOURCE_NOT_FOUND));

        return UserRole.HOST.equals(userAppointmentMapping.getUserRole());
    }

    @Override
    public boolean checkJoin(String appointmentCode, Long userId) {
        UserAppointmentMapping userAppointmentMapping = userAppointmentMappingRepository
                .findByAppointmentCodeAndUserSeqAndUserRole(appointmentCode, userId, UserRole.GUEST)
                .orElseThrow(() -> new UserException(ErrorCode.RESOURCE_NOT_FOUND));

        return UserRole.GUEST.equals(userAppointmentMapping.getUserRole());
    }

    @Override
    public boolean checkName(String appointmentCode, String name) {
        List<UserAppointmentMapping> userAppointmentMappingList =
                userAppointmentMappingRepository.findAllByAppointmentCode(appointmentCode);

        return userAppointmentMappingList.stream().noneMatch(
                userAppointmentMapping -> StringUtils.equals(userAppointmentMapping.getNickname(), name));
    }

    @Override
    public boolean checkNickname(String appointmentCode, String nickname) {
        List<UserAppointmentMapping> userAppointmentMappingList =
                userAppointmentMappingRepository.findAllByAppointmentCode(appointmentCode);

        return userAppointmentMappingList.stream().noneMatch(
                userAppointmentMapping -> StringUtils.equals(userAppointmentMapping.getNickname(), nickname));
    }

    @Override
    public List<AppointmentSearchListRespDto> retrieveAppointmentSearchList(String keyword, Long userId) {
        return customAppointmentRepository.findAppointmentSearchListRespDtoListByKeywordAndUserSeq(keyword, userId);
    }

    @Override
    public AppointmentPreviewRespDto retrieveAppointmentPreview(String appointmentCode) {
        Appointment appointment = appointmentRepository.findByAppointmentCode(appointmentCode).orElseThrow(()
                -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        String hostName = userAppointmentMappingRepository.findByAppointmentCodeAndUserRole(appointmentCode, UserRole.HOST)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND))
                .getNickname();

        return appointmentMapper.to(appointment, hostName);
    }

}
