package com.example.gatherplan.appointment.service.impl;

import com.example.gatherplan.appointment.dto.*;
import com.example.gatherplan.appointment.enums.AppointmentState;
import com.example.gatherplan.appointment.enums.UserAuthType;
import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.exception.AppointmentException;
import com.example.gatherplan.appointment.exception.UserException;
import com.example.gatherplan.appointment.mapper.TempAppointmentMapper;
import com.example.gatherplan.appointment.repository.AppointmentRepository;
import com.example.gatherplan.appointment.repository.UserAppointmentMappingRepository;
import com.example.gatherplan.appointment.repository.UserRepository;
import com.example.gatherplan.appointment.repository.entity.Appointment;
import com.example.gatherplan.appointment.repository.entity.User;
import com.example.gatherplan.appointment.repository.entity.UserAppointmentMapping;
import com.example.gatherplan.appointment.service.TempAppointmentService;
import com.example.gatherplan.appointment.utils.AppointmentUtils;
import com.example.gatherplan.appointment.utils.unit.AppointmentCandidateInfo;
import com.example.gatherplan.appointment.validator.AppointmentValidator;
import com.example.gatherplan.common.exception.ErrorCode;
import com.example.gatherplan.common.unit.ConfirmedDateTime;
import com.example.gatherplan.common.unit.CustomPageRequest;
import com.example.gatherplan.common.unit.ParticipationInfo;
import com.example.gatherplan.common.unit.UserParticipationInfo;
import com.example.gatherplan.common.utils.MailUtils;
import com.example.gatherplan.common.utils.UuidUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class TempAppointmentServiceImpl implements TempAppointmentService {

    private final TempAppointmentMapper tempAppointmentMapper;

    private final AppointmentRepository appointmentRepository;
    private final UserAppointmentMappingRepository userAppointmentMappingRepository;

    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;
    @Value("${spring.mail.username}")
    private String adminEmail;

    @Override
    @Transactional
    public String registerAppointment(CreateTempAppointmentReqDto reqDto) {
        String appointmentCode = UuidUtils.generateRandomString(12);
        Appointment appointment = tempAppointmentMapper.to(reqDto, AppointmentState.UNCONFIRMED, appointmentCode);
        appointmentRepository.save(appointment);

        UserAppointmentMapping userAppointmentMapping = UserAppointmentMapping.ofTempUser(appointmentCode, UserRole.HOST, reqDto.getTempUserInfo(), UserAuthType.TEMPORARY);
        userAppointmentMappingRepository.save(userAppointmentMapping);

        return appointmentCode;
    }

    @Override
    public TempAppointmentInfoRespDto retrieveAppointmentInfo(TempAppointmentInfoReqDto reqDto) {
        Appointment appointment = appointmentRepository.findByAppointmentCode(reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.APPOINTMENT_NOT_FOUND));

        List<UserAppointmentMapping> userAppointmentMappingList = userAppointmentMappingRepository.findAllByAppointmentCode(reqDto.getAppointmentCode());
        AppointmentValidator.validateTempUserExistenceAndThrow(reqDto.getTempUserInfo(), userAppointmentMappingList);

        String hostName = AppointmentUtils.findHostName(userAppointmentMappingList);
        boolean isHost = StringUtils.equals(reqDto.getTempUserInfo().getNickname(), hostName);
        boolean isGuest = AppointmentValidator.isUserParticipated(reqDto.getTempUserInfo(), userAppointmentMappingList);

        List<UserParticipationInfo> userParticipationInfoList = AppointmentUtils.retrieveUserParticipationInfoList(userAppointmentMappingList, hostName);

        return tempAppointmentMapper.to(appointment, userParticipationInfoList, hostName, isHost, isGuest);
    }

    @Override
    @Transactional
    public void updateAppointment(UpdateTempAppointmentReqDto reqDto) {
        Appointment appointment = appointmentRepository.findByAppointmentCode(reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.APPOINTMENT_NOT_FOUND));
        AppointmentValidator.validateAppointmentStateUnconfirmedAndThrow(appointment);

        List<UserAppointmentMapping> userAppointmentMappingList = userAppointmentMappingRepository.findAllByAppointmentCode(reqDto.getAppointmentCode());
        AppointmentValidator.validateIsUserHostAndThrow(reqDto.getTempUserInfo(), userAppointmentMappingList);

        userAppointmentMappingRepository.deleteAllByAppointmentCodeAndUserRole(appointment.getAppointmentCode(), UserRole.GUEST);
        appointment.update(reqDto.getAppointmentName(), reqDto.getAddress(), reqDto.getCandidateDateList(), reqDto.getNotice());
    }

    @Override
    @Transactional
    public void deleteAppointment(DeleteTempAppointmentReqDto reqDto) {
        Appointment appointment = appointmentRepository.findByAppointmentCode(reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.APPOINTMENT_NOT_FOUND));
        AppointmentValidator.validateAppointmentStateUnconfirmedAndThrow(appointment);

        List<UserAppointmentMapping> userAppointmentMappingList = userAppointmentMappingRepository.findAllByAppointmentCode(reqDto.getAppointmentCode());
        AppointmentValidator.validateIsUserHostAndThrow(reqDto.getTempUserInfo(), userAppointmentMappingList);

        userAppointmentMappingRepository.deleteAllByAppointmentCode(reqDto.getAppointmentCode());
        appointmentRepository.deleteById(appointment.getId());
    }

    @Override
    @Transactional
    public void registerAppointmentJoin(CreateTempAppointmentJoinReqDto reqDto) {
        Appointment appointment = appointmentRepository.findByAppointmentCode(reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.APPOINTMENT_NOT_FOUND));
        AppointmentValidator.validateAppointmentStateUnconfirmedAndThrow(appointment);

        List<UserAppointmentMapping> userAppointmentMappingList
                = userAppointmentMappingRepository.findAllByAppointmentCode(reqDto.getAppointmentCode());
        AppointmentValidator.validateIsUserJoined(reqDto.getTempUserInfo(), userAppointmentMappingList);

        if (!AppointmentValidator.isUserHost(reqDto.getTempUserInfo(), userAppointmentMappingList)) {
            AppointmentValidator.validateNotDuplicatedName(reqDto.getTempUserInfo().getNickname(), userAppointmentMappingList);
        }

        AppointmentValidator.validateSelectedDateTimeAndThrow(appointment.getCandidateDateList(), reqDto.getSelectedDateTimeList());

        UserAppointmentMapping userAppointmentMapping =
                UserAppointmentMapping.ofTempUser(reqDto.getAppointmentCode(), UserRole.GUEST, reqDto.getTempUserInfo(), UserAuthType.TEMPORARY);
        userAppointmentMapping.update(reqDto.getSelectedDateTimeList());

        userAppointmentMappingRepository.save(userAppointmentMapping);
        UserAppointmentMapping hostMapping = AppointmentUtils.findHost(userAppointmentMappingList);
        if (!UserAuthType.TEMPORARY.equals(hostMapping.getUserAuthType())) {
            String hostEmail = userRepository.findById(hostMapping.getUserSeq()).map(User::getEmail)
                    .orElseThrow(() -> new UserException(ErrorCode.HOST_NOT_FOUND_IN_APPOINTMENT));
            String subject = String.format("'%s' 약속에 새로운 인원이 참여했습니다.", appointment.getAppointmentName());
            String content = String.format("'%s' 님이 약속에 새로 참여했습니다.", reqDto.getTempUserInfo().getNickname());
            MailUtils.sendEmail(adminEmail, hostEmail, subject, content, javaMailSender);
        }
    }

    @Override
    public List<TempAppointmentParticipantsRespDto> retrieveAppointmentParticipants(TempAppointmentParticipantsReqDto reqDto) {
        if (!appointmentRepository.existsByAppointmentCode(reqDto.getAppointmentCode()))
            throw new AppointmentException(ErrorCode.APPOINTMENT_NOT_FOUND);

        List<UserAppointmentMapping> userAppointmentMappingList =
                userAppointmentMappingRepository.findAllByAppointmentCode(reqDto.getAppointmentCode());
        AppointmentValidator.validateIsUserHostOrJoinedAndThrow(reqDto.getTempUserInfo(), userAppointmentMappingList);

        String hostName = AppointmentUtils.findHostName(userAppointmentMappingList);

        List<ParticipationInfo> participationInfoList =
                AppointmentUtils.convertToParticipationInfoList(userAppointmentMappingList, hostName, tempAppointmentMapper);

        return participationInfoList.stream().map(tempAppointmentMapper::to).toList();
    }

    @Override
    public TempAppointmentMyParticipantRespDto retrieveAppointmentMyParticipant(TempAppointmentMyParticipantReqDto reqDto) {
        if (!appointmentRepository.existsByAppointmentCode(reqDto.getAppointmentCode()))
            throw new AppointmentException(ErrorCode.APPOINTMENT_NOT_FOUND);

        List<UserAppointmentMapping> userAppointmentMappingList =
                userAppointmentMappingRepository.findAllByAppointmentCode(reqDto.getAppointmentCode());
        AppointmentValidator.validateIsUserHostOrJoinedAndThrow(reqDto.getTempUserInfo(), userAppointmentMappingList);

        String hostName = AppointmentUtils.findHostName(userAppointmentMappingList);

        UserAppointmentMapping userAppointmentMapping = AppointmentUtils.findGuestMapping(reqDto.getTempUserInfo(), userAppointmentMappingList);

        ParticipationInfo participationInfo = tempAppointmentMapper.toParticipationInfo(
                userAppointmentMapping, StringUtils.equals(hostName, userAppointmentMapping.getNickname()) ? UserRole.HOST : UserRole.GUEST);

        return tempAppointmentMapper.toAppointmentParticipantRespDto(participationInfo);
    }

    @Override
    @Transactional
    public void updateAppointmentJoin(UpdateTempAppointmentJoinReqDto reqDto) {
        Appointment appointment = appointmentRepository.findByAppointmentCode(reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.APPOINTMENT_NOT_FOUND));
        AppointmentValidator.validateAppointmentStateUnconfirmedAndThrow(appointment);

        List<UserAppointmentMapping> userAppointmentMappingList = userAppointmentMappingRepository.findAllByAppointmentCode(reqDto.getAppointmentCode());
        UserAppointmentMapping mapping = AppointmentUtils.findGuestMapping(reqDto.getTempUserInfo(), userAppointmentMappingList);

        AppointmentValidator.validateSelectedDateTimeAndThrow(appointment.getCandidateDateList(), reqDto.getSelectedDateTimeList());

        mapping.update(reqDto.getSelectedDateTimeList());
    }

    @Override
    @Transactional
    public void deleteAppointmentJoin(DeleteTempAppointmentJoinReqDto reqDto) {
        Appointment appointment = appointmentRepository.findByAppointmentCode(reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.APPOINTMENT_NOT_FOUND));
        AppointmentValidator.validateAppointmentStateUnconfirmedAndThrow(appointment);

        List<UserAppointmentMapping> userAppointmentMappingList = userAppointmentMappingRepository.findAllByAppointmentCode(reqDto.getAppointmentCode());
        UserAppointmentMapping mapping = AppointmentUtils.findGuestMapping(reqDto.getTempUserInfo(), userAppointmentMappingList);

        userAppointmentMappingRepository.deleteById(mapping.getId());
    }

    @Override
    public Page<TempAppointmentCandidateInfoRespDto> retrieveCandidateInfo(TempAppointmentCandidateInfoReqDto reqDto) {
        Appointment appointment = appointmentRepository.findByAppointmentCode(reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.APPOINTMENT_NOT_FOUND));
        AppointmentValidator.validateAppointmentStateUnconfirmedAndThrow(appointment);

        List<UserAppointmentMapping> userAppointmentMappingList = userAppointmentMappingRepository.findAllByAppointmentCode(reqDto.getAppointmentCode());
        AppointmentValidator.validateIsUserHostAndThrow(reqDto.getTempUserInfo(), userAppointmentMappingList);

        CustomPageRequest customPageRequest = CustomPageRequest.of(reqDto.getPage(), reqDto.getSize());

        List<UserAppointmentMapping> participationInfoList =
                userAppointmentMappingRepository.findAllByAppointmentCodeAndUserRole(reqDto.getAppointmentCode(), UserRole.GUEST);

        String hostName = AppointmentUtils.findHostName(userAppointmentMappingList);

        List<AppointmentCandidateInfo> appointmentCandidateInfos =
                AppointmentUtils.retrieveCandidateInfoList(appointment.getCandidateDateList(), participationInfoList, hostName);

        List<TempAppointmentCandidateInfoRespDto> dataList = appointmentCandidateInfos.stream()
                .skip(Integer.toUnsignedLong((reqDto.getPage() - 1) * reqDto.getSize()))
                .limit(reqDto.getSize())
                .map(tempAppointmentMapper::to)
                .toList();

        return new PageImpl<>(dataList, customPageRequest, appointmentCandidateInfos.size());
    }

    @Override
    @Transactional
    public void confirmAppointment(TempConfirmAppointmentReqDto reqDto) {
        Appointment appointment = appointmentRepository.findByAppointmentCode(reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.APPOINTMENT_NOT_FOUND));
        AppointmentValidator.validateAppointmentStateUnconfirmedAndThrow(appointment);

        List<UserAppointmentMapping> userAppointmentMappingList = userAppointmentMappingRepository.findAllByAppointmentCode(reqDto.getAppointmentCode());
        AppointmentValidator.validateIsUserHostAndThrow(reqDto.getTempUserInfo(), userAppointmentMappingList);

        List<UserAppointmentMapping> userGuestList =
                userAppointmentMappingRepository.findAllByAppointmentCodeAndUserRole(appointment.getAppointmentCode(), UserRole.GUEST);

        ConfirmedDateTime confirmedDateTime = reqDto.getConfirmedDateTime();

        AppointmentUtils.retrieveAvailableUserList(confirmedDateTime, userGuestList)
                .forEach(userAppointmentMapping -> userAppointmentMapping.updateIsAvailable(true));

        appointment.confirmed(confirmedDateTime);
    }

}
