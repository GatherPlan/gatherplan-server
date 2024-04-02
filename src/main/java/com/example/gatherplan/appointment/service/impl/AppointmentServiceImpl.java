package com.example.gatherplan.appointment.service.impl;

import com.example.gatherplan.appointment.dto.*;
import com.example.gatherplan.appointment.enums.AppointmentState;
import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.exception.AppointmentException;
import com.example.gatherplan.appointment.exception.UserException;
import com.example.gatherplan.appointment.mapper.AppointmentMapper;
import com.example.gatherplan.appointment.repository.AppointmentRepository;
import com.example.gatherplan.appointment.repository.UserAppointmentMappingRepository;
import com.example.gatherplan.appointment.repository.UserRepository;
import com.example.gatherplan.appointment.repository.entity.Appointment;
import com.example.gatherplan.appointment.repository.entity.User;
import com.example.gatherplan.appointment.repository.entity.UserAppointmentMapping;
import com.example.gatherplan.appointment.service.AppointmentService;
import com.example.gatherplan.common.exception.ErrorCode;
import com.example.gatherplan.common.utils.UuidUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentMapper appointmentMapper;
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final UserAppointmentMappingRepository userAppointmentMappingRepository;


    @Override
    @Transactional
    public String registerAppointment(CreateAppointmentReqDto reqDto, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ErrorCode.RESOURCE_NOT_FOUND, "해당 회원은 존재하지 않습니다."));

        String appointmentCode = UuidUtils.generateRandomString(12);

        Appointment appointment = appointmentMapper.to(reqDto, AppointmentState.UNCONFIRMED, appointmentCode);

        Long appointmentId = appointmentRepository.save(appointment).getId();

        UserAppointmentMapping userAppointmentMapping = UserAppointmentMapping.builder()
                .appointmentSeq(appointmentId)
                .userSeq(user.getId())
                .userRole(UserRole.HOST)
                .build();

        userAppointmentMappingRepository.save(userAppointmentMapping);

        return appointmentCode;
    }

    @Override
    public void checkParticipation(CheckAppointmentReqDto reqDto, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 회원입니다."));

        Appointment appointment = appointmentRepository
                .findByAppointmentCode(reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 약속입니다."));

        userAppointmentMappingRepository
                .findByAppointmentSeqAndUserSeqAndUserRole(appointment.getId(), user.getId(), UserRole.GUEST)
                .orElseThrow(() -> new AppointmentException(ErrorCode.RESOURCE_NOT_FOUND, "참여하지 않은 약속입니다."));
    }

    @Override
    public List<GetAppointmentListRespDto> getAppointmentList(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 회원입니다."));

        List<UserAppointmentMapping> maps = userAppointmentMappingRepository.findByUserSeq(user.getId());

        return maps.stream()
                .map(mapping -> {
                    Appointment appointment = appointmentRepository.findById(mapping.getAppointmentSeq())
                            .orElseThrow(() ->
                                    new AppointmentException(ErrorCode.RESOURCE_NOT_FOUND, "약속을 찾을 수 없습니다."));

                    UserAppointmentMapping hostMapping = userAppointmentMappingRepository
                            .findByAppointmentSeqAndUserRole(appointment.getId(), UserRole.HOST)
                            .orElseThrow(() ->
                                    new AppointmentException(ErrorCode.RESOURCE_NOT_FOUND, "호스트를 찾을 수 없습니다."));

                    User host = userRepository.findById(hostMapping.getUserSeq())
                            .orElseThrow(() ->
                                    new AppointmentException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 회원입니다."));

                    return appointmentMapper.toGetAppointmentListRespDto(appointment, host.getNickname());
                })
                .toList();
    }

    @Override
    public List<GetAppointmentSearchListRespDto> getAppointmentSearchList(
            GetAppointmentSearchListReqDto reqDto, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 회원입니다."));

        List<UserAppointmentMapping> maps = userAppointmentMappingRepository.findByUserSeq(user.getId());

        return maps.stream()
                .map(mapping -> {
                    Appointment appointment = appointmentRepository.findById(mapping.getAppointmentSeq())
                            .filter(filterAppointment -> filterAppointment.getAppointmentName()
                                    .contains(reqDto.getKeyword()))
                            .orElseThrow(() ->
                                    new AppointmentException(ErrorCode.RESOURCE_NOT_FOUND, "약속을 찾을 수 없습니다."));

                    UserAppointmentMapping hostMapping = userAppointmentMappingRepository
                            .findByAppointmentSeqAndUserRole(appointment.getId(), UserRole.HOST)
                            .orElseThrow(() ->
                                    new AppointmentException(ErrorCode.RESOURCE_NOT_FOUND, "호스트를 찾을 수 없습니다."));

                    User host = userRepository.findById(hostMapping.getUserSeq())
                            .orElseThrow(() ->
                                    new AppointmentException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 회원입니다."));

                    return appointmentMapper.toGetAppointmentSearchListRespDto(appointment, host.getNickname());
                })
                .toList();
    }

    @Override
    public GetAppointmentInfoRespDto getAppointmentInfo
            (GetAppointmentInfoReqDto reqDto, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 회원입니다."));

        Appointment appointment = appointmentRepository
                .findByAppointmentCode(reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 약속입니다."));

        userAppointmentMappingRepository
                .findByAppointmentSeqAndUserSeq(appointment.getId(), user.getId())
                .orElseThrow(() -> new AppointmentException(ErrorCode.RESOURCE_NOT_FOUND, "매핑되어 있지 않은 약속입니다."));

        return GetAppointmentInfoRespDto.builder()
                .address(appointment.getAddress())
                .confirmedDateTime(appointment.getConfirmedDateTime())
                .build();
    }

    @Override
    public GetAppointmentParticipationInfoRespDto getAppointmentParticipationInfo(
            GetAppointmentParticipationInfoReqDto reqDto, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 회원입니다."));

        Appointment appointment = appointmentRepository
                .findByAppointmentCode(reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 약속입니다."));

        userAppointmentMappingRepository
                .findByAppointmentSeqAndUserSeq(appointment.getId(), user.getId())
                .orElseThrow(() -> new AppointmentException(ErrorCode.RESOURCE_NOT_FOUND, "매핑되어 있지 않은 약속입니다."));

        List<UserAppointmentMapping> maps = userAppointmentMappingRepository.findByAppointmentSeq(appointment.getId());

        List<GetAppointmentParticipationInfoRespDto.UserParticipationInfo> userParticipationInfoList = maps.stream()
                .map(mapping -> {
                    User findUser = userRepository.findById(mapping.getUserSeq())
                            .orElseThrow(() -> new UserException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 회원입니다."));

                    return GetAppointmentParticipationInfoRespDto.UserParticipationInfo.builder()
                            .nickname(findUser.getNickname())
                            .selectedDateTime(mapping.getSelectedDateTimeList())
                            .build();
                })
                .toList();

        return GetAppointmentParticipationInfoRespDto.builder()
                .userParticipationInfo(userParticipationInfoList)
                .candidateTimeTypeList(appointment.getCandidateTimeTypeList())
                .candidateDateList(appointment.getCandidateDateList())
                .build();
    }

}
