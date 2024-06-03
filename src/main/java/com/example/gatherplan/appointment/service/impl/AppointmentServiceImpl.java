package com.example.gatherplan.appointment.service.impl;

import com.example.gatherplan.appointment.dto.*;
import com.example.gatherplan.appointment.enums.AppointmentState;
import com.example.gatherplan.appointment.enums.UserAuthType;
import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.exception.AppointmentException;
import com.example.gatherplan.appointment.exception.UserException;
import com.example.gatherplan.appointment.mapper.AppointmentMapper;
import com.example.gatherplan.appointment.repository.*;
import com.example.gatherplan.appointment.repository.entity.Appointment;
import com.example.gatherplan.appointment.repository.entity.UserAppointmentMapping;
import com.example.gatherplan.appointment.service.AppointmentService;
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
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentMapper appointmentMapper;

    private final AppointmentRepository appointmentRepository;
    private final UserAppointmentMappingRepository userAppointmentMappingRepository;

    private final CustomUserRepository customUserRepository;
    private final CustomAppointmentRepository customAppointmentRepository;
    private final CustomUserAppointmentMappingRepository customUserAppointmentMappingRepository;

    @Override
    @Transactional
    public String registerAppointment(CreateAppointmentReqDto reqDto, Long userId, String name) {
        String appointmentCode = UuidUtils.generateRandomString(12);

        Appointment appointment = appointmentMapper.to(reqDto, AppointmentState.UNCONFIRMED, appointmentCode);

        appointmentRepository.save(appointment);

        UserAppointmentMapping userAppointmentMapping = UserAppointmentMapping.builder()
                .appointmentCode(appointmentCode)
                .userSeq(userId)
                .userRole(UserRole.HOST)
                .nickname(name)
                .userAuthType(UserAuthType.LOCAL)
                .build();

        userAppointmentMappingRepository.save(userAppointmentMapping);

        return appointmentCode;
    }

    @Override
    public AppointmentInfoRespDto retrieveAppointmentInfo(String appointmentCode, Long userId) {
        Appointment appointment = customAppointmentRepository.findByAppointmentCodeAndUserSeq(appointmentCode, userId)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        String hostName = userAppointmentMappingRepository.findByAppointmentCodeAndUserRole(appointmentCode, UserRole.HOST)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_HOST))
                .getNickname();

        boolean isParticipated = userAppointmentMappingRepository
                .existsByAppointmentCodeAndUserSeqAndUserRole(appointmentCode, userId, UserRole.GUEST);

        boolean isHost = userAppointmentMappingRepository
                .existsByAppointmentCodeAndUserSeqAndUserRole(appointmentCode, userId, UserRole.HOST);

        List<UserAppointmentMapping> userAppointmentMappingList =
                userAppointmentMappingRepository.findAllByAppointmentCodeAndUserRole(appointmentCode, UserRole.GUEST);

        List<UserParticipationInfo> userParticipationInfoList = userAppointmentMappingList.stream()
                .map(appointmentMapper::toUserParticipationInfo)
                .toList();

        return appointmentMapper.toAppointmentInfoDetailRespDto(appointment, hostName, isParticipated, isHost, userParticipationInfoList);
    }

    @Override
    @Transactional
    public void updateAppointment(UpdateAppointmentReqDto reqDto, Long userId) {
        Appointment appointment = customAppointmentRepository
                .findByAppointmentCodeAndUserSeqAndUserRole(reqDto.getAppointmentCode(), userId, UserRole.HOST)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        userAppointmentMappingRepository.deleteAllByAppointmentCodeAndUserRole(appointment.getAppointmentCode(), UserRole.GUEST);

        appointment.update(reqDto.getAppointmentName(), reqDto.getAddress(), reqDto.getCandidateDateList(), reqDto.getNotice());
    }

    @Override
    @Transactional
    public void deleteAppointment(String appointmentCode, Long userId) {
        Appointment appointment = customAppointmentRepository
                .findByAppointmentCodeAndUserSeqAndUserRole(appointmentCode, userId, UserRole.HOST)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        userAppointmentMappingRepository.deleteAllByAppointmentCode(appointmentCode);
        appointmentRepository.deleteById(appointment.getId());
    }

    @Override
    @Transactional
    public void registerAppointmentJoin(CreateAppointmentJoinReqDto reqDto, Long userId) {
        userAppointmentMappingRepository
                .findByAppointmentCodeAndUserSeqAndUserRole(reqDto.getAppointmentCode(), userId, UserRole.GUEST)
                .ifPresent(mapping -> {
                    throw new AppointmentException(ErrorCode.APPOINTMENT_ALREADY_PARTICIPATE);
                });

        Appointment appointment = appointmentRepository.findByAppointmentCode(reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        AppointmentValidator.retrieveInvalidSelectedDateTime(appointment.getCandidateDateList(), reqDto.getSelectedDateTimeList())
                .ifPresent(result -> {
                    throw new AppointmentException(ErrorCode.PARAMETER_VALIDATION_FAIL,
                            String.format("후보 날짜에서 벗어난 입력 값 입니다. %s", result));
                });

        UserAppointmentMapping userAppointmentMapping = UserAppointmentMapping.builder()
                .appointmentCode(reqDto.getAppointmentCode())
                .userSeq(userId)
                .userRole(UserRole.GUEST)
                .userAuthType(UserAuthType.LOCAL)
                .isAvailable(false)
                .selectedDateTimeList(List.copyOf(reqDto.getSelectedDateTimeList()))
                .nickname(reqDto.getNickname())
                .build();

        userAppointmentMappingRepository.save(userAppointmentMapping);
    }

    @Override
    public List<AppointmentParticipantsRespDto> retrieveAppointmentParticipants(String appointmentCode, Long userId) {
        Appointment appointment = customAppointmentRepository.findByAppointmentCodeAndUserSeq(appointmentCode, userId)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        List<UserAppointmentMapping> participationInfoList =
                userAppointmentMappingRepository.findAllByAppointmentCodeAndUserRole(appointment.getAppointmentCode(), UserRole.GUEST);

        return participationInfoList.stream().map(appointmentMapper::to).toList();
    }

    @Override
    @Transactional
    public void updateAppointmentJoin(UpdateAppointmentJoinReqDto reqDto, Long userId) {
        Appointment appointment = appointmentRepository.findByAppointmentCode(reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        UserAppointmentMapping userAppointmentMapping = userAppointmentMappingRepository
                .findByAppointmentCodeAndUserSeqAndUserRole(reqDto.getAppointmentCode(), userId, UserRole.GUEST)
                .orElseThrow(() -> new AppointmentException(ErrorCode.APPOINTMENT_NOT_PARTICIPATE));

        AppointmentValidator.retrieveInvalidSelectedDateTime(appointment.getCandidateDateList(), reqDto.getSelectedDateTimeList())
                .ifPresent(result -> {
                    throw new AppointmentException(ErrorCode.PARAMETER_VALIDATION_FAIL,
                            String.format("후보 날짜에서 벗어난 입력 값 입니다. %s", result));
                });

        userAppointmentMapping.update(reqDto.getSelectedDateTimeList());
    }

    @Override
    @Transactional
    public void deleteAppointmentJoin(String appointmentCode, Long userId) {
        UserAppointmentMapping userAppointmentMapping = userAppointmentMappingRepository
                .findByAppointmentCodeAndUserSeqAndUserRole(appointmentCode, userId, UserRole.GUEST)
                .orElseThrow(() -> new AppointmentException(ErrorCode.APPOINTMENT_NOT_PARTICIPATE));

        userAppointmentMappingRepository.deleteById(userAppointmentMapping.getId());
    }

    @Override
    public List<AppointmentCandidateDatesRespDto> retrieveCandidateDates(String appointmentCode, Long userId) {
        Appointment appointment = customAppointmentRepository.findByAppointmentCodeAndUserSeqAndUserRole(appointmentCode,
                        userId, UserRole.HOST)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        List<LocalDate> candidateDateList = appointment.getCandidateDateList();

        List<UserAppointmentMapping> participationInfoList =
                userAppointmentMappingRepository.findAllByAppointmentCodeAndUserRole(appointmentCode, UserRole.GUEST);

        UserAppointmentMapping hostUserAppointMapping =
                userAppointmentMappingRepository.findAllByAppointmentCodeAndUserRole(appointmentCode, UserRole.HOST).stream()
                        .findAny().orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        List<String> participants = participationInfoList.stream()
                .map(UserAppointmentMapping::getNickname).toList();

        List<Set<String>> combinations = MathUtils.combinations(participants);

        List<AppointmentCandidateDatesRespDto> candidateDateInfoRespDtos = new ArrayList<>();

        combinations.forEach(
                combination -> {
                    List<UserParticipationInfo> userParticipationInfoList =
                            participationInfoList.stream()
                                    .map(participationInfo -> {
                                        UserRole userRole =
                                                StringUtils.equals(participationInfo.getNickname(), hostUserAppointMapping.getNickname()) ?
                                                        UserRole.HOST : UserRole.GUEST;

                                        return UserParticipationInfo.builder()
                                                .nickname(participationInfo.getNickname())
                                                .userRole(userRole)
                                                .userAuthType(participationInfo.getUserAuthType())
                                                .isAvailable(combination.contains(participationInfo.getNickname()))
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
                                                                                .allMatch(UserParticipationInfo::getIsAvailable);

                                                                return isEqualDateTime && isEqualParticipants;
                                                            }
                                                    );

                                            if (!isDuplicated) {
                                                AppointmentCandidateDatesRespDto appointmentCandidateDatesRespDto = AppointmentCandidateDatesRespDto.builder()
                                                        .candidateDate(candidateDate)
                                                        .startTime(LocalTime.of(start, 0))
                                                        .endTime(LocalTime.of(end, 0))
                                                        .userParticipationInfoList(userParticipationInfoList)
                                                        .build();

                                                candidateDateInfoRespDtos.add(appointmentCandidateDatesRespDto);
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
                                                                        .allMatch(UserParticipationInfo::getIsAvailable);

                                                        return isEqualDateTime && isEqualParticipants;
                                                    }
                                            );

                                    if (!isDuplicated) {
                                        // check
                                        AppointmentCandidateDatesRespDto appointmentCandidateDatesRespDto = AppointmentCandidateDatesRespDto.builder()
                                                .candidateDate(candidateDate)
                                                .startTime(LocalTime.of(start, 0))
                                                .endTime(LocalTime.of(end, 0))
                                                .userParticipationInfoList(userParticipationInfoList)
                                                .build();
                                        candidateDateInfoRespDtos.add(appointmentCandidateDatesRespDto);
                                    }
                                }

                            }
                    );

                }
        );

        return candidateDateInfoRespDtos;
    }

    @Override
    @Transactional
    public void confirmAppointment(ConfirmAppointmentReqDto reqDto, Long userId) {
        Appointment appointment = customAppointmentRepository.findByAppointmentCodeAndUserSeqAndUserRole(reqDto.getAppointmentCode(),
                        userId, UserRole.HOST)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        reqDto.getNicknameList().stream()
                .map(nickname -> userAppointmentMappingRepository
                        .findUserAppointmentMappingByAppointmentCodeAndNicknameAndUserRole(reqDto.getAppointmentCode(), nickname, UserRole.GUEST))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(mapping -> mapping.updateIsAvailable(true));

        appointment.confirmed(reqDto.getConfirmedDateTime());
    }

    @Override
    public boolean checkHost(String appointmentCode, Long userId) {
        return userAppointmentMappingRepository
                .existsByAppointmentCodeAndUserSeqAndUserRole(appointmentCode, userId, UserRole.HOST);
    }

    @Override
    public boolean checkJoin(String appointmentCode, Long userId) {
        return userAppointmentMappingRepository
                .existsByAppointmentCodeAndUserSeqAndUserRole(appointmentCode, userId, UserRole.GUEST);
    }

    @Override
    public boolean checkName(String appointmentCode, String name) {
        return customUserRepository.findAllUserNameByAppointmentCode(appointmentCode).stream()
                .noneMatch(findNickname -> StringUtils.equals(findNickname, name));
    }

    @Override
    public boolean checkNickname(String appointmentCode, String nickname) {
        return customUserRepository.findAllUserNameByAppointmentCode(appointmentCode).stream()
                .noneMatch(findNickname -> StringUtils.equals(findNickname, nickname));
    }

    @Override
    public List<AppointmentSearchListRespDto> retrieveAppointmentSearchList(String keyword, Long userId, String name) {
        return customAppointmentRepository.findAppointmentSearchListRespDtoList(keyword, userId, name);
    }

    @Override
    public AppointmentPreviewRespDto retrieveAppointmentPreview(String appointmentCode) {
        Appointment appointment = appointmentRepository.findByAppointmentCode(appointmentCode).orElseThrow(()
                -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        String hostName = userAppointmentMappingRepository.findByAppointmentCodeAndUserRole(appointmentCode, UserRole.HOST)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND))
                .getNickname();

        return appointmentMapper.to(appointment, hostName);
    }
}
