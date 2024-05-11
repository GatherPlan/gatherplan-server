package com.example.gatherplan.appointment.service.impl;

import com.example.gatherplan.appointment.dto.*;
import com.example.gatherplan.appointment.enums.AppointmentState;
import com.example.gatherplan.appointment.enums.UserAuthType;
import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.exception.AppointmentException;
import com.example.gatherplan.appointment.exception.UserException;
import com.example.gatherplan.appointment.mapper.TempAppointmentMapper;
import com.example.gatherplan.appointment.repository.AppointmentRepository;
import com.example.gatherplan.appointment.repository.CustomAppointmentRepository;
import com.example.gatherplan.appointment.repository.CustomUserAppointmentMappingRepository;
import com.example.gatherplan.appointment.repository.UserAppointmentMappingRepository;
import com.example.gatherplan.appointment.repository.entity.Appointment;
import com.example.gatherplan.appointment.repository.entity.UserAppointmentMapping;
import com.example.gatherplan.appointment.service.TempAppointmentService;
import com.example.gatherplan.appointment.validator.AppointmentValidator;
import com.example.gatherplan.common.exception.ErrorCode;
import com.example.gatherplan.common.unit.ParticipationInfo;
import com.example.gatherplan.common.unit.TempUserInfo;
import com.example.gatherplan.common.utils.MathUtils;
import com.example.gatherplan.common.utils.UuidUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class TempAppointmentServiceImpl implements TempAppointmentService {

    private final TempAppointmentMapper tempAppointmentMapper;
    private final AppointmentRepository appointmentRepository;
    private final UserAppointmentMappingRepository userAppointmentMappingRepository;
    private final CustomAppointmentRepository customAppointmentRepository;
    private final CustomUserAppointmentMappingRepository customUserAppointmentMappingRepository;

    @Override
    @Transactional
    public String registerTempAppointment(CreateTempAppointmentReqDto reqDto) {
        String appointmentCode = UuidUtils.generateRandomString(12);

        Appointment appointment = tempAppointmentMapper.to(reqDto, AppointmentState.UNCONFIRMED, appointmentCode);

        TempUserInfo tempUserInfo = reqDto.getTempUserInfo();

        Long appointmentId = appointmentRepository.save(appointment).getId();

        UserAppointmentMapping userAppointmentMapping = UserAppointmentMapping.builder()
                .appointmentSeq(appointmentId)
                .userRole(UserRole.HOST)
                .nickname(tempUserInfo.getNickname())
                .tempPassword(tempUserInfo.getPassword())
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
    public TempAppointmentParticipationInfoRespDto retrieveAppointmentParticipationInfo(
            TempAppointmentParticipationInfoReqDto reqDto) {

        Appointment appointment = customAppointmentRepository.findByAppointmentCodeAndTempUserInfo(reqDto.getAppointmentCode(),
                        reqDto.getTempUserInfo().getNickname(), reqDto.getTempUserInfo().getPassword(), UserRole.GUEST)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        List<ParticipationInfo> participationInfoList =
                customUserAppointmentMappingRepository.findAppointmentParticipationInfo(appointment.getId()).stream().toList();

        return tempAppointmentMapper.to(appointment, participationInfoList);
    }

    @Override
    @Transactional
    public void deleteAppointment(DeleteTempAppointmentReqDto reqDto) {
        TempUserInfo tempUserInfo = reqDto.getTempUserInfo();

        Long appointmentId = customAppointmentRepository.findByAppointmentCodeAndTempUserInfo(reqDto.getAppointmentCode(),
                        tempUserInfo.getNickname(), tempUserInfo.getPassword(), UserRole.HOST)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT))
                .getId();

        userAppointmentMappingRepository.deleteAllByAppointmentSeq(appointmentId);
        appointmentRepository.deleteById(appointmentId);
    }

    @Override
    @Transactional
    public void updateAppointment(UpdateTempAppointmentReqDto reqDto) {
        TempUserInfo tempUserInfo = reqDto.getTempUserInfo();

        Appointment appointment = customAppointmentRepository.findByAppointmentCodeAndTempUserInfo(reqDto.getAppointmentCode(),
                        tempUserInfo.getNickname(), tempUserInfo.getPassword(), UserRole.HOST)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        appointment.update(reqDto.getAppointmentName(),
                reqDto.getAddress(), reqDto.getCandidateDateList(), reqDto.getNotice());
    }

    @Override
    public void registerAppointmentParticipation(CreateTempAppointmentParticipationReqDto reqDto) {
        Appointment appointment = appointmentRepository.findByAppointmentCode(reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        AppointmentValidator.retrieveInvalidSelectedDateTime(appointment, reqDto.getSelectedDateTimeList())
                .ifPresent(result -> {
                    throw new AppointmentException(ErrorCode.PARAMETER_VALIDATION_FAIL,
                            String.format("후보 날짜 및 시간에 벗어난 입력 값 입니다. %s", result));
                });

        TempUserInfo tempUserInfo = reqDto.getTempUserInfo();

        UserAppointmentMapping userAppointmentMapping = UserAppointmentMapping.builder()
                .appointmentSeq(appointment.getId())
                .userRole(UserRole.GUEST)
                .nickname(tempUserInfo.getNickname())
                .tempPassword(tempUserInfo.getPassword())
                .userAuthType(UserAuthType.TEMPORARY)
                .selectedDateTimeList(List.copyOf(reqDto.getSelectedDateTimeList()))
                .build();

        userAppointmentMappingRepository.save(userAppointmentMapping);
    }

    @Override
    @Transactional
    public void confirmedAppointment(TempConfirmedAppointmentReqDto reqDto) {
        Appointment appointment = customAppointmentRepository.findByAppointmentCodeAndTempUserInfo(reqDto.getAppointmentCode(),
                        reqDto.getTempUserInfo().getNickname(), reqDto.getTempUserInfo().getPassword(), UserRole.HOST)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        appointment.confirmed(reqDto.getConfirmedDateTime());
    }


    public List<AppointmentCandidateInfoRespDto> retrieveAppointmentCandidateInfoList(AppointmentCandidateInfoReqDto reqDto) {
        Appointment appointment = customAppointmentRepository.findByAppointmentCodeAndTempUserInfo(reqDto.getAppointmentCode(),
                        reqDto.getTempUserInfo().getNickname(), reqDto.getTempUserInfo().getPassword(), UserRole.HOST)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        List<ParticipationInfo> participationInfoList =
                customUserAppointmentMappingRepository.findAppointmentParticipationInfo(appointment.getId()).stream().toList();

        return combinationedAppointmentCandidateInfoList(participationInfoList);
    }

    private List<AppointmentCandidateInfoRespDto> combinationedAppointmentCandidateInfoList(List<ParticipationInfo> participationInfoList) {

        List<String> participants = participationInfoList.stream()
                .map(ParticipationInfo::getNickname).toList();

        List<Set<String>> combinations = MathUtils.combinations(participants);

        List<List<Object>> list = combinations.stream().map(
                combination -> {
                    List<ParticipationInfo> fiteredParticipationInfoList = participationInfoList.stream()
                            .filter(participationInfo -> combination.contains(participationInfo.getNickname()))
                            .toList();

                    return List.of();
                }
        ).toList();

        return List.of(AppointmentCandidateInfoRespDto.builder().build());
    }
}
