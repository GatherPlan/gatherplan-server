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
import com.example.gatherplan.common.exception.ErrorCode;
import com.example.gatherplan.common.utils.UuidUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    public List<AppointmentWithHostRespDto> retrieveAppointmentList(String email) {
        List<Appointment> appointmentList = customAppointmentRepository.findAllByUserInfo(email, UserRole.GUEST);
        List<Long> appointmentIdList = appointmentList.stream().map(Appointment::getId).toList();

        List<AppointmentWithHostDto> appointmentWithHostDtoList = new ArrayList<>();

        appointmentWithHostDtoList.addAll(customUserAppointmentMappingRepository.findAllAppointmentWithHost(appointmentIdList));
        appointmentWithHostDtoList.addAll(customTempUserAppointmentMappingRepository.findAllAppointmentWithHost(appointmentIdList));

        Map<Long, String> hostNameMap = appointmentWithHostDtoList.stream()
                .collect(Collectors.toMap(AppointmentWithHostDto::getAppointmentId, AppointmentWithHostDto::getHostName));

        return appointmentList.stream()
                .map(appointment -> appointmentMapper.toAppointmentWithHostRespDto(appointment, hostNameMap.get(appointment.getId())))
                .toList();
    }

    @Override
    public List<AppointmentWithHostByKeywordRespDto> retrieveAppointmentSearchList(String keyword, String email) {
        List<Appointment> appointmentList =
                customAppointmentRepository.findAllByUserInfoAndKeyword(email, UserRole.GUEST, keyword);
        List<Long> appointmentIdList = appointmentList.stream().map(Appointment::getId).toList();

        List<AppointmentWithHostByKeywordDto> appointmentWithHostByKeywordDtoList = new ArrayList<>();

        appointmentWithHostByKeywordDtoList.addAll(customUserAppointmentMappingRepository.findAllAppointmentWithHostByKeyword(appointmentIdList));
        appointmentWithHostByKeywordDtoList.addAll(customTempUserAppointmentMappingRepository.findAllAppointmentWithHostByKeyword(appointmentIdList));

        Map<Long, String> hostNameMap = appointmentWithHostByKeywordDtoList.stream()
                .collect(Collectors.toMap(AppointmentWithHostByKeywordDto::getAppointmentId, AppointmentWithHostByKeywordDto::getHostName));

        return appointmentList.stream()
                .map(appointment -> appointmentMapper.toAppointmentWithHostByKeywordRespDto(appointment, hostNameMap.get(appointment.getId())))
                .toList();
    }

    @Override
    public AppointmentInfoRespDto retrieveAppointmentInfo(String appointmentCode, String email) {
        Appointment appointment = customAppointmentRepository.findByAppointmentCodeAndUserInfo(appointmentCode,
                        email, UserRole.GUEST)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        String hostName = Optional.ofNullable(customTempUserAppointmentMappingRepository.findHostName(appointment.getId()))
                .orElseGet(() -> customUserAppointmentMappingRepository.findHostName(appointment.getId()));

        return appointmentMapper.toAppointmentInfoRespDto(appointment, hostName);
    }

    @Override
    public AppointmentParticipationInfoRespDto retrieveAppointmentParticipationInfo(String appointmentCode, String email) {
        Appointment appointment = customAppointmentRepository.findByAppointmentCodeAndUserInfo(appointmentCode,
                        email, UserRole.HOST)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        List<AppointmentParticipationInfoRespDto.UserParticipationInfo> userParticipationInfo =
                customUserAppointmentMappingRepository.findAppointmentParticipationInfo(appointment.getId());

        List<TempAppointmentParticipationInfoRespDto.UserParticipationInfo> tempUserParticipationInfo =
                customTempUserAppointmentMappingRepository.findAppointmentParticipationInfo(appointment.getId());

        return appointmentMapper.to(appointment, userParticipationInfo, tempUserParticipationInfo);
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

}
