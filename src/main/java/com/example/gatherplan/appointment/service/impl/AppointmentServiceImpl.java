package com.example.gatherplan.appointment.service.impl;

import com.example.gatherplan.appointment.dto.*;
import com.example.gatherplan.appointment.enums.AppointmentState;
import com.example.gatherplan.appointment.enums.UserAuthType;
import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.exception.AppointmentException;
import com.example.gatherplan.appointment.mapper.AppointmentMapper;
import com.example.gatherplan.appointment.repository.AppointmentRepository;
import com.example.gatherplan.appointment.repository.CustomAppointmentRepository;
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
    public String registerAppointment(CreateAppointmentReqDto reqDto, Long userId, String name, UserAuthType userAuthType) {
        String appointmentCode = UuidUtils.generateRandomString(12);
        Appointment appointment = appointmentMapper.to(reqDto, AppointmentState.UNCONFIRMED, appointmentCode);
        appointmentRepository.save(appointment);

        UserAppointmentMapping userAppointmentMapping = UserAppointmentMapping.ofUser(appointmentCode, userId, UserRole.HOST, name, userAuthType);
        userAppointmentMappingRepository.save(userAppointmentMapping);

        return appointmentCode;
    }

    @Override
    public AppointmentInfoRespDto retrieveAppointmentInfo(String appointmentCode, Long userId, String name) {
        Appointment appointment = appointmentRepository.findByAppointmentCode(appointmentCode)
                .orElseThrow(() -> new AppointmentException(ErrorCode.APPOINTMENT_NOT_FOUND));

        List<UserAppointmentMapping> userAppointmentMappingList =
                userAppointmentMappingRepository.findAllByAppointmentCode(appointmentCode);
        AppointmentValidator.validateUserExistenceAndThrow(userId, userAppointmentMappingList);

        String hostName = AppointmentUtils.findHostName(userAppointmentMappingList);
        boolean isHost = StringUtils.equals(hostName, name);
        boolean isGuest = AppointmentValidator.isUserParticipated(userId, userAppointmentMappingList);

        List<UserParticipationInfo> userParticipationInfoList =
                AppointmentUtils.retrieveUserParticipationInfoList(userAppointmentMappingList, hostName);

        return appointmentMapper.to(appointment, userParticipationInfoList, hostName, isHost, isGuest);
    }

    @Override
    @Transactional
    public void updateAppointment(UpdateAppointmentReqDto reqDto, Long userId) {
        Appointment appointment = appointmentRepository.findByAppointmentCode(reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.APPOINTMENT_NOT_FOUND));

        AppointmentValidator.validateAppointmentStateUnconfirmedAndThrow(appointment);

        List<UserAppointmentMapping> userAppointmentMappingList =
                userAppointmentMappingRepository.findAllByAppointmentCode(reqDto.getAppointmentCode());
        AppointmentValidator.validateIsUserHostAndThrow(userId, userAppointmentMappingList);

        userAppointmentMappingRepository.deleteAllByAppointmentCodeAndUserRole(appointment.getAppointmentCode(), UserRole.GUEST);
        appointment.update(reqDto.getAppointmentName(), reqDto.getAddress(), reqDto.getCandidateDateList(), reqDto.getNotice());
    }

    @Override
    @Transactional
    public void deleteAppointment(String appointmentCode, Long userId) {
        Appointment appointment = appointmentRepository.findByAppointmentCode(appointmentCode)
                .orElseThrow(() -> new AppointmentException(ErrorCode.APPOINTMENT_NOT_FOUND));
        AppointmentValidator.validateAppointmentStateUnconfirmedAndThrow(appointment);

        List<UserAppointmentMapping> userAppointmentMappingList =
                userAppointmentMappingRepository.findAllByAppointmentCode(appointmentCode);
        AppointmentValidator.validateIsUserHostAndThrow(userId, userAppointmentMappingList);

        userAppointmentMappingRepository.deleteAllByAppointmentCode(appointmentCode);
        appointmentRepository.deleteById(appointment.getId());
    }

    @Override
    @Transactional
    public void registerAppointmentJoin(CreateAppointmentJoinReqDto reqDto, Long userId, String name) {
        Appointment appointment = appointmentRepository.findByAppointmentCode(reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.APPOINTMENT_NOT_FOUND));
        AppointmentValidator.validateAppointmentStateUnconfirmedAndThrow(appointment);

        List<UserAppointmentMapping> userAppointmentMappingList =
                userAppointmentMappingRepository.findAllByAppointmentCode(reqDto.getAppointmentCode());
        AppointmentValidator.validateIsUserJoined(userId, userAppointmentMappingList);

        if (!AppointmentValidator.isUserHost(userId, userAppointmentMappingList)) {
            AppointmentValidator.validateNotDuplicatedName(reqDto.getNickname(), userAppointmentMappingList);
        }

        AppointmentValidator.validateSelectedDateTimeAndThrow(appointment.getCandidateDateList(), reqDto.getSelectedDateTimeList());

        UserAppointmentMapping userAppointmentMapping = UserAppointmentMapping.ofUser(reqDto.getAppointmentCode(), userId, UserRole.GUEST, reqDto.getNickname(), UserAuthType.LOCAL);
        userAppointmentMapping.update(reqDto.getSelectedDateTimeList());

        userAppointmentMappingRepository.save(userAppointmentMapping);
    }

    @Override
    public List<AppointmentParticipantsRespDto> retrieveAppointmentParticipants(String appointmentCode, Long userId) {
        if (!appointmentRepository.existsByAppointmentCode(appointmentCode))
            throw new AppointmentException(ErrorCode.APPOINTMENT_NOT_FOUND);

        List<UserAppointmentMapping> userAppointmentMappingList =
                userAppointmentMappingRepository.findAllByAppointmentCode(appointmentCode);
        AppointmentValidator.validateIsUserHostOrJoinedAndThrow(userId, userAppointmentMappingList);

        String hostName = AppointmentUtils.findHostName(userAppointmentMappingList);

        List<ParticipationInfo> participationInfoList =
                AppointmentUtils.convertToParticipationInfoList(userAppointmentMappingList, hostName, appointmentMapper);

        return participationInfoList.stream().map(appointmentMapper::to).toList();
    }

    @Override
    public AppointmentMyParticipantRespDto retrieveAppointmentMyParticipant(String appointmentCode, Long userId) {
        if (!appointmentRepository.existsByAppointmentCode(appointmentCode))
            throw new AppointmentException(ErrorCode.APPOINTMENT_NOT_FOUND);

        List<UserAppointmentMapping> userAppointmentMappingList =
                userAppointmentMappingRepository.findAllByAppointmentCode(appointmentCode);
        AppointmentValidator.validateIsUserHostOrJoinedAndThrow(userId, userAppointmentMappingList);

        String hostName = AppointmentUtils.findHostName(userAppointmentMappingList);

        UserAppointmentMapping userAppointmentMapping = AppointmentUtils.findGuestMapping(userId, userAppointmentMappingList);

        ParticipationInfo participationInfo = appointmentMapper.toParticipationInfo(
                userAppointmentMapping, StringUtils.equals(hostName, userAppointmentMapping.getNickname()) ? UserRole.HOST : UserRole.GUEST);

        return appointmentMapper.toAppointmentParticipantRespDto(participationInfo);
    }

    @Override
    @Transactional
    public void updateAppointmentJoin(UpdateAppointmentJoinReqDto reqDto, Long userId) {
        Appointment appointment = appointmentRepository.findByAppointmentCode(reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.APPOINTMENT_NOT_FOUND));
        AppointmentValidator.validateAppointmentStateUnconfirmedAndThrow(appointment);

        List<UserAppointmentMapping> userAppointmentMappingList =
                userAppointmentMappingRepository.findAllByAppointmentCode(reqDto.getAppointmentCode());
        UserAppointmentMapping mapping = AppointmentUtils.findGuestMapping(userId, userAppointmentMappingList);

        AppointmentValidator.validateSelectedDateTimeAndThrow(appointment.getCandidateDateList(), reqDto.getSelectedDateTimeList());

        mapping.update(reqDto.getSelectedDateTimeList());
    }

    @Override
    @Transactional
    public void deleteAppointmentJoin(String appointmentCode, Long userId) {
        Appointment appointment = appointmentRepository.findByAppointmentCode(appointmentCode)
                .orElseThrow(() -> new AppointmentException(ErrorCode.APPOINTMENT_NOT_FOUND));
        AppointmentValidator.validateAppointmentStateUnconfirmedAndThrow(appointment);

        List<UserAppointmentMapping> userAppointmentMappingList =
                userAppointmentMappingRepository.findAllByAppointmentCode(appointmentCode);
        UserAppointmentMapping mapping = AppointmentUtils.findGuestMapping(userId, userAppointmentMappingList);

        userAppointmentMappingRepository.deleteById(mapping.getId());
    }

    @Override
    public Page<AppointmentCandidateInfoRespDto> retrieveCandidateInfo(AppointmentCandidateInfoReqDto reqDto, Long userId) {
        Appointment appointment = appointmentRepository.findByAppointmentCode(reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.APPOINTMENT_NOT_FOUND));
        AppointmentValidator.validateAppointmentStateUnconfirmedAndThrow(appointment);

        List<UserAppointmentMapping> userAppointmentMappingList =
                userAppointmentMappingRepository.findAllByAppointmentCode(reqDto.getAppointmentCode());
        AppointmentValidator.validateIsUserHostAndThrow(userId, userAppointmentMappingList);

        CustomPageRequest customPageRequest = CustomPageRequest.of(reqDto.getPage(), reqDto.getSize());

        List<UserAppointmentMapping> participationInfoList =
                userAppointmentMappingRepository.findAllByAppointmentCodeAndUserRole(reqDto.getAppointmentCode(), UserRole.GUEST);

        String hostName = AppointmentUtils.findHostName(userAppointmentMappingList);

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
                .orElseThrow(() -> new AppointmentException(ErrorCode.APPOINTMENT_NOT_FOUND));
        AppointmentValidator.validateAppointmentStateUnconfirmedAndThrow(appointment);

        List<UserAppointmentMapping> userAppointmentMappingList =
                userAppointmentMappingRepository.findAllByAppointmentCode(reqDto.getAppointmentCode());
        AppointmentValidator.validateIsUserHostAndThrow(userId, userAppointmentMappingList);

        List<UserAppointmentMapping> userGuestList =
                userAppointmentMappingRepository.findAllByAppointmentCodeAndUserRole(appointment.getAppointmentCode(), UserRole.GUEST);

        ConfirmedDateTime confirmedDateTime = reqDto.getConfirmedDateTime();

        AppointmentUtils.retrieveAvailableUserList(confirmedDateTime, userGuestList)
                .forEach(userAppointmentMapping -> userAppointmentMapping.updateIsAvailable(true));

        appointment.confirmed(confirmedDateTime);
    }

    @Override
    public Page<AppointmentSearchRespDto> retrieveAppointmentSearchList(AppointmentSearchReqDto reqDto, Long userId, String name) {
        CustomPageRequest customPageRequest = CustomPageRequest.of(reqDto.getPage(), reqDto.getSize());

        Page<Appointment> pagedAppointmentList = customAppointmentRepository.findAllByUserIdAndKeywordContaining(userId, reqDto.getKeyword(), customPageRequest);

        List<Appointment> appointmentList = pagedAppointmentList.getContent();

        List<String> appointmentCodeList = AppointmentUtils.findAppointmentCodeListByAppointmentList(appointmentList);

        List<UserAppointmentMapping> hostMappingList =
                userAppointmentMappingRepository.findByAppointmentCodeInAndUserRole(appointmentCodeList, UserRole.HOST);
        Map<String, String> hostNames = AppointmentUtils.findHostNameList(hostMappingList);

        return pagedAppointmentList.map(mapping -> appointmentMapper.toAppointmentSearchListRespDto(mapping,
                hostNames.get(mapping.getAppointmentCode()), name.equals(hostNames.get(mapping.getAppointmentCode()))));
    }

    @Override
    public AppointmentPreviewRespDto retrieveAppointmentPreview(String appointmentCode) {
        Appointment appointment = appointmentRepository.findByAppointmentCode(appointmentCode)
                .orElseThrow(() -> new AppointmentException(ErrorCode.APPOINTMENT_NOT_FOUND));

        List<UserAppointmentMapping> mappingList =
                userAppointmentMappingRepository.findAllByAppointmentCode(appointmentCode);

        String hostName = AppointmentUtils.findHostName(mappingList);

        return appointmentMapper.to(appointment, hostName);
    }

}