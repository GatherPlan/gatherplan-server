package com.example.gatherplan.appointment.validator;

import com.example.gatherplan.appointment.enums.AppointmentState;
import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.exception.AppointmentException;
import com.example.gatherplan.appointment.exception.UserException;
import com.example.gatherplan.appointment.mapper.AppointmentMapper;
import com.example.gatherplan.appointment.mapper.TempAppointmentMapper;
import com.example.gatherplan.appointment.repository.entity.Appointment;
import com.example.gatherplan.appointment.repository.entity.UserAppointmentMapping;
import com.example.gatherplan.common.exception.ErrorCode;
import com.example.gatherplan.common.unit.ParticipationInfo;
import com.example.gatherplan.common.unit.SelectedDateTime;
import com.example.gatherplan.common.unit.TempUserInfo;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@UtilityClass
public class AppointmentValidator {

    public Optional<SelectedDateTime> retrieveInvalidSelectedDateTime(
            List<LocalDate> candidateDateList, List<SelectedDateTime> selectedDateTimeList) {

        return selectedDateTimeList.stream()
                .filter(selectedDateTime ->
                        candidateDateList.stream()
                                .noneMatch(candidateDate -> candidateDate.isEqual(selectedDateTime.getSelectedDate()))
                )
                .findFirst();
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
            throw new UserException(ErrorCode.NOT_FOUND_USER_APPOINTMENT_MAPPING);
        }
    }

    public void validateTempUserExistenceAndThrow(TempUserInfo tempUserInfo, List<UserAppointmentMapping> mappingList) {
        boolean isExists = mappingList.stream().anyMatch(mapping -> mapping.getNickname().equals(tempUserInfo.getNickname()) && tempUserInfo.getPassword().equals(mapping.getTempPassword()));
        if (!isExists) {
            throw new UserException(ErrorCode.NOT_FOUND_USER_APPOINTMENT_MAPPING);
        }
    }

    public void validateAppointmentStateUnconfirmedAndThrow(Appointment appointment) {
        if (!AppointmentState.UNCONFIRMED.equals(appointment.getAppointmentState())) {
            throw new AppointmentException(ErrorCode.APPOINTMENT_ALREADY_CONFIRMED);
        }
    }

    public String findHostName(List<UserAppointmentMapping> mappingList) {
        return mappingList.stream()
                .filter(mapping -> UserRole.HOST.equals(mapping.getUserRole()))
                .map(UserAppointmentMapping::getNickname)
                .findFirst()
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_HOST));
    }

    public void validateIsUserHostOrParticipatedAndThrow(Long userId, List<UserAppointmentMapping> mappings) {
        boolean isHostOrGuest = mappings.stream()
                .anyMatch(mapping -> userId.equals(mapping.getUserSeq()) && (mapping.getUserRole().equals(UserRole.GUEST) || mapping.getUserRole().equals(UserRole.HOST)));
        if (!isHostOrGuest) {
            throw new UserException(ErrorCode.NOT_FOUND_USER_APPOINTMENT_MAPPING);
        }
    }

    public void validateIsUserHostOrParticipatedAndThrow(TempUserInfo tempUserInfo, List<UserAppointmentMapping> mappings) {
        boolean isHostOrGuest = mappings.stream()
                .anyMatch(mapping -> mapping.getNickname().equals(tempUserInfo.getNickname()) && tempUserInfo.getPassword().equals(mapping.getTempPassword()) && (mapping.getUserRole().equals(UserRole.GUEST) || mapping.getUserRole().equals(UserRole.HOST)));
        if (!isHostOrGuest) {
            throw new UserException(ErrorCode.NOT_FOUND_USER_APPOINTMENT_MAPPING);
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

    public void validateIsUserParticipated(TempUserInfo tempUserInfo, List<UserAppointmentMapping> mappings) {
        boolean isParticipated = mappings.stream()
                .anyMatch(mapping -> UserRole.GUEST.equals(mapping.getUserRole()) && mapping.getNickname().equals(tempUserInfo.getNickname()));
        if (isParticipated) {
            throw new UserException(ErrorCode.APPOINTMENT_ALREADY_PARTICIPATE);
        }
    }

    public void validateIsUserParticipated(Long userId, List<UserAppointmentMapping> mappings) {
        boolean isParticipated = mappings.stream()
                .anyMatch(mapping -> UserRole.GUEST.equals(mapping.getUserRole()) && userId.equals(mapping.getUserSeq()));
        if (isParticipated) {
            throw new UserException(ErrorCode.APPOINTMENT_ALREADY_PARTICIPATE);
        }
    }

    public List<String> findAppointmentCodeListByAppointmentList(List<Appointment> appointmentList) {
        return appointmentList.stream()
                .map(Appointment::getAppointmentCode)
                .toList();
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
            throw new UserException(ErrorCode.RESOURCE_CONFLICT);
        }
    }

    public UserAppointmentMapping findGuestMapping(Long userId, List<UserAppointmentMapping> mappingList) {
        return mappingList.stream()
                .filter(mapping -> UserRole.GUEST.equals(mapping.getUserRole()) && userId.equals(mapping.getUserSeq()))
                .findFirst()
                .orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_USER_APPOINTMENT_MAPPING));
    }

    public UserAppointmentMapping findGuestMapping(TempUserInfo tempUserInfo, List<UserAppointmentMapping> mappingList) {
        return mappingList.stream()
                .filter(mapping -> UserRole.GUEST.equals(mapping.getUserRole()) && mapping.getNickname().equals(tempUserInfo.getNickname()) && tempUserInfo.getPassword().equals(mapping.getTempPassword()))
                .findFirst()
                .orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_USER_APPOINTMENT_MAPPING));
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
}
