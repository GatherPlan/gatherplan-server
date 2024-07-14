package com.example.gatherplan.appointment.service.impl;

import com.example.gatherplan.appointment.dto.*;
import com.example.gatherplan.appointment.enums.AppointmentState;
import com.example.gatherplan.appointment.enums.UserAuthType;
import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.exception.AppointmentException;
import com.example.gatherplan.appointment.mapper.AppointmentMapper;
import com.example.gatherplan.appointment.repository.AppointmentRepository;
import com.example.gatherplan.appointment.repository.UserAppointmentMappingRepository;
import com.example.gatherplan.appointment.repository.entity.Appointment;
import com.example.gatherplan.appointment.repository.entity.UserAppointmentMapping;
import com.example.gatherplan.appointment.service.AppointmentService;
import com.example.gatherplan.appointment.utils.AppointmentUtils;
import com.example.gatherplan.appointment.utils.unit.AppointmentCandidateInfo;
import com.example.gatherplan.appointment.validator.AppointmentValidator;
import com.example.gatherplan.common.exception.ErrorCode;
import com.example.gatherplan.common.unit.ConfirmedDateTime;
import com.example.gatherplan.common.unit.CustomPageRequest;
import com.example.gatherplan.common.unit.ParticipationInfo;
import com.example.gatherplan.common.unit.UserParticipationInfo;
import com.example.gatherplan.common.utils.UuidUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentMapper appointmentMapper;

    private final AppointmentRepository appointmentRepository;
    private final UserAppointmentMappingRepository userAppointmentMappingRepository;

    @Override
    @Transactional
    public String registerAppointment(CreateAppointmentReqDto reqDto, Long userId, String name, UserAuthType userAuthType) {
        String appointmentCode = UuidUtils.generateRandomString(12);
        Appointment appointment = appointmentMapper.to(reqDto, AppointmentState.UNCONFIRMED, appointmentCode);
        appointmentRepository.save(appointment);

        UserAppointmentMapping userAppointmentMapping = UserAppointmentMapping.of(appointmentCode, userId, UserRole.HOST, name, null, userAuthType);
        userAppointmentMappingRepository.save(userAppointmentMapping);

        return appointmentCode;
    }

    @Override
    public AppointmentInfoRespDto retrieveAppointmentInfo(String appointmentCode, Long userId, String name) {
        Appointment appointment = appointmentRepository.findByAppointmentCode(appointmentCode)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        List<UserAppointmentMapping> userAppointmentMappingList = userAppointmentMappingRepository.findAllByAppointmentCode(appointmentCode);
        AppointmentValidator.validateUserAppointmentMappingExistence(userId, userAppointmentMappingList);

        String hostName = AppointmentValidator.findHostName(userAppointmentMappingList);
        boolean isHost = StringUtils.equals(hostName, name);
        boolean isGuest = AppointmentValidator.isUserGuest(userId, userAppointmentMappingList);

        List<UserParticipationInfo> userParticipationInfoList = AppointmentUtils.retrieveuserParticipationInfoList(userAppointmentMappingList, hostName);

        return appointmentMapper.to(appointment, userParticipationInfoList, hostName, isHost, isGuest);
    }

    @Override
    @Transactional
    public void updateAppointment(UpdateAppointmentReqDto reqDto, Long userId) {
        Appointment appointment = appointmentRepository.findByAppointmentCode(reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));
        AppointmentValidator.validateAppointmentStateUnconfirmed(appointment);

        List<UserAppointmentMapping> userAppointmentMappingList = userAppointmentMappingRepository.findAllByAppointmentCode(reqDto.getAppointmentCode());
        AppointmentValidator.validateIsUserHost(userId, userAppointmentMappingList);

        userAppointmentMappingRepository.deleteAllByAppointmentCodeAndUserRole(appointment.getAppointmentCode(), UserRole.GUEST);
        appointment.update(reqDto.getAppointmentName(), reqDto.getAddress(), reqDto.getCandidateDateList(), reqDto.getNotice());
    }

    @Override
    @Transactional
    public void deleteAppointment(String appointmentCode, Long userId) {
        Appointment appointment = appointmentRepository.findByAppointmentCode(appointmentCode)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));
        AppointmentValidator.validateAppointmentStateUnconfirmed(appointment);

        List<UserAppointmentMapping> userAppointmentMappingList = userAppointmentMappingRepository.findAllByAppointmentCode(appointmentCode);
        AppointmentValidator.validateIsUserHost(userId, userAppointmentMappingList);

        userAppointmentMappingRepository.deleteAllByAppointmentCode(appointmentCode);
        appointmentRepository.deleteById(appointment.getId());
    }

    @Override
    @Transactional
    public void registerAppointmentJoin(CreateAppointmentJoinReqDto reqDto, Long userId, String name) {
        Appointment appointment = appointmentRepository.findByAppointmentCode(reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));
        AppointmentValidator.validateAppointmentStateUnconfirmed(appointment);

        List<UserAppointmentMapping> userAppointmentMappingList = userAppointmentMappingRepository.findAllByAppointmentCode(reqDto.getAppointmentCode());
        AppointmentValidator.validateIsUserNotGuest(userId, userAppointmentMappingList);

        if (!AppointmentValidator.isUserHost(userId, userAppointmentMappingList)) {
            AppointmentValidator.validateNotDuplicatedName(reqDto.getNickname(), userAppointmentMappingList);
        }

        AppointmentValidator.retrieveInvalidSelectedDateTime(appointment.getCandidateDateList(), reqDto.getSelectedDateTimeList())
                .ifPresent(result -> {
                    throw new AppointmentException(ErrorCode.PARAMETER_VALIDATION_FAIL, String.format("후보 날짜에서 벗어난 입력 값 입니다. %s", result));
                });

        UserAppointmentMapping userAppointmentMapping = UserAppointmentMapping.of(reqDto.getAppointmentCode(), userId, UserRole.GUEST, reqDto.getNickname(), null, UserAuthType.LOCAL);
        userAppointmentMapping.update(reqDto.getSelectedDateTimeList());

        userAppointmentMappingRepository.save(userAppointmentMapping);
    }

    @Override
    public List<AppointmentParticipantsRespDto> retrieveAppointmentParticipants(String appointmentCode, Long userId) {
        validateAppointmentExistence(appointmentCode);

        List<UserAppointmentMapping> userAppointmentMappingList = userAppointmentMappingRepository.findAllByAppointmentCode(appointmentCode);
        AppointmentValidator.validateIsUserHostOrGuest(userId, userAppointmentMappingList);

        String hostName = AppointmentValidator.findHostName(userAppointmentMappingList);

        List<ParticipationInfo> participationInfoList = AppointmentValidator.convertToParticipationInfoList(userAppointmentMappingList, hostName, appointmentMapper);

        return participationInfoList.stream().map(appointmentMapper::to).toList();
    }

    @Override
    public AppointmentMyParticipantRespDto retrieveAppointmentMyParticipant(String appointmentCode, Long userId) {
        validateAppointmentExistence(appointmentCode);

        List<UserAppointmentMapping> userAppointmentMappingList = userAppointmentMappingRepository.findAllByAppointmentCode(appointmentCode);
        AppointmentValidator.validateIsUserHostOrGuest(userId, userAppointmentMappingList);

        String hostName = AppointmentValidator.findHostName(userAppointmentMappingList);

        UserAppointmentMapping userAppointmentMapping = userAppointmentMappingList.stream()
                .filter(mapping -> UserRole.GUEST.equals(mapping.getUserRole()) && userId.equals(mapping.getUserSeq()))
                .findFirst()
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_USER_APPOINTMENT_MAPPING));

        ParticipationInfo participationInfo = appointmentMapper.toParticipationInfo(
                userAppointmentMapping, StringUtils.equals(hostName, userAppointmentMapping.getNickname()) ? UserRole.HOST : UserRole.GUEST);

        return appointmentMapper.toAppointmentParticipantRespDto(participationInfo);
    }

    @Override
    @Transactional
    public void updateAppointmentJoin(UpdateAppointmentJoinReqDto reqDto, Long userId) {
        Appointment appointment = appointmentRepository.findByAppointmentCode(reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));
        AppointmentValidator.validateAppointmentStateUnconfirmed(appointment);

        List<UserAppointmentMapping> userAppointmentMappingList = userAppointmentMappingRepository.findAllByAppointmentCode(reqDto.getAppointmentCode());
        UserAppointmentMapping mapping = AppointmentValidator.findGuestMapping(userId, userAppointmentMappingList);

        AppointmentValidator.retrieveInvalidSelectedDateTime(appointment.getCandidateDateList(), reqDto.getSelectedDateTimeList())
                .ifPresent(result -> {
                    throw new AppointmentException(ErrorCode.PARAMETER_VALIDATION_FAIL,
                            String.format("후보 날짜에서 벗어난 입력 값 입니다. %s", result));
                });

        mapping.update(reqDto.getSelectedDateTimeList());
    }

    @Override
    @Transactional
    public void deleteAppointmentJoin(String appointmentCode, Long userId) {
        Appointment appointment = appointmentRepository.findByAppointmentCode(appointmentCode)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));
        AppointmentValidator.validateAppointmentStateUnconfirmed(appointment);

        List<UserAppointmentMapping> userAppointmentMappingList = userAppointmentMappingRepository.findAllByAppointmentCode(appointmentCode);
        UserAppointmentMapping mapping = AppointmentValidator.findGuestMapping(userId, userAppointmentMappingList);

        userAppointmentMappingRepository.deleteById(mapping.getId());
    }

    @Override
    public Page<AppointmentCandidateInfoRespDto> retrieveCandidateInfo(AppointmentCandidateInfoReqDto reqDto, Long userId) {
        Appointment appointment = appointmentRepository.findByAppointmentCode(reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));
        AppointmentValidator.validateAppointmentStateUnconfirmed(appointment);

        List<UserAppointmentMapping> userAppointmentMappingList = userAppointmentMappingRepository.findAllByAppointmentCode(reqDto.getAppointmentCode());
        AppointmentValidator.validateIsUserHost(userId, userAppointmentMappingList);

        CustomPageRequest customPageRequest = CustomPageRequest.of(reqDto.getPage(), reqDto.getSize());

        List<UserAppointmentMapping> participationInfoList =
                userAppointmentMappingRepository.findAllByAppointmentCodeAndUserRole(reqDto.getAppointmentCode(), UserRole.GUEST);

        String hostName = AppointmentValidator.findHostName(participationInfoList);

        List<AppointmentCandidateInfo> appointmentCandidateInfos =
                AppointmentUtils.retrieveCandidateInfoList(appointment.getCandidateDateList(), participationInfoList, hostName);

        List<AppointmentCandidateInfoRespDto> dataList = appointmentCandidateInfos.stream()
                .skip(Integer.toUnsignedLong((reqDto.getPage() - 1) * reqDto.getSize()))
                .limit(reqDto.getSize())
                .map(appointmentMapper::to)
                .toList();

        return new PageImpl<>(dataList, customPageRequest, appointmentCandidateInfos.size());
    }

    @Override
    @Transactional
    public void confirmAppointment(ConfirmAppointmentReqDto reqDto, Long userId) {
        Appointment appointment = appointmentRepository.findByAppointmentCode(reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));
        AppointmentValidator.validateAppointmentStateUnconfirmed(appointment);

        List<UserAppointmentMapping> userAppointmentMappingList = userAppointmentMappingRepository.findAllByAppointmentCode(reqDto.getAppointmentCode());
        AppointmentValidator.validateIsUserHost(userId, userAppointmentMappingList);

        List<UserAppointmentMapping> userGuestList =
                userAppointmentMappingRepository.findAllByAppointmentCodeAndUserRole(appointment.getAppointmentCode(), UserRole.GUEST);

        ConfirmedDateTime confirmedDateTime = reqDto.getConfirmedDateTime();

        AppointmentUtils.retrieveAvailableUserList(confirmedDateTime, userGuestList)
                .forEach(userAppointmentMapping -> userAppointmentMapping.updateIsAvailable(true));

        appointment.confirmed(confirmedDateTime);
    }

    @Override
    public Page<AppointmentSearchRespDto> retrieveAppointmentSearchList(AppointmentSearchReqDto reqDto, Long userId) {
        CustomPageRequest customPageRequest = CustomPageRequest.of(reqDto.getPage(), reqDto.getSize());
        return customAppointmentRepository.findAppointmentSearchListRespDtoListByKeywordAndUserSeq(reqDto.getKeyword(), userId, customPageRequest);
    }

    @Override
    public AppointmentPreviewRespDto retrieveAppointmentPreview(String appointmentCode) {
        Appointment appointment = appointmentRepository.findByAppointmentCode(appointmentCode)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        List<UserAppointmentMapping> mappingList = userAppointmentMappingRepository.findAllByAppointmentCode(appointmentCode);

        String hostName = AppointmentValidator.findHostName(mappingList);

        return appointmentMapper.to(appointment, hostName);
    }

}
