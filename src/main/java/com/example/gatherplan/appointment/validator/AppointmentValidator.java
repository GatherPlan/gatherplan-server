package com.example.gatherplan.appointment.validator;

import com.example.gatherplan.appointment.enums.AppointmentState;
import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.exception.AppointmentException;
import com.example.gatherplan.appointment.exception.UserException;
import com.example.gatherplan.appointment.repository.entity.Appointment;
import com.example.gatherplan.appointment.repository.entity.UserAppointmentMapping;
import com.example.gatherplan.common.exception.ErrorCode;
import com.example.gatherplan.common.unit.SelectedDateTime;
import com.example.gatherplan.common.unit.TempUserInfo;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.List;

@UtilityClass
public class AppointmentValidator {

    /**
     * 게스트가 선택한 날이 호스트가 선택한 날에 포함이 되는지에 대한 validation
     *
     * @param candidateDateList    후보 날짜 (호스트가 선택한 날)
     * @param selectedDateTimeList 선택한 날짜 (게스트가 선택한 날)
     */
    public void validateSelectedDateTimeAndThrow(
            List<LocalDate> candidateDateList, List<SelectedDateTime> selectedDateTimeList) {
        selectedDateTimeList.stream()
                .filter(selectedDateTime ->
                        candidateDateList.stream()
                                .noneMatch(candidateDate -> candidateDate.isEqual(selectedDateTime.getSelectedDate()))
                )
                .findFirst()
                .ifPresent(result -> {
                    throw new AppointmentException(ErrorCode.PARAMETER_VALIDATION_FAIL, String.format("후보 날짜에서 벗어난 날짜입니다. %s", result));
                });
    }

    // validate
    public void validateIsUserHostAndThrow(Long userId, List<UserAppointmentMapping> mappingList) {
        boolean isHost = mappingList.stream()
                .anyMatch(mapping -> UserRole.HOST.equals(mapping.getUserRole()) && userId.equals(mapping.getUserSeq()));
        if (!isHost) {
            throw new UserException(ErrorCode.USER_NOT_HOST);
        }
    }

    public void validateIsUserHostAndThrow(TempUserInfo tempUserInfo, List<UserAppointmentMapping> mappingList) {
        boolean isHost = mappingList.stream()
                .anyMatch(mapping -> UserRole.HOST.equals(mapping.getUserRole()) && mapping.getNickname().equals(tempUserInfo.getNickname()) && tempUserInfo.getPassword().equals(mapping.getTempPassword()));
        if (!isHost) {
            throw new UserException(ErrorCode.USER_NOT_HOST);
        }
    }

    public void validateUserExistenceAndThrow(Long userId, List<UserAppointmentMapping> mappingList) {
        boolean isExists = mappingList.stream().anyMatch(mapping -> userId.equals(mapping.getUserSeq()));
        if (!isExists) {
            throw new UserException(ErrorCode.USER_NOT_RELATED_TO_APPOINTMENT);
        }
    }

    public void validateTempUserExistenceAndThrow(TempUserInfo tempUserInfo, List<UserAppointmentMapping> mappingList) {
        boolean isExists = mappingList.stream().anyMatch(mapping -> mapping.getNickname().equals(tempUserInfo.getNickname()) && tempUserInfo.getPassword().equals(mapping.getTempPassword()));
        if (!isExists) {
            throw new UserException(ErrorCode.USER_NOT_RELATED_TO_APPOINTMENT);
        }
    }

    public void validateAppointmentStateUnconfirmedAndThrow(Appointment appointment) {
        if (!AppointmentState.UNCONFIRMED.equals(appointment.getAppointmentState())) {
            throw new AppointmentException(ErrorCode.APPOINTMENT_ALREADY_CONFIRMED);
        }
    }

    public void validateIsUserHostOrJoinedAndThrow(Long userId, List<UserAppointmentMapping> mappings) {
        boolean isHostOrGuest = mappings.stream()
                .anyMatch(mapping -> userId.equals(mapping.getUserSeq()) && (mapping.getUserRole().equals(UserRole.GUEST) || mapping.getUserRole().equals(UserRole.HOST)));
        if (!isHostOrGuest) {
            throw new UserException(ErrorCode.USER_NOT_RELATED_TO_APPOINTMENT);
        }
    }

    public void validateIsUserHostOrJoinedAndThrow(TempUserInfo tempUserInfo, List<UserAppointmentMapping> mappings) {
        boolean isHostOrGuest = mappings.stream()
                .anyMatch(mapping -> mapping.getNickname().equals(tempUserInfo.getNickname()) && tempUserInfo.getPassword().equals(mapping.getTempPassword()) && (mapping.getUserRole().equals(UserRole.GUEST) || mapping.getUserRole().equals(UserRole.HOST)));
        if (!isHostOrGuest) {
            throw new UserException(ErrorCode.USER_NOT_RELATED_TO_APPOINTMENT);
        }
    }

    public boolean isUserHost(Long userId, List<UserAppointmentMapping> mappings) {
        return mappings.stream()
                .anyMatch(mapping -> userId.equals(mapping.getUserSeq()) && UserRole.HOST.equals(mapping.getUserRole()));
    }

    public boolean isUserHost(TempUserInfo tempUserInfo, List<UserAppointmentMapping> mappings) {
        return mappings.stream()
                .anyMatch(mapping -> mapping.getNickname().equals(tempUserInfo.getNickname()) && tempUserInfo.getPassword().equals(mapping.getTempPassword()) && UserRole.HOST.equals(mapping.getUserRole()));
    }

    public boolean isUserHostOrParticipated(TempUserInfo tempUserInfo, List<UserAppointmentMapping> mappings) {
        return mappings.stream()
                .anyMatch(mapping -> mapping.getNickname().equals(tempUserInfo.getNickname()) && tempUserInfo.getPassword().equals(mapping.getTempPassword()) && (mapping.getUserRole().equals(UserRole.GUEST) || mapping.getUserRole().equals(UserRole.HOST)));
    }

    public void validateIsUserJoined(TempUserInfo tempUserInfo, List<UserAppointmentMapping> mappings) {
        boolean isParticipated = mappings.stream()
                .anyMatch(mapping -> UserRole.GUEST.equals(mapping.getUserRole()) && mapping.getNickname().equals(tempUserInfo.getNickname()));
        if (isParticipated) {
            throw new UserException(ErrorCode.USER_ALREADY_JOINED_APPOINTMENT);
        }
    }

    public void validateIsUserJoined(Long userId, List<UserAppointmentMapping> mappings) {
        boolean isParticipated = mappings.stream()
                .anyMatch(mapping -> UserRole.GUEST.equals(mapping.getUserRole()) && userId.equals(mapping.getUserSeq()));
        if (isParticipated) {
            throw new UserException(ErrorCode.USER_ALREADY_JOINED_APPOINTMENT);
        }
    }

    public boolean isUserParticipated(Long userId, List<UserAppointmentMapping> mappingList) {
        return mappingList.stream().anyMatch(mapping -> userId.equals(mapping.getUserSeq()) && UserRole.GUEST.equals(mapping.getUserRole()));
    }

    public boolean isUserParticipated(TempUserInfo tempUserInfo, List<UserAppointmentMapping> mappingList) {
        return mappingList.stream().anyMatch(mapping -> mapping.getNickname().equals(tempUserInfo.getNickname()) && tempUserInfo.getPassword().equals(mapping.getTempPassword()) && UserRole.GUEST.equals(mapping.getUserRole()));
    }

    public boolean isNotDuplicatedName(String name, List<UserAppointmentMapping> userAppointmentMappingList) {
        return userAppointmentMappingList.stream().noneMatch(
                userAppointmentMapping -> StringUtils.equals(userAppointmentMapping.getNickname(), name));
    }

    public void validateNotDuplicatedName(String name, List<UserAppointmentMapping> userAppointmentMappingList) {
        if (userAppointmentMappingList.stream().anyMatch(
                userAppointmentMapping -> StringUtils.equals(userAppointmentMapping.getNickname(), name))) {
            throw new UserException(ErrorCode.USER_NAME_DUPLICATED_IN_APPOINTMENT);
        }
    }
}
