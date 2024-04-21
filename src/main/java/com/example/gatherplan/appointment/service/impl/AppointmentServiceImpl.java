package com.example.gatherplan.appointment.service.impl;

import com.example.gatherplan.appointment.dto.*;
import com.example.gatherplan.appointment.enums.AppointmentState;
import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.exception.AppointmentException;
import com.example.gatherplan.appointment.mapper.AppointmentMapper;
import com.example.gatherplan.appointment.repository.*;
import com.example.gatherplan.appointment.repository.entity.Appointment;
import com.example.gatherplan.appointment.repository.entity.UserAppointmentMapping;
import com.example.gatherplan.appointment.service.AppointmentService;
import com.example.gatherplan.appointment.validator.AppointmentValidator;
import com.example.gatherplan.common.exception.ErrorCode;
import com.example.gatherplan.common.unit.ConfirmedDateTime;
import com.example.gatherplan.common.unit.ParticipationInfo;
import com.example.gatherplan.common.utils.UuidUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentMapper appointmentMapper;
    private final AppointmentRepository appointmentRepository;
    private final CustomTempUserRepository customTempUserRepository;
    private final UserAppointmentMappingRepository userAppointmentMappingRepository;
    private final CustomUserAppointmentMappingRepository customUserAppointmentMappingRepository;
    private final TempUserAppointmentMappingRepository tempUserAppointmentMappingRepository;

    private final CustomAppointmentRepository customAppointmentRepository;
    private final CustomTempUserAppointmentMappingRepository customTempUserAppointmentMappingRepository;

    @Override
    @Transactional
    public String registerAppointment(CreateAppointmentReqDto reqDto, Long userId) {
        String appointmentCode = UuidUtils.generateRandomString(12);

        Appointment appointment = appointmentMapper.to(reqDto, AppointmentState.UNCONFIRMED, appointmentCode);

        Long appointmentId = appointmentRepository.save(appointment).getId();

        UserAppointmentMapping userAppointmentMapping = UserAppointmentMapping.builder()
                .appointmentSeq(appointmentId)
                .userSeq(userId)
                .userRole(UserRole.HOST)
                .build();

        userAppointmentMappingRepository.save(userAppointmentMapping);

        return appointmentCode;
    }

    @Override
    public boolean isUserParticipated(String appointmentCode, String email) {
        return customUserAppointmentMappingRepository.existUserMappedToAppointment(
                email, appointmentCode, UserRole.GUEST);
    }

    @Override
    public List<AppointmentWithHostByKeywordRespDto> retrieveAppointmentSearchList(String keyword, String email,
                                                                                   String nickname) {
        List<Appointment> appointmentList =
                customAppointmentRepository.findAllByUserInfoAndKeyword(email, UserRole.GUEST, keyword);
        List<Long> appointmentIdList = appointmentList.stream().map(Appointment::getId).toList();

        Map<Long, String> hostNameMap = Stream.concat(
                        customUserAppointmentMappingRepository.findAllAppointmentWithHost(appointmentIdList).stream(),
                        customTempUserAppointmentMappingRepository.findAllAppointmentWithHost(appointmentIdList).stream())
                .collect(Collectors.toMap(AppointmentWithHostDto::getAppointmentId, AppointmentWithHostDto::getHostName));

        return appointmentList.stream()
                .map(appointment ->
                        appointmentMapper.toAppointmentWithHostByKeywordRespDto(appointment, hostNameMap.get(appointment.getId()),
                                StringUtils.equals(nickname, hostNameMap.get(appointment.getId()))))
                .toList();
    }

    @Override
    public AppointmentInfoRespDto retrieveAppointmentInfo(String appointmentCode, String email) {
        Appointment appointment = customAppointmentRepository.findByAppointmentCodeAndUserInfo(appointmentCode,
                        email, UserRole.GUEST)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        String hostName = Optional.ofNullable(customTempUserAppointmentMappingRepository.findHostName(appointment.getId()))
                .orElseGet(() -> customUserAppointmentMappingRepository.findHostName(appointment.getId()));

        return appointmentMapper.to(appointment, hostName);
    }

    @Override
    public AppointmentParticipationInfoRespDto retrieveAppointmentParticipationInfo(String appointmentCode, String email) {
        Appointment appointment = customAppointmentRepository.findByAppointmentCodeAndUserInfo(appointmentCode,
                        email, UserRole.GUEST)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        List<ParticipationInfo> participationInfo = new ArrayList<>(customUserAppointmentMappingRepository.findAppointmentParticipationInfo(appointment.getId()));
        participationInfo.addAll(customTempUserAppointmentMappingRepository.findAppointmentParticipationInfo(appointment.getId()));

        return appointmentMapper.to(appointment, participationInfo);
    }

    @Override
    @Transactional
    public void deleteAppointment(String appointmentCode, String email) {
        Appointment appointment = customAppointmentRepository.findByAppointmentCodeAndUserInfo(appointmentCode,
                        email, UserRole.HOST)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        Long appointmentId = appointment.getId();

        customTempUserRepository.deleteAllByAppointmentId(appointmentId);
        tempUserAppointmentMappingRepository.deleteAllByAppointmentSeq(appointmentId);
        userAppointmentMappingRepository.deleteAllByAppointmentSeq(appointmentId);
        appointmentRepository.deleteById(appointmentId);
    }

    @Override
    @Transactional
    public void updateAppointment(UpdateAppointmentReqDto reqDto, String email) {
        Appointment appointment = customAppointmentRepository.findByAppointmentCodeAndUserInfo(reqDto.getAppointmentCode(),
                        email, UserRole.HOST)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        appointment.update(reqDto.getAppointmentName(), reqDto.getCandidateTimeTypeList(),
                reqDto.getAddress(), reqDto.getCandidateDateList());
    }

    @Override
    public AppointmentRespDto retrieveAppointment(String appointmentCode) {
        Appointment appointment = appointmentRepository.findByAppointmentCode(appointmentCode)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        String hostName = Optional.ofNullable(customTempUserAppointmentMappingRepository.findHostName(appointment.getId()))
                .orElseGet(() -> customUserAppointmentMappingRepository.findHostName(appointment.getId()));

        return appointmentMapper.toAppointmentRespDto(appointment, hostName);
    }

    @Override
    @Transactional
    public void registerAppointmentParticipation(CreateAppointmentParticipationReqDto reqDto, Long userId) {
        Appointment appointment = appointmentRepository.findByAppointmentCode(reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        AppointmentValidator.retrieveInvalidSelectedDateTime(appointment, reqDto.getSelectedDateTimeList())
                .ifPresent(result -> {
                    throw new AppointmentException(ErrorCode.PARAMETER_VALIDATION_FAIL,
                            String.format("후보 날짜 및 시간에 벗어난 입력 값 입니다. %s", result));
                });


        userAppointmentMappingRepository.findUserAppointmentMappingByAppointmentSeqAndUserSeqAndUserRole(
                appointment.getId(), userId, UserRole.GUEST
        ).ifPresent(result -> {
            throw new AppointmentException(ErrorCode.APPOINTMENT_ALREADY_PARTICIPATE);
        });

        UserAppointmentMapping userAppointmentMapping = UserAppointmentMapping.builder()
                .appointmentSeq(appointment.getId())
                .userSeq(userId)
                .userRole(UserRole.GUEST)
                .selectedDateTimeList(List.copyOf(reqDto.getSelectedDateTimeList()))
                .build();

        userAppointmentMappingRepository.save(userAppointmentMapping);
    }

    @Override
    public List<String> retrieveEligibleParticipantsList(ConfirmedAppointmentParticipantsReqDto reqDto, String email) {
        Appointment appointment = customAppointmentRepository.findByAppointmentCodeAndUserInfo(reqDto.getAppointmentCode(),
                        email, UserRole.GUEST)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        ConfirmedDateTime confirmedDateTime = reqDto.getConfirmedDateTime();

        return Stream.concat(customUserAppointmentMappingRepository.findAppointmentParticipationInfo(appointment.getId()).stream(),
                        customTempUserAppointmentMappingRepository.findAppointmentParticipationInfo(appointment.getId()).stream())
                .filter(participant -> participant.getSelectedDateTimeList().stream().anyMatch(
                        selectedDateTime ->
                                selectedDateTime.getSelectedDate().equals(confirmedDateTime.getConfirmedDate())
                                        && !selectedDateTime.getSelectedStartTime().isAfter(confirmedDateTime.getConfirmedStartTime())
                                        && !selectedDateTime.getSelectedEndTime().isBefore(confirmedDateTime.getConfirmedEndTime())
                ))
                .map(ParticipationInfo::getNickname)
                .toList();
    }

    @Override
    @Transactional
    public void confirmedAppointment(ConfirmedAppointmentReqDto reqDto, String email) {
        Appointment appointment = customAppointmentRepository.findByAppointmentCodeAndUserInfo(reqDto.getAppointmentCode(),
                email, UserRole.HOST).orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        if (AppointmentValidator.retrieveInvalidConfirmedDateTime(appointment, reqDto.getConfirmedDateTime())) {
            throw new AppointmentException(ErrorCode.PARAMETER_VALIDATION_FAIL,
                    String.format("후보 날짜 및 시간에 벗어난 값 입니다. %s", reqDto.getConfirmedDateTime()));
        }

        appointment.confirmed(reqDto.getConfirmedDateTime());
    }
}
