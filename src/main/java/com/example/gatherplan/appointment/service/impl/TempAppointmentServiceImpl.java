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
import com.example.gatherplan.appointment.repository.entity.Appointment;
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
import com.example.gatherplan.common.utils.UuidUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class TempAppointmentServiceImpl implements TempAppointmentService {

    private final TempAppointmentMapper tempAppointmentMapper;

    private final AppointmentRepository appointmentRepository;
    private final UserAppointmentMappingRepository userAppointmentMappingRepository;

    @Override
    @Transactional
    public String registerAppointment(CreateTempAppointmentReqDto reqDto) {
        String appointmentCode = UuidUtils.generateRandomString(12);

        Appointment appointment = tempAppointmentMapper.to(reqDto, AppointmentState.UNCONFIRMED, appointmentCode);

        appointmentRepository.save(appointment);

        UserAppointmentMapping userAppointmentMapping = UserAppointmentMapping.builder()
                .appointmentCode(appointmentCode)
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
        Appointment appointment = appointmentRepository.findByAppointmentCode(reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        List<UserAppointmentMapping> userAppointmentMappingList =
                userAppointmentMappingRepository.findAllByAppointmentCode(reqDto.getAppointmentCode());

        boolean isNotExists = userAppointmentMappingList.stream()
                .noneMatch(mapping -> mapping.getAppointmentCode().equals(reqDto.getAppointmentCode())
                        && mapping.getNickname().equals(reqDto.getTempUserInfo().getNickname())
                        && mapping.getTempPassword().equals(reqDto.getTempUserInfo().getPassword()));

        if (isNotExists) {
            throw new AppointmentException(ErrorCode.NOT_FOUND_USER_APPOINTMENT_MAPPING);
        }

        UserAppointmentMapping hostMapping = userAppointmentMappingList.stream()
                .filter(mapping -> UserRole.HOST.equals(mapping.getUserRole()))
                .findFirst().orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_HOST));

        String hostName = hostMapping.getNickname();

        boolean isHost = StringUtils.equals(hostMapping.getNickname(), reqDto.getTempUserInfo().getNickname());

        boolean isParticipated = userAppointmentMappingList.stream()
                .anyMatch(mapping -> StringUtils.equals(reqDto.getTempUserInfo().getNickname(), mapping.getNickname()) &&
                        StringUtils.equals(reqDto.getTempUserInfo().getPassword(), mapping.getTempPassword()) && UserRole.GUEST.equals(mapping.getUserRole()));

        List<UserParticipationInfo> userParticipationInfoList =
                AppointmentUtils.retrieveUserParticipationInfoList(userAppointmentMappingList, hostName);

        return tempAppointmentMapper.to(appointment, userParticipationInfoList, hostName, isHost, isParticipated);
    }

    @Override
    @Transactional
    public void updateAppointment(UpdateTempAppointmentReqDto reqDto) {
        Appointment appointment = appointmentRepository.findByAppointmentCode(reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        if (AppointmentState.CONFIRMED.equals(appointment.getAppointmentState())) {
            throw new AppointmentException(ErrorCode.APPOINTMENT_ALREADY_CONFIRMED);
        }

        UserAppointmentMapping userAppointmentMapping =
                userAppointmentMappingRepository.findByAppointmentCodeAndNicknameAndTempPassword(
                                reqDto.getAppointmentCode(), reqDto.getTempUserInfo().getNickname(), reqDto.getTempUserInfo().getPassword())
                        .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_USER_APPOINTMENT_MAPPING));

        if (!UserRole.HOST.equals(userAppointmentMapping.getUserRole())) {
            throw new AppointmentException(ErrorCode.USER_NOT_HOST);
        }

        userAppointmentMappingRepository.deleteAllByAppointmentCodeAndUserRole(appointment.getAppointmentCode(), UserRole.GUEST);

        appointment.update(reqDto.getAppointmentName(), reqDto.getAddress(), reqDto.getCandidateDateList(), reqDto.getNotice());
    }

    @Override
    @Transactional
    public void deleteAppointment(DeleteTempAppointmentReqDto reqDto) {
        Appointment appointment = appointmentRepository.findByAppointmentCode(reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        if (AppointmentState.CONFIRMED.equals(appointment.getAppointmentState())) {
            throw new AppointmentException(ErrorCode.APPOINTMENT_ALREADY_CONFIRMED);
        }

        UserAppointmentMapping userAppointmentMapping =
                userAppointmentMappingRepository.findByAppointmentCodeAndNicknameAndTempPassword(
                                reqDto.getAppointmentCode(), reqDto.getTempUserInfo().getNickname(), reqDto.getTempUserInfo().getPassword())
                        .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_USER_APPOINTMENT_MAPPING));

        if (!UserRole.HOST.equals(userAppointmentMapping.getUserRole())) {
            throw new AppointmentException(ErrorCode.USER_NOT_HOST);
        }

        userAppointmentMappingRepository.deleteAllByAppointmentCode(reqDto.getAppointmentCode());
        appointmentRepository.deleteById(appointment.getId());
    }

    @Override
    @Transactional
    public void registerAppointmentJoin(CreateTempAppointmentJoinReqDto reqDto) {
        Appointment appointment = appointmentRepository.findByAppointmentCode(reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        if (AppointmentState.CONFIRMED.equals(appointment.getAppointmentState())) {
            throw new AppointmentException(ErrorCode.APPOINTMENT_ALREADY_CONFIRMED);
        }

        List<UserAppointmentMapping> userAppointmentMappingList =
                userAppointmentMappingRepository.findAllByAppointmentCodeAndNicknameAndAndTempPassword(
                        reqDto.getAppointmentCode(), reqDto.getTempUserInfo().getNickname(), reqDto.getTempUserInfo().getPassword());

        userAppointmentMappingList.stream()
                .filter(mapping -> UserRole.GUEST.equals(mapping.getUserRole()))
                .findFirst()
                .ifPresent(mapping -> {
                    throw new AppointmentException(ErrorCode.APPOINTMENT_ALREADY_PARTICIPATE);
                });

        AppointmentValidator.retrieveInvalidSelectedDateTime(appointment.getCandidateDateList(), reqDto.getSelectedDateTimeList())
                .ifPresent(result -> {
                    throw new AppointmentException(ErrorCode.PARAMETER_VALIDATION_FAIL,
                            String.format("후보 날짜에서 벗어난 입력 값 입니다. %s", result));
                });

        UserAppointmentMapping userAppointmentMapping = UserAppointmentMapping.builder()
                .appointmentCode(reqDto.getAppointmentCode())
                .userRole(UserRole.GUEST)
                .userAuthType(UserAuthType.TEMPORARY)
                .isAvailable(false)
                .selectedDateTimeList(List.copyOf(reqDto.getSelectedDateTimeList()))
                .nickname(reqDto.getTempUserInfo().getNickname())
                .tempPassword(reqDto.getTempUserInfo().getPassword())
                .build();

        userAppointmentMappingRepository.save(userAppointmentMapping);
    }

    @Override
    public List<TempAppointmentParticipantsRespDto> retrieveAppointmentParticipants(TempAppointmentParticipantsReqDto reqDto) {
        Appointment appointment = appointmentRepository.findByAppointmentCode(reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        userAppointmentMappingRepository.findByAppointmentCodeAndNicknameAndTempPassword(appointment.getAppointmentCode(),
                        reqDto.getTempUserInfo().getNickname(), reqDto.getTempUserInfo().getPassword())
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_USER_APPOINTMENT_MAPPING));

        List<UserAppointmentMapping> userAppointmentMappingList =
                userAppointmentMappingRepository.findAllByAppointmentCodeAndNicknameAndAndTempPassword(
                        reqDto.getAppointmentCode(), reqDto.getTempUserInfo().getNickname(), reqDto.getTempUserInfo().getPassword());

        String hostName = userAppointmentMappingList.stream()
                .filter(mapping -> UserRole.HOST.equals(mapping.getUserRole()))
                .findFirst()
                .map(UserAppointmentMapping::getNickname)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_HOST));

        List<ParticipationInfo> participationInfoList =
                userAppointmentMappingRepository.findAllByAppointmentCodeAndUserRole(appointment.getAppointmentCode(), UserRole.GUEST).stream()
                        .map(mapping -> {
                            UserRole userRole = StringUtils.equals(hostName, mapping.getNickname()) ? UserRole.HOST : UserRole.GUEST;
                            return tempAppointmentMapper.toParticipationInfo(mapping, userRole);
                        })
                        .toList();

        return participationInfoList.stream().map(tempAppointmentMapper::to).toList();
    }

    @Override
    public TempAppointmentMyParticipantRespDto retrieveAppointmentMyParticipant(TempAppointmentMyParticipantReqDto reqDto) {
        Appointment appointment = appointmentRepository.findByAppointmentCode(reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        userAppointmentMappingRepository.findByAppointmentCodeAndNicknameAndTempPassword(appointment.getAppointmentCode(),
                        reqDto.getTempUserInfo().getNickname(), reqDto.getTempUserInfo().getPassword())
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_USER_APPOINTMENT_MAPPING));

        List<UserAppointmentMapping> userAppointmentMappingList =
                userAppointmentMappingRepository.findAllByAppointmentCodeAndNicknameAndAndTempPassword(
                        reqDto.getAppointmentCode(), reqDto.getTempUserInfo().getNickname(), reqDto.getTempUserInfo().getPassword());

        String hostName = userAppointmentMappingList.stream()
                .filter(mapping -> UserRole.HOST.equals(mapping.getUserRole()))
                .findFirst()
                .map(UserAppointmentMapping::getNickname)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_HOST));

        UserAppointmentMapping userAppointmentMapping = userAppointmentMappingList.stream()
                .filter(mapping -> UserRole.GUEST.equals(mapping.getUserRole())
                        && reqDto.getTempUserInfo().getNickname().equals(mapping.getNickname())
                        && reqDto.getTempUserInfo().getPassword().equals(mapping.getTempPassword()))
                .findFirst()
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_USER_APPOINTMENT_MAPPING));

        ParticipationInfo participationInfo = tempAppointmentMapper.toParticipationInfo(
                userAppointmentMapping, StringUtils.equals(hostName, userAppointmentMapping.getNickname()) ? UserRole.HOST : UserRole.GUEST);

        return tempAppointmentMapper.toTempAppointmentParticipantRespDto(participationInfo);
    }

    @Override
    @Transactional
    public void updateAppointmentJoin(UpdateTempAppointmentJoinReqDto reqDto) {
        Appointment appointment = appointmentRepository.findByAppointmentCode(reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        if (AppointmentState.CONFIRMED.equals(appointment.getAppointmentState())) {
            throw new AppointmentException(ErrorCode.APPOINTMENT_ALREADY_CONFIRMED);
        }

        UserAppointmentMapping userAppointmentMapping = userAppointmentMappingRepository.findAllByAppointmentCodeAndNicknameAndAndTempPassword(reqDto.getAppointmentCode(),
                        reqDto.getTempUserInfo().getNickname(), reqDto.getTempUserInfo().getPassword())
                .stream()
                .filter(mapping -> UserRole.GUEST.equals(mapping.getUserRole()))
                .findFirst()
                .orElseThrow(() -> new AppointmentException(ErrorCode.USER_NOT_GUEST));

        AppointmentValidator.retrieveInvalidSelectedDateTime(appointment.getCandidateDateList(), reqDto.getSelectedDateTimeList())
                .ifPresent(result -> {
                    throw new AppointmentException(ErrorCode.PARAMETER_VALIDATION_FAIL,
                            String.format("후보 날짜에서 벗어난 입력 값 입니다. %s", result));
                });

        userAppointmentMapping.update(reqDto.getSelectedDateTimeList());
    }

    @Override
    @Transactional
    public void deleteAppointmentJoin(DeleteTempAppointmentJoinReqDto reqDto) {
        Appointment appointment = appointmentRepository.findByAppointmentCode(reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        if (AppointmentState.CONFIRMED.equals(appointment.getAppointmentState())) {
            throw new AppointmentException(ErrorCode.APPOINTMENT_ALREADY_CONFIRMED);
        }

        UserAppointmentMapping userAppointmentMapping = userAppointmentMappingRepository.findByAppointmentCodeAndNicknameAndAndTempPassword(reqDto.getAppointmentCode(),
                        reqDto.getTempUserInfo().getNickname(), reqDto.getTempUserInfo().getPassword())
                .stream()
                .filter(mapping -> UserRole.GUEST.equals(mapping.getUserRole()))
                .findFirst()
                .orElseThrow(() -> new AppointmentException(ErrorCode.USER_NOT_GUEST));

        userAppointmentMappingRepository.deleteById(userAppointmentMapping.getId());
    }

    @Override
    public Page<TempAppointmentCandidateInfoRespDto> retrieveCandidateInfo(TempAppointmentCandidateInfoReqDto reqDto) {
        CustomPageRequest customPageRequest = CustomPageRequest.of(reqDto.getPage(), reqDto.getSize());

        Appointment appointment = appointmentRepository.findByAppointmentCode(reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        if (AppointmentState.CONFIRMED.equals(appointment.getAppointmentState())) {
            throw new AppointmentException(ErrorCode.APPOINTMENT_ALREADY_CONFIRMED);
        }

        userAppointmentMappingRepository.findAllByAppointmentCodeAndNicknameAndAndTempPassword(reqDto.getAppointmentCode(),
                        reqDto.getTempUserInfo().getNickname(), reqDto.getTempUserInfo().getPassword())
                .stream()
                .filter(mapping -> UserRole.HOST.equals(mapping.getUserRole()))
                .findFirst()
                .orElseThrow(() -> new AppointmentException(ErrorCode.USER_NOT_HOST));

        List<LocalDate> candidateDateList = appointment.getCandidateDateList();

        List<UserAppointmentMapping> participationInfoList =
                userAppointmentMappingRepository.findAllByAppointmentCodeAndUserRole(reqDto.getAppointmentCode(), UserRole.GUEST);

        String hostNickname = userAppointmentMappingRepository.findByAppointmentCodeAndUserRole(reqDto.getAppointmentCode(), UserRole.HOST)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND))
                .getNickname();

        List<AppointmentCandidateInfo> appointmentCandidateInfos =
                AppointmentUtils.retrieveCandidateInfoList(candidateDateList, participationInfoList, hostNickname);

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
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        if (AppointmentState.CONFIRMED.equals(appointment.getAppointmentState())) {
            throw new AppointmentException(ErrorCode.APPOINTMENT_ALREADY_CONFIRMED);
        }

        userAppointmentMappingRepository.findAllByAppointmentCodeAndNicknameAndAndTempPassword(reqDto.getAppointmentCode(),
                        reqDto.getTempUserInfo().getNickname(), reqDto.getTempUserInfo().getPassword())
                .stream()
                .filter(mapping -> UserRole.HOST.equals(mapping.getUserRole()))
                .findFirst()
                .orElseThrow(() -> new AppointmentException(ErrorCode.USER_NOT_HOST));

        List<UserAppointmentMapping> userGuestList =
                userAppointmentMappingRepository.findAllByAppointmentCodeAndUserRole(appointment.getAppointmentCode(), UserRole.GUEST);

        ConfirmedDateTime confirmedDateTime = reqDto.getConfirmedDateTime();

        AppointmentUtils.retrieveAvailableUserList(confirmedDateTime, userGuestList)
                .forEach(userAppointmentMapping -> userAppointmentMapping.updateIsAvailable(true));

        appointment.confirmed(confirmedDateTime);
    }

}
