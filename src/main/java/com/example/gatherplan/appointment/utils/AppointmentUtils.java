package com.example.gatherplan.appointment.utils;

import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.exception.AppointmentException;
import com.example.gatherplan.appointment.exception.UserException;
import com.example.gatherplan.appointment.mapper.AppointmentMapper;
import com.example.gatherplan.appointment.mapper.TempAppointmentMapper;
import com.example.gatherplan.appointment.repository.entity.Appointment;
import com.example.gatherplan.appointment.repository.entity.UserAppointmentMapping;
import com.example.gatherplan.appointment.utils.unit.AppointmentCandidateInfo;
import com.example.gatherplan.common.exception.ErrorCode;
import com.example.gatherplan.common.unit.*;
import com.example.gatherplan.common.utils.MathUtils;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class AppointmentUtils {

    public List<AppointmentCandidateInfo> retrieveCandidateInfoList(
            List<LocalDate> candidateDateList, List<UserAppointmentMapping> userAppointmentMappingList, String hostNickname
    ) {
        List<String> participantsNicknames = userAppointmentMappingList.stream()
                .map(UserAppointmentMapping::getNickname).toList();

        List<Set<String>> participantsCombination = MathUtils.combinations(participantsNicknames);

        List<AppointmentCandidateInfo> candidateInfoList = new ArrayList<>();

        participantsCombination.forEach(
                combination -> {
                    List<UserParticipationInfo> userParticipationInfoList =
                            userAppointmentMappingList.stream()
                                    .map(participationInfo -> {
                                        UserRole userRole =
                                                StringUtils.equals(participationInfo.getNickname(), hostNickname) ?
                                                        UserRole.HOST : UserRole.GUEST;

                                        return UserParticipationInfo.builder()
                                                .nickname(participationInfo.getNickname())
                                                .userRole(userRole)
                                                .userAuthType(participationInfo.getUserAuthType())
                                                .isAvailable(combination.contains(participationInfo.getNickname()))
                                                .build();
                                    })
                                    .toList();

                    List<UserAppointmentMapping> filteredParticipationInfoList = userAppointmentMappingList.stream()
                            .filter(participationInfo -> combination.contains(participationInfo.getNickname()))
                            .toList();

                    List<List<SelectedDateTime>> filteredSelectedDatesList = filteredParticipationInfoList.stream()
                            .map(UserAppointmentMapping::getSelectedDateTimeList)
                            .toList();

                    candidateDateList.forEach(
                            candidateDate -> {
                                List<Integer> timeList =
                                        getTimeListWhenAllAvailable(candidateDate,
                                                filteredSelectedDatesList, filteredParticipationInfoList);

                                if (ObjectUtils.isNotEmpty(timeList)) {
                                    findContinuousTimeAndAddResult(combination, candidateDate, timeList,
                                            candidateInfoList, userParticipationInfoList);
                                }
                            }
                    );

                }
        );

        return candidateInfoList;
    }

    public List<UserAppointmentMapping> retrieveAvailableUserList(ConfirmedDateTime confirmedDateTime, List<UserAppointmentMapping> userGuestList) {
        LocalDate confirmedDate = confirmedDateTime.getConfirmedDate();
        LocalTime confirmedStartTime = confirmedDateTime.getConfirmedStartTime();
        LocalTime confirmedEndTime = confirmedDateTime.getConfirmedEndTime();

        return userGuestList.stream()
                .filter(u ->
                        u.getSelectedDateTimeList().stream()
                                .anyMatch(s -> {
                                    boolean isAvailableDate = confirmedDate.isEqual(s.getSelectedDate());
                                    boolean isAvailableTime = !confirmedStartTime.isBefore(s.getSelectedStartTime())
                                            && !confirmedEndTime.isAfter(s.getSelectedEndTime());
                                    return isAvailableDate && isAvailableTime;
                                }))
                .toList();
    }

    // 24시간 동안 1시간 단위로 해당 조합의 멤버 모두 참여 가능한 시간 리스트 구하기 ex) [1,2,3,5,6,10, ...]
    private List<Integer> getTimeListWhenAllAvailable(
            LocalDate candidateDate, List<List<SelectedDateTime>> filteredSelectedDatesList,
            List<UserAppointmentMapping> filteredParticipationInfoList
    ) {
        List<Integer> timeList = new ArrayList<>();
        for (int nowHour = 0; nowHour <= 24; nowHour++) {

            int includedCount = 0;
            for (List<SelectedDateTime> selectedDateTimes : filteredSelectedDatesList) {
                for (SelectedDateTime selectedDateTime : selectedDateTimes) {
                    LocalDate date = selectedDateTime.getSelectedDate();
                    int startHour = selectedDateTime.getSelectedStartTime().getHour();
                    int endHour = localEndTimeToHour(selectedDateTime.getSelectedEndTime());

                    if (candidateDate.equals(date) && (startHour <= nowHour && nowHour <= endHour)) {
                        includedCount++;
                    }
                }
            }

            if (includedCount == filteredParticipationInfoList.size()) {
                timeList.add(nowHour);
            }
        }
        return timeList;
    }

    // 시간 리스트에서 연속적인 시간 그룹 찾기 ex) [1,2,3,4,10,11,18] -> [[1,2,3,4],[10,11]] // 18은 들어가면 안됨 -> start != end
    private void findContinuousTimeAndAddResult(
            Set<String> combination, LocalDate candidateDate, List<Integer> timeList,
            List<AppointmentCandidateInfo> candidateInfoList, List<UserParticipationInfo> userParticipationInfoList) {
        int start = timeList.get(0);
        int end = start;

        for (int i = 1; i < timeList.size(); i++) {
            if (timeList.get(i) == end + 1) {
                end = timeList.get(i);
            } else {
                boolean isDuplicated = isDuplicated(combination, candidateDate, candidateInfoList, start, end);
                if (start != end && !isDuplicated) {
                    AppointmentCandidateInfo appointmentCandidateInfo =
                            AppointmentCandidateInfo.of(candidateDate, start, end, userParticipationInfoList);

                    candidateInfoList.add(appointmentCandidateInfo);
                }

                start = end = timeList.get(i);
            }
        }
        boolean isDuplicated = isDuplicated(combination, candidateDate, candidateInfoList, start, end);
        if (start != end && !isDuplicated) {
            AppointmentCandidateInfo appointmentCandidateInfo =
                    AppointmentCandidateInfo.of(candidateDate, start, end, userParticipationInfoList);

            candidateInfoList.add(appointmentCandidateInfo);
        }
    }

    // 현재 나온 결과가 이미 반영된 결과인지 확인
    private boolean isDuplicated(Set<String> combination, LocalDate candidateDate,
                                 List<AppointmentCandidateInfo> candidateInfoList, int start, int end) {
        return candidateInfoList.stream()
                .anyMatch(
                        candidateDateInfo -> {
                            LocalDate date = candidateDateInfo.getCandidateDate();
                            int startTimeHour = candidateDateInfo.getStartTime().getHour();
                            int endTimeHour = localEndTimeToHour(candidateDateInfo.getEndTime());

                            // 날짜 및 시간 동일한지 확인
                            boolean isEqualDateTime =
                                    date.equals(candidateDate) &&
                                            (startTimeHour == start && endTimeHour == end);
                            // 참여 가능 멤버가 동일한지 확인
                            boolean isEqualParticipants =
                                    candidateDateInfo.getUserParticipationInfoList().stream()
                                            .filter(p -> combination.contains(p.getNickname()))
                                            .allMatch(UserParticipationInfo::getIsAvailable);

                            return isEqualDateTime && isEqualParticipants;
                        }
                );
    }

    public List<UserParticipationInfo> retrieveUserParticipationInfoList(List<UserAppointmentMapping> userAppointmentMappingList, String hostName) {
        return userAppointmentMappingList.stream()
                .filter(mapping -> UserRole.GUEST.equals(mapping.getUserRole()))
                .map(mapping -> UserParticipationInfo.builder()
                        .nickname(mapping.getNickname())
                        .userRole(StringUtils.equals(hostName, mapping.getNickname()) ? UserRole.HOST : UserRole.GUEST)
                        .userAuthType(mapping.getUserAuthType())
                        .isAvailable(mapping.isAvailable())
                        .build())
                .toList();
    }


    public String findHostName(List<UserAppointmentMapping> mappingList) {
        return mappingList.stream()
                .filter(mapping -> UserRole.HOST.equals(mapping.getUserRole()))
                .map(UserAppointmentMapping::getNickname)
                .findFirst()
                .orElseThrow(() -> new AppointmentException(ErrorCode.HOST_NOT_FOUND_IN_APPOINTMENT,
                        "잘못된 약속 정보가 존재합니다."));
    }

    public List<String> findAppointmentCodeListByAppointmentList(List<Appointment> appointmentList) {
        return appointmentList.stream()
                .map(Appointment::getAppointmentCode)
                .toList();
    }

    public UserAppointmentMapping findGuestMapping(Long userId, List<UserAppointmentMapping> mappingList) {
        return mappingList.stream()
                .filter(mapping -> UserRole.GUEST.equals(mapping.getUserRole()) && userId.equals(mapping.getUserSeq()))
                .findFirst()
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_RELATED_TO_APPOINTMENT));
    }

    public UserAppointmentMapping findGuestMapping(TempUserInfo tempUserInfo, List<UserAppointmentMapping> mappingList) {
        return mappingList.stream()
                .filter(mapping -> UserRole.GUEST.equals(mapping.getUserRole()) && mapping.getNickname().equals(tempUserInfo.getNickname()) && tempUserInfo.getPassword().equals(mapping.getTempPassword()))
                .findFirst()
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_RELATED_TO_APPOINTMENT));
    }

    public Map<String, String> findHostNameList(List<UserAppointmentMapping> hostMappingList) {
        return hostMappingList.stream()
                .collect(Collectors.toMap(UserAppointmentMapping::getAppointmentCode, UserAppointmentMapping::getNickname));
    }

    public List<ParticipationInfo> convertToParticipationInfoList(List<UserAppointmentMapping> userAppointmentMappingList, String hostName, AppointmentMapper appointmentMapper) {
        return userAppointmentMappingList.stream()
                .filter(mapping -> UserRole.GUEST.equals(mapping.getUserRole()))
                .map(mapping -> appointmentMapper.toParticipationInfo(mapping, StringUtils.equals(hostName, mapping.getNickname()) ? UserRole.HOST : UserRole.GUEST))
                .toList();
    }

    public List<ParticipationInfo> convertToParticipationInfoList(List<UserAppointmentMapping> userAppointmentMappingList, String hostName, TempAppointmentMapper tempAppointmentMapper) {
        return userAppointmentMappingList.stream()
                .filter(mapping -> UserRole.GUEST.equals(mapping.getUserRole()))
                .map(mapping -> tempAppointmentMapper.toParticipationInfo(mapping, StringUtils.equals(hostName, mapping.getNickname()) ? UserRole.HOST : UserRole.GUEST))
                .toList();
    }

    /**
     * 종료 LocalTime 시간 변환
     * LocalTime 에 24시가 없으므로 종료 시간 중 하나인 23시 59분의 시간반환을 24로 반환해 줌
     *
     * @param endTime 약속종료시간
     * @return 시간(HH)
     */
    public int localEndTimeToHour(LocalTime endTime) {
        int endHour = endTime.getHour();
        int endMinute = endTime.getMinute();
        return (endHour == 23 && endMinute == 59) ? 24 : endHour;
    }

    /**
     * 종료 시간(HH) LocalTIme 변환
     * LocalTime 에 24시가 없으므로 시간이 24시 일 경우 23시 59분의 LocalTime 으로 반환해 줌
     *
     * @param endHour 시간
     * @return 종료시각
     */
    public LocalTime hourToLocalEndTime(int endHour) {
        return endHour == 24 ? LocalTime.of(23, 59) : LocalTime.of(endHour, 0);
    }

}
