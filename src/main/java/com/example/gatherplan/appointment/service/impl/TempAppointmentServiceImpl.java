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
@Transactional(readOnly = true)
@Slf4j
public class TempAppointmentServiceImpl implements TempAppointmentService {

    private final TempAppointmentMapper tempAppointmentMapper;

    private final AppointmentRepository appointmentRepository;
    private final UserAppointmentMappingRepository userAppointmentMappingRepository;

    private final CustomUserRepository customUserRepository;
    private final CustomAppointmentRepository customAppointmentRepository;

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
        Appointment appointment = customAppointmentRepository.findByAppointmentCodeAndTempUserInfo(reqDto.getAppointmentCode(),
                        reqDto.getTempUserInfo().getNickname(), reqDto.getTempUserInfo().getPassword())
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        String hostName = userAppointmentMappingRepository.findByAppointmentCodeAndUserRole(reqDto.getAppointmentCode(), UserRole.HOST)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND))
                .getNickname();

        boolean isParticipated = userAppointmentMappingRepository
                .existsByAppointmentCodeAndNicknameAndTempPasswordAndUserRole(
                        reqDto.getAppointmentCode(), reqDto.getTempUserInfo().getNickname(), reqDto.getTempUserInfo().getPassword(), UserRole.GUEST);

        boolean isHost = userAppointmentMappingRepository
                .existsByAppointmentCodeAndNicknameAndTempPasswordAndUserRole(
                        reqDto.getAppointmentCode(), reqDto.getTempUserInfo().getNickname(), reqDto.getTempUserInfo().getPassword(), UserRole.HOST);

        List<UserAppointmentMapping> userAppointmentMappingList =
                userAppointmentMappingRepository.findAllByAppointmentCodeAndUserRole(reqDto.getAppointmentCode(), UserRole.GUEST);

        List<UserParticipationInfo> userParticipationInfoList = userAppointmentMappingList.stream()
                .map(tempAppointmentMapper::toUserParticipationInfo)
                .toList();

        return tempAppointmentMapper.toTempAppointmentInfoDetailRespDto(appointment, hostName, isParticipated, isHost, userParticipationInfoList);
    }

    @Override
    @Transactional
    public void updateAppointment(UpdateTempAppointmentReqDto reqDto) {
        Appointment appointment = customAppointmentRepository.findByAppointmentCodeAndTempUserInfoAndUserRole(reqDto.getAppointmentCode(),
                        reqDto.getTempUserInfo().getNickname(), reqDto.getTempUserInfo().getPassword(), UserRole.HOST)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        userAppointmentMappingRepository.deleteAllByAppointmentCodeAndUserRole(appointment.getAppointmentCode(), UserRole.GUEST);

        appointment.update(reqDto.getAppointmentName(), reqDto.getAddress(), reqDto.getCandidateDateList(), reqDto.getNotice());
    }

    @Override
    @Transactional
    public void deleteAppointment(DeleteTempAppointmentReqDto reqDto) {
        Appointment appointment = customAppointmentRepository
                .findByAppointmentCodeAndTempUserInfoAndUserRole(reqDto.getAppointmentCode(), reqDto.getTempUserInfo().getNickname(), reqDto.getTempUserInfo().getPassword(), UserRole.HOST)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        userAppointmentMappingRepository.deleteAllByAppointmentCode(reqDto.getAppointmentCode());
        appointmentRepository.deleteById(appointment.getId());
    }

    @Override
    @Transactional
    public void registerAppointmentJoin(CreateTempAppointmentJoinReqDto reqDto) {
        userAppointmentMappingRepository
                .findByAppointmentCodeAndNicknameAndTempPasswordAndUserRole(reqDto.getAppointmentCode(),reqDto.getTempUserInfo().getNickname(),reqDto.getTempUserInfo().getPassword(),UserRole.GUEST)
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
        Appointment appointment = customAppointmentRepository
                .findByAppointmentCodeAndTempUserInfo(reqDto.getAppointmentCode(), reqDto.getTempUserInfo().getNickname(),reqDto.getTempUserInfo().getPassword())
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        List<UserAppointmentMapping> participationInfoList =
                userAppointmentMappingRepository.findAllByAppointmentCodeAndUserRole(appointment.getAppointmentCode(), UserRole.GUEST);

        return participationInfoList.stream().map(tempAppointmentMapper::to).toList();
    }

    @Override
    public void updateAppointmentJoin(UpdateTempAppointmentJoinReqDto reqDto) {
        UserAppointmentMapping userAppointmentMapping = userAppointmentMappingRepository
                .findByAppointmentCodeAndNicknameAndTempPasswordAndUserRole(
                        reqDto.getAppointmentCode(), reqDto.getTempUserInfo().getNickname(), reqDto.getTempUserInfo().getPassword(), UserRole.GUEST)
                .orElseThrow(() -> new AppointmentException(ErrorCode.APPOINTMENT_NOT_PARTICIPATE));

        userAppointmentMapping.update(reqDto.getSelectedDateTimeList());
    }

    @Override
    @Transactional
    public void deleteAppointmentJoin(DeleteTempAppointmentJoinReqDto reqDto) {
        UserAppointmentMapping userAppointmentMapping = userAppointmentMappingRepository
                .findByAppointmentCodeAndNicknameAndTempPasswordAndUserRole(
                        reqDto.getAppointmentCode(), reqDto.getTempUserInfo().getNickname(), reqDto.getTempUserInfo().getPassword(), UserRole.GUEST)
                .orElseThrow(() -> new AppointmentException(ErrorCode.APPOINTMENT_NOT_PARTICIPATE));

        userAppointmentMappingRepository.deleteById(userAppointmentMapping.getId());
    }

    @Override
    public List<TempAppointmentCandidateDatesRespDto> retrieveCandidateDates(TempAppointmentCandidateDatesReqDto reqDto) {
        Appointment appointment = customAppointmentRepository.findByAppointmentCodeAndTempUserInfoAndUserRole(reqDto.getAppointmentCode(),
                        reqDto.getTempUserInfo().getNickname(), reqDto.getTempUserInfo().getPassword(), UserRole.HOST)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        List<LocalDate> candidateDateList = appointment.getCandidateDateList();

        List<UserAppointmentMapping> participationInfoList =
                userAppointmentMappingRepository.findAllByAppointmentCodeAndUserRole(reqDto.getAppointmentCode(), UserRole.GUEST);

        UserAppointmentMapping hostUserAppointMapping =
                userAppointmentMappingRepository.findAllByAppointmentCodeAndUserRole(reqDto.getAppointmentCode(), UserRole.HOST).stream()
                        .findAny().orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        List<String> participants = participationInfoList.stream()
                .map(UserAppointmentMapping::getNickname).toList();

        List<Set<String>> combinations = MathUtils.combinations(participants);

        List<TempAppointmentCandidateDatesRespDto> candidateDateInfoRespDtos = new ArrayList<>();

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
                                                TempAppointmentCandidateDatesRespDto appointmentCandidateDateInfoRespDto = TempAppointmentCandidateDatesRespDto.builder()
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
                                                                        .allMatch(UserParticipationInfo::getIsAvailable);

                                                        return isEqualDateTime && isEqualParticipants;
                                                    }
                                            );

                                    if (!isDuplicated) {
                                        // check
                                        TempAppointmentCandidateDatesRespDto appointmentCandidateDateInfoRespDto = TempAppointmentCandidateDatesRespDto.builder()
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

    @Override
    @Transactional
    public void confirmAppointment(TempConfirmAppointmentReqDto reqDto) {
        Appointment appointment = customAppointmentRepository.findByAppointmentCodeAndTempUserInfoAndUserRole(reqDto.getAppointmentCode(),
                        reqDto.getTempUserInfo().getNickname(), reqDto.getTempUserInfo().getPassword(), UserRole.HOST)
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
    public boolean checkHost(TempCheckHostReqDto reqDto) {
        return userAppointmentMappingRepository
                .existsByAppointmentCodeAndNicknameAndTempPasswordAndUserRole(
                        reqDto.getAppointmentCode(), reqDto.getTempUserInfo().getNickname(), reqDto.getTempUserInfo().getPassword(), UserRole.HOST);
    }

    @Override
    public boolean checkJoin(TempCheckJoinReqDto reqDto) {
        return userAppointmentMappingRepository
                .existsByAppointmentCodeAndNicknameAndTempPasswordAndUserRole(
                        reqDto.getAppointmentCode(), reqDto.getTempUserInfo().getNickname(), reqDto.getTempUserInfo().getPassword(), UserRole.GUEST);
    }

    @Override
    @Transactional
    public boolean validJoin(CreateTempUserReqDto reqDto) {
        return customUserRepository.findAllUserNameByAppointmentCode(reqDto.getAppointmentCode()).stream()
                .noneMatch(findNickname -> StringUtils.equals(findNickname, reqDto.getTempUserInfo().getNickname()));
    }
}
