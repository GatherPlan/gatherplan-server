package com.example.gatherplan.appointment.utils;

import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.repository.entity.UserAppointmentMapping;
import com.example.gatherplan.appointment.utils.unit.AppointmentCandidateInfo;
import com.example.gatherplan.common.unit.ConfirmedDateTime;
import com.example.gatherplan.common.unit.SelectedDateTime;
import com.example.gatherplan.common.unit.UserParticipationInfo;
import com.example.gatherplan.common.utils.MathUtils;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    private static List<Integer> getTimeListWhenAllAvailable(
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
                    int endHour = getEndTimeHour(selectedDateTime.getSelectedEndTime());

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
    private static void findContinuousTimeAndAddResult(
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
    private static boolean isDuplicated(Set<String> combination, LocalDate candidateDate,
                                        List<AppointmentCandidateInfo> candidateInfoList, int start, int end) {
        return candidateInfoList.stream()
                .anyMatch(
                        candidateDateInfo -> {
                            LocalDate date = candidateDateInfo.getCandidateDate();
                            int startTimeHour = candidateDateInfo.getStartTime().getHour();
                            int endTimeHour = candidateDateInfo.getEndTimeHour();

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

    private int getEndTimeHour(LocalTime endTime) {
        int hour = endTime.getHour();
        int minute = endTime.getMinute();
        return (hour == 23 && minute == 59) ? 24 : hour;
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


}
