package com.example.gatherplan.appointment.mapper;

import com.example.gatherplan.appointment.dto.*;
import com.example.gatherplan.appointment.enums.AppointmentState;
import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.repository.entity.Appointment;
import com.example.gatherplan.appointment.repository.entity.UserAppointmentMapping;
import com.example.gatherplan.appointment.utils.unit.AppointmentCandidateInfo;
import com.example.gatherplan.common.unit.ParticipationInfo;
import com.example.gatherplan.common.unit.UserParticipationInfo;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL
)
public interface TempAppointmentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "confirmedDateTime", ignore = true)
    Appointment to(CreateTempAppointmentReqDto req, AppointmentState appointmentState, String appointmentCode);

    @Mapping(target = "participationInfo", source = "userAppointmentMapping")
    TempAppointmentParticipantsRespDto to(UserAppointmentMapping userAppointmentMapping);

    TempAppointmentCandidateInfoRespDto to(AppointmentCandidateInfo candidateInfo);

    @Mapping(target = "participationInfo", source = "userAppointmentMapping")
    TempAppointmentMyParticipantRespDto toTempAppointmentParticipantRespDto(ParticipationInfo userAppointmentMapping);

    @Mapping(target = "isAvailable", source = "userAppointmentMapping.available")
    @Mapping(target = "userRole", source = "userRole")
    UserParticipationInfo to(UserAppointmentMapping userAppointmentMapping, UserRole userRole);

    @Mapping(target = "userRole", source = "userRole")
    ParticipationInfo toParticipationInfo(UserAppointmentMapping userAppointmentMapping, UserRole userRole);

    TempAppointmentInfoRespDto to(Appointment appointment, List<UserParticipationInfo> userParticipationInfoList, String hostName, boolean isHost, boolean isParticipated);

    @Mapping(target = "participationInfo", source = "participationInfo")
    TempAppointmentParticipantsRespDto to(ParticipationInfo participationInfo);

    @Mapping(target = "participationInfo", source = "participationInfo")
    TempAppointmentMyParticipantRespDto toAppointmentParticipantRespDto(ParticipationInfo participationInfo);
}
