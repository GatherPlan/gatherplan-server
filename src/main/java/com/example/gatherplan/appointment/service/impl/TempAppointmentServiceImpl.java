package com.example.gatherplan.appointment.service.impl;

import com.example.gatherplan.appointment.dto.*;
import com.example.gatherplan.appointment.enums.AppointmentState;
import com.example.gatherplan.appointment.enums.UserAuthType;
import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.exception.AppointmentException;
import com.example.gatherplan.appointment.exception.UserException;
import com.example.gatherplan.appointment.mapper.TempAppointmentMapper;
import com.example.gatherplan.appointment.repository.*;
import com.example.gatherplan.appointment.repository.entity.Appointment;
import com.example.gatherplan.appointment.repository.entity.UserAppointmentMapping;
import com.example.gatherplan.appointment.service.TempAppointmentService;
import com.example.gatherplan.appointment.validator.AppointmentValidator;
import com.example.gatherplan.common.exception.ErrorCode;
import com.example.gatherplan.common.unit.SelectedDateTime;
import com.example.gatherplan.common.unit.UserParticipationInfo;
import com.example.gatherplan.common.utils.MathUtils;
import com.example.gatherplan.common.utils.UuidUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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
    private final CustomUserRepository customUserRepository;

    @Override
    @Transactional
    public String registerTempAppointment(CreateTempAppointmentReqDto reqDto) {
        String appointmentCode = UuidUtils.generateRandomString(12);

        Appointment appointment = tempAppointmentMapper.to(reqDto, AppointmentState.UNCONFIRMED, appointmentCode);

        Long appointmentId = appointmentRepository.save(appointment).getId();

        UserAppointmentMapping userAppointmentMapping = UserAppointmentMapping.builder()
                .appointmentSeq(appointmentId)
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
        Appointment appointment = appointmentRepository.findByAppointmentCode(reqDto.getAppointmentCode()).orElseThrow(()
                -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        String hostName = Optional.ofNullable(customUserAppointmentMappingRepository.findHostName(appointment.getId()))
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        return tempAppointmentMapper.to(appointment, hostName);
    }

    @Override
    @Transactional
    public boolean validJoinTempUser(CreateTempUserReqDto reqDto) {
        Appointment appointment = appointmentRepository.findByAppointmentCode(reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        return customUserRepository.findAllUserNameByAppointmentId(appointment.getId()).stream()
                .noneMatch(nickname -> StringUtils.equals(nickname, reqDto.getTempUserInfo().getNickname()));
    }

    @Override
    public boolean login(TempUserLoginReqDto reqDto) {
        Appointment appointment = appointmentRepository.findByAppointmentCode(reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        return userAppointmentMappingRepository
                .existsByAppointmentSeqAndNicknameAndTempPasswordAndUserRole(
                        appointment.getId(), reqDto.getTempUserInfo().getNickname(), reqDto.getTempUserInfo().getPassword(), UserRole.HOST);
    }

    @Override
    public boolean retrieveParticipationStatus(TempAppointmentParticipationStatusReqDto reqDto) {
        Appointment appointment = appointmentRepository.findByAppointmentCode(reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        return userAppointmentMappingRepository
                .existsByAppointmentSeqAndNicknameAndTempPasswordAndUserRole(
                        appointment.getId(), reqDto.getTempUserInfo().getNickname(), reqDto.getTempUserInfo().getPassword(), UserRole.GUEST);
    }

    @Override
    public void registerAppointmentParticipation(CreateTempAppointmentParticipationReqDto reqDto) {
        Appointment appointment = appointmentRepository.findByAppointmentCode(reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        AppointmentValidator.retrieveInvalidSelectedDateTime(appointment, reqDto.getSelectedDateTimeList())
                .ifPresent(result -> {
                    throw new AppointmentException(ErrorCode.PARAMETER_VALIDATION_FAIL,
                            String.format("후보 날짜에서 벗어난 입력 값 입니다. %s", result));
                });

        UserAppointmentMapping userAppointmentMapping = UserAppointmentMapping.builder()
                .appointmentSeq(appointment.getId())
                .userRole(UserRole.GUEST)
                .nickname(reqDto.getTempUserInfo().getNickname())
                .tempPassword(reqDto.getTempUserInfo().getPassword())
                .userAuthType(UserAuthType.TEMPORARY)
                .isAvailable(false)
                .selectedDateTimeList(List.copyOf(reqDto.getSelectedDateTimeList()))
                .build();

        userAppointmentMappingRepository.save(userAppointmentMapping);
    }

    @Override
    public TempAppointmentInfoDetailRespDto retrieveAppointmentInfoDetail(TempAppointmentInfoDetailReqDto reqDto) {
        Appointment appointment = customAppointmentRepository.findByAppointmentCodeAndTempUserInfo(reqDto.getAppointmentCode(),
                        reqDto.getTempUserInfo().getNickname(), reqDto.getTempUserInfo().getPassword())
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        String hostName = Optional.ofNullable(customUserAppointmentMappingRepository.findHostName(appointment.getId()))
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        boolean isParticipated = userAppointmentMappingRepository
                .existsByAppointmentSeqAndNicknameAndTempPasswordAndUserRole(
                        appointment.getId(), reqDto.getTempUserInfo().getNickname(), reqDto.getTempUserInfo().getPassword(), UserRole.GUEST);

        boolean isHost = userAppointmentMappingRepository
                .existsByAppointmentSeqAndNicknameAndTempPasswordAndUserRole(
                        appointment.getId(), reqDto.getTempUserInfo().getNickname(), reqDto.getTempUserInfo().getPassword(), UserRole.HOST);

        List<UserAppointmentMapping> userAppointmentMappingList =
                userAppointmentMappingRepository.findAllByAppointmentSeqAndUserRole(appointment.getId(), UserRole.GUEST);

        List<UserParticipationInfo> userParticipationInfoList = userAppointmentMappingList.stream()
                .map(tempAppointmentMapper::toUserParticipationInfo)
                .toList();

        return tempAppointmentMapper.toTempAppointmentInfoDetailRespDto(appointment, hostName, isParticipated, isHost, userParticipationInfoList);
    }

    @Override
    public List<TempAppointmentParticipationInfoRespDto> retrieveAppointmentParticipationInfo(TempAppointmentParticipationInfoReqDto reqDto) {
        Appointment appointment = customAppointmentRepository.findByAppointmentCodeAndTempUserInfo(reqDto.getAppointmentCode(),
                        reqDto.getTempUserInfo().getNickname(), reqDto.getTempUserInfo().getPassword())
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        List<UserAppointmentMapping> participationInfoList =
                userAppointmentMappingRepository.findAllByAppointmentSeqAndUserRole(appointment.getId(), UserRole.GUEST);

        return participationInfoList.stream().map(tempAppointmentMapper::to).toList();
    }

    @Override
    @Transactional
    public void deleteAppointment(DeleteTempAppointmentReqDto reqDto) {
        Long appointmentId = customAppointmentRepository.findByAppointmentCodeAndTempUserInfoAndUserRole(reqDto.getAppointmentCode(),
                        reqDto.getTempUserInfo().getNickname(), reqDto.getTempUserInfo().getPassword(), UserRole.HOST)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT))
                .getId();

        userAppointmentMappingRepository.deleteAllByAppointmentSeq(appointmentId);
        appointmentRepository.deleteById(appointmentId);
    }

    @Override
    @Transactional
    public void deleteAppointmentParticipation(DeleteTempAppointmentParticipationReqDto reqDto) {
        Long appointmentId = customAppointmentRepository.findByAppointmentCodeAndTempUserInfoAndUserRole(reqDto.getAppointmentCode(),
                        reqDto.getTempUserInfo().getNickname(), reqDto.getTempUserInfo().getPassword(), UserRole.GUEST)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT))
                .getId();

        userAppointmentMappingRepository.deleteByAppointmentSeqAndNicknameAndTempPasswordAndUserRole(
                appointmentId, reqDto.getTempUserInfo().getNickname(), reqDto.getTempUserInfo().getPassword(), UserRole.GUEST);
    }

    @Override
    @Transactional
    public void updateAppointment(UpdateTempAppointmentReqDto reqDto) {
        Appointment appointment = customAppointmentRepository.findByAppointmentCodeAndTempUserInfoAndUserRole(reqDto.getAppointmentCode(),
                        reqDto.getTempUserInfo().getNickname(), reqDto.getTempUserInfo().getPassword(), UserRole.HOST)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        appointment.update(reqDto.getAppointmentName(),
                reqDto.getAddress(), reqDto.getCandidateDateList(), reqDto.getNotice());
    }

    @Override
    public void updateAppointmentParticipation(UpdateTempAppointmentParticipationReqDto reqDto) {
        Appointment appointment = appointmentRepository.findByAppointmentCode(reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        UserAppointmentMapping userAppointmentMapping = userAppointmentMappingRepository
                .findUserAppointmentMappingByAppointmentSeqAndNicknameAndTempPasswordAndUserRole(
                        appointment.getId(), reqDto.getTempUserInfo().getNickname(), reqDto.getTempUserInfo().getPassword(), UserRole.GUEST)
                .orElseThrow(() -> new AppointmentException(ErrorCode.APPOINTMENT_NOT_PARTICIPATE));

        userAppointmentMapping.update(reqDto.getSelectedDateTimeList());
    }


    @Override
    @Transactional
    public void confirmedAppointment(TempConfirmedAppointmentReqDto reqDto) {
        Appointment appointment = customAppointmentRepository.findByAppointmentCodeAndTempUserInfoAndUserRole(reqDto.getAppointmentCode(),
                        reqDto.getTempUserInfo().getNickname(), reqDto.getTempUserInfo().getPassword(), UserRole.HOST)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        reqDto.getNicknameList().stream()
                .map(nickname -> userAppointmentMappingRepository
                        .findUserAppointmentMappingByAppointmentSeqAndNicknameAndUserRole(appointment.getId(), nickname, UserRole.GUEST))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(mapping -> mapping.updateIsAvailable(true));

        appointment.confirmed(reqDto.getConfirmedDateTime());
    }

    @Override
    public List<TempAppointmentCandidateDateInfoRespDto> retrieveAppointmentCandidateDate(TempAppointmentCandidateDateInfoReqDto reqDto) {
        Appointment appointment = customAppointmentRepository.findByAppointmentCodeAndTempUserInfoAndUserRole(reqDto.getAppointmentCode(),
                        reqDto.getTempUserInfo().getNickname(), reqDto.getTempUserInfo().getPassword(), UserRole.HOST)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        List<LocalDate> candidateDateList = appointment.getCandidateDateList();

        List<UserAppointmentMapping> participationInfoList =
                userAppointmentMappingRepository.findAllByAppointmentSeqAndUserRole(appointment.getId(), UserRole.GUEST);

        UserAppointmentMapping hostUserAppointMapping =
                userAppointmentMappingRepository.findAllByAppointmentSeqAndUserRole(appointment.getId(), UserRole.HOST).stream()
                        .findAny().orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        List<String> participants = participationInfoList.stream()
                .map(UserAppointmentMapping::getNickname).toList();

        List<Set<String>> combinations = MathUtils.combinations(participants);

        List<TempAppointmentCandidateDateInfoRespDto> candidateDateInfoRespDtos = new ArrayList<>();

        combinations.forEach(
                combination -> {
                    List<TempAppointmentCandidateDateInfoRespDto.UserParticipationInfo> userParticipationInfoList =
                            participationInfoList.stream()
                                    .map(participationInfo -> {
                                        UserRole userRole =
                                                StringUtils.equals(participationInfo.getNickname(), hostUserAppointMapping.getNickname()) ?
                                                        UserRole.HOST : UserRole.GUEST;

                                        return TempAppointmentCandidateDateInfoRespDto.UserParticipationInfo.builder()
                                                .nickname(participationInfo.getNickname())
                                                .userRole(userRole)
                                                .userAuthType(participationInfo.getUserAuthType())
                                                .participant(combination.contains(participationInfo.getNickname()))
                                                .build();
                                    })
                                    .toList();

                    List<UserAppointmentMapping> filteredParticipationInfoList = participationInfoList.stream()
                            .filter(participationInfo -> combination.contains(participationInfo.getNickname()))
                            .toList();

                    List<List<SelectedDateTime>> filteredSelectedDatesList = filteredParticipationInfoList.stream()
                            .map(UserAppointmentMapping::getSelectedDateTimeList)
                            .toList();

                    candidateDateList.forEach(
                            candidateDate -> {
                                // 24시간 동안 1시간 단위로 해당 조합의 멤버 모두 참여 가능한 시간 리스트 구하기 ex) [1,2,3,5,6,10, ...]
                                List<Integer> timeList = new ArrayList<>();
                                for (int nowHour = 0; nowHour < 24; nowHour++) {

                                    int includedCount = 0;
                                    for (List<SelectedDateTime> selectedDateTimes : filteredSelectedDatesList) {
                                        for (SelectedDateTime selectedDateTime : selectedDateTimes) {
                                            LocalDate date = selectedDateTime.getSelectedDate();
                                            int startHour = selectedDateTime.getSelectedStartTime().getHour();
                                            int endHour = selectedDateTime.getSelectedEndTime().getHour();

                                            if (candidateDate.equals(date) && (startHour <= nowHour && nowHour <= endHour)) {
                                                includedCount++;
                                            }
                                        }
                                    }

                                    if (includedCount == filteredParticipationInfoList.size()) {
                                        timeList.add(nowHour);
                                    }
                                }

                                // 시간 리스트에서 연속적인 시간 그룹 찾기 ex) [1,2,3,4,10,11,18] -> [[1,2,3,4],[10,11],[18]]
                                if (ObjectUtils.isNotEmpty(timeList)) {

                                    int start = timeList.get(0);
                                    int end = start;

                                    for (int i = 1; i < timeList.size(); i++) {
                                        if (timeList.get(i) == end + 1) {
                                            end = timeList.get(i);
                                        } else {
                                            // check
                                            int finalStart = start;
                                            int finalEnd = end;
                                            boolean isDuplicated = candidateDateInfoRespDtos.stream()
                                                    .anyMatch(
                                                            candidateDateInfo -> {
                                                                LocalDate date = candidateDateInfo.getCandidateDate();
                                                                LocalTime startTime = candidateDateInfo.getStartTime();
                                                                LocalTime endTime = candidateDateInfo.getEndTime();

                                                                // 날짜 및 시간 동일한지 확인
                                                                boolean isEqualDateTime =
                                                                        date.equals(candidateDate) &&
                                                                                startTime.getHour() == finalStart && endTime.getHour() == finalEnd;
                                                                // 참여 가능 멤버가 동일한지 확인
                                                                boolean isEqualParticipants =
                                                                        candidateDateInfo.getUserParticipationInfoList().stream()
                                                                                .filter(p -> combination.contains(p.getNickname()))
                                                                                .allMatch(TempAppointmentCandidateDateInfoRespDto.UserParticipationInfo::isParticipant);

                                                                return isEqualDateTime && isEqualParticipants;
                                                            }
                                                    );

                                            if (!isDuplicated) {
                                                TempAppointmentCandidateDateInfoRespDto appointmentCandidateDateInfoRespDto = TempAppointmentCandidateDateInfoRespDto.builder()
                                                        .candidateDate(candidateDate)
                                                        .startTime(LocalTime.of(start, 0))
                                                        .endTime(LocalTime.of(end, 0))
                                                        .userParticipationInfoList(userParticipationInfoList)
                                                        .build();

                                                candidateDateInfoRespDtos.add(appointmentCandidateDateInfoRespDto);
                                            }

                                            start = end = timeList.get(i);
                                        }
                                    }
                                    // check
                                    int finalStart = start;
                                    int finalEnd = end;
                                    boolean isDuplicated = candidateDateInfoRespDtos.stream()
                                            .anyMatch(
                                                    candidateDateInfo -> {
                                                        LocalDate date = candidateDateInfo.getCandidateDate();
                                                        LocalTime startTime = candidateDateInfo.getStartTime();
                                                        LocalTime endTime = candidateDateInfo.getEndTime();

                                                        // 날짜 및 시간 동일한지 확인
                                                        boolean isEqualDateTime =
                                                                date.equals(candidateDate) &&
                                                                        startTime.getHour() == finalStart && endTime.getHour() == finalEnd;
                                                        // 참여 가능 멤버가 동일한지 확인
                                                        boolean isEqualParticipants =
                                                                candidateDateInfo.getUserParticipationInfoList().stream()
                                                                        .filter(p -> combination.contains(p.getNickname()))
                                                                        .allMatch(TempAppointmentCandidateDateInfoRespDto.UserParticipationInfo::isParticipant);

                                                        return isEqualDateTime && isEqualParticipants;
                                                    }
                                            );

                                    if (!isDuplicated) {
                                        // check
                                        TempAppointmentCandidateDateInfoRespDto appointmentCandidateDateInfoRespDto = TempAppointmentCandidateDateInfoRespDto.builder()
                                                .candidateDate(candidateDate)
                                                .startTime(LocalTime.of(start, 0))
                                                .endTime(LocalTime.of(end, 0))
                                                .userParticipationInfoList(userParticipationInfoList)
                                                .build();
                                        candidateDateInfoRespDtos.add(appointmentCandidateDateInfoRespDto);
                                    }
                                }

                            }
                    );

                }
        );

        return candidateDateInfoRespDtos;
    }
}
