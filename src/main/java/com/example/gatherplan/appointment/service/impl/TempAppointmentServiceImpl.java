package com.example.gatherplan.appointment.service.impl;

import com.example.gatherplan.appointment.dto.*;
import com.example.gatherplan.appointment.enums.AppointmentState;
import com.example.gatherplan.appointment.enums.TimeType;
import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.exception.AppointmentException;
import com.example.gatherplan.appointment.mapper.TempAppointmentMapper;
import com.example.gatherplan.appointment.mapper.TempUserMapper;
import com.example.gatherplan.appointment.repository.*;
import com.example.gatherplan.appointment.repository.entity.Appointment;
import com.example.gatherplan.appointment.repository.entity.TempUser;
import com.example.gatherplan.appointment.repository.entity.TempUserAppointmentMapping;
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

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class TempAppointmentServiceImpl implements TempAppointmentService {

    private final TempUserMapper tempUserMapper;
    private final TempAppointmentMapper tempAppointmentMapper;
    private final AppointmentRepository appointmentRepository;
    private final TempUserRepository tempUserRepository;
    private final CustomTempUserRepository customTempUserRepository;
    private final UserAppointmentMappingRepository userAppointmentMappingRepository;
    private final TempUserAppointmentMappingRepository tempUserAppointmentMappingRepository;
    private final CustomTempUserAppointmentMappingRepository customTempUserAppointmentMappingRepository;
    private final CustomAppointmentRepository customAppointmentRepository;
    private final CustomUserAppointmentMappingRepository customUserAppointmentMappingRepository;

    @Override
    @Transactional
    public String registerTempAppointment(CreateTempAppointmentReqDto reqDto) {
        String appointmentCode = UuidUtils.generateRandomString(12);

        Appointment appointment = tempAppointmentMapper.to(reqDto, AppointmentState.UNCONFIRMED, appointmentCode);

        TempUserInfo tempUserInfo = reqDto.getTempUserInfo();
        TempUser tempUser = TempUser.builder()
                .nickname(tempUserInfo.getNickname())
                .password(tempUserInfo.getPassword())
                .build();

        Long appointmentId = appointmentRepository.save(appointment).getId();
        Long tempUserId = tempUserRepository.save(tempUser).getId();

        TempUserAppointmentMapping tempUserAppointmentMapping = TempUserAppointmentMapping.builder()
                .appointmentSeq(appointmentId)
                .tempUserSeq(tempUserId)
                .userRole(UserRole.HOST)
                .build();

        tempUserAppointmentMappingRepository.save(tempUserAppointmentMapping);

        return appointmentCode;
    }

    @Override
    public TempAppointmentInfoRespDto retrieveAppointmentInfo(TempAppointmentInfoReqDto reqDto) {
        Appointment appointment = customAppointmentRepository.findByAppointmentCodeAndTempUserInfo(reqDto.getAppointmentCode(),
                        reqDto.getTempUserInfo().getNickname(), reqDto.getTempUserInfo().getPassword(), UserRole.GUEST)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        String hostName = Optional.ofNullable(customTempUserAppointmentMappingRepository.findHostName(appointment.getId()))
                .orElseGet(() -> customUserAppointmentMappingRepository.findHostName(appointment.getId()));

        return tempAppointmentMapper.to(appointment, hostName);
    }

    @Override
    public TempAppointmentParticipationInfoRespDto retrieveAppointmentParticipationInfo(
            TempAppointmentParticipationInfoReqDto reqDto) {

        Appointment appointment = customAppointmentRepository.findByAppointmentCodeAndTempUserInfo(reqDto.getAppointmentCode(),
                        reqDto.getTempUserInfo().getNickname(), reqDto.getTempUserInfo().getPassword(), UserRole.GUEST)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        List<ParticipationInfo> participationInfo =
                Stream.concat(customUserAppointmentMappingRepository.findAppointmentParticipationInfo(appointment.getId()).stream(),
                                customTempUserAppointmentMappingRepository.findAppointmentParticipationInfo(appointment.getId()).stream())
                        .toList();

        return tempAppointmentMapper.to(appointment, participationInfo);
    }

    @Override
    @Transactional
    public void deleteAppointment(DeleteTempAppointmentReqDto reqDto) {
        TempUserInfo tempUserInfo = reqDto.getTempUserInfo();

        Long appointmentId = customAppointmentRepository.findByAppointmentCodeAndTempUserInfo(reqDto.getAppointmentCode(),
                        tempUserInfo.getNickname(), tempUserInfo.getPassword(), UserRole.HOST)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT))
                .getId();

        customTempUserRepository.deleteAllByAppointmentId(appointmentId);
        tempUserAppointmentMappingRepository.deleteAllByAppointmentSeq(appointmentId);
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

        appointment.update(reqDto.getAppointmentName(), reqDto.getCandidateTimeTypeList(),
                reqDto.getAddress(), reqDto.getCandidateDateList());
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

        TempUser tempUser = tempUserMapper.to(reqDto.getTempUserInfo());

        Long tempUserId = tempUserRepository.save(tempUser).getId();

        TempUserAppointmentMapping tempUserAppointmentMapping = TempUserAppointmentMapping.builder()
                .appointmentSeq(appointment.getId())
                .tempUserSeq(tempUserId)
                .userRole(UserRole.GUEST)
                .selectedDateTimeList(List.copyOf(reqDto.getSelectedDateTimeList()))
                .build();

        tempUserAppointmentMappingRepository.save(tempUserAppointmentMapping);
    }

    @Override
    @Transactional
    public void confirmedAppointment(TempConfirmedAppointmentReqDto reqDto) {
        Appointment appointment = customAppointmentRepository.findByAppointmentCodeAndTempUserInfo(reqDto.getAppointmentCode(),
                        reqDto.getTempUserInfo().getNickname(), reqDto.getTempUserInfo().getPassword(), UserRole.HOST)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        if (AppointmentValidator.retrieveInvalidConfirmedDateTime(appointment, reqDto.getConfirmedDateTime())) {
            throw new AppointmentException(ErrorCode.PARAMETER_VALIDATION_FAIL,
                    String.format("후보 날짜 및 시간에 벗어난 값 입니다. %s", reqDto.getConfirmedDateTime()));
        }

        appointment.confirmed(reqDto.getConfirmedDateTime());
    }


    public List<AppointmentCandidateInfoRespDto> retrieveAppointmentCandidateInfoList(AppointmentCandidateInfoReqDto reqDto) {
        Appointment appointment = customAppointmentRepository.findByAppointmentCodeAndTempUserInfo(reqDto.getAppointmentCode(),
                        reqDto.getTempUserInfo().getNickname(), reqDto.getTempUserInfo().getPassword(), UserRole.HOST)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        List<ParticipationInfo> participationInfoList =
                Stream.concat(customUserAppointmentMappingRepository.findAppointmentParticipationInfo(appointment.getId()).stream(),
                                customTempUserAppointmentMappingRepository.findAppointmentParticipationInfo(appointment.getId()).stream())
                        .toList();

        return combinationedAppointmentCandidateInfoList(participationInfoList);
    }

    private List<AppointmentCandidateInfoRespDto> combinationedAppointmentCandidateInfoList(List<ParticipationInfo> participationInfoList) {

        List<String> participants = participationInfoList.stream()
                .map(ParticipationInfo::getNickname).toList();

        List<Set<String>> combinations = MathUtils.combinations(participants);

        LocalTime startTime = TimeType.MORNING.getStartTime();
        LocalTime endTime = TimeType.EVENING.getEndTime();

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
