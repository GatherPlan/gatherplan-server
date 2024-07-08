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
public interface AppointmentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "confirmedDateTime", ignore = true)
    Appointment to(CreateAppointmentReqDto reqDto, AppointmentState appointmentState, String appointmentCode);

    AppointmentPreviewRespDto to(Appointment appointment, String hostName);

    AppointmentSearchListRespDto toAppointmentSearchListRespDto(Appointment appointment, String hostName, boolean isHost);

    @Mapping(target = "participationInfo", source = "userAppointmentMapping")
    AppointmentParticipantsRespDto to(UserAppointmentMapping userAppointmentMapping);

    @Mapping(target = "isAvailable", source = "userAppointmentMapping.available")
    @Mapping(target = "userRole", source = "userRole")
    UserParticipationInfo to(UserAppointmentMapping userAppointmentMapping, UserRole userRole);

    AppointmentCandidateInfoRespDto to(AppointmentCandidateInfo candidateInfo);

    AppointmentInfoRespDto to(Appointment appointment, List<UserParticipationInfo> userParticipationInfoList, String hostName, Boolean isHost, Boolean isParticipated);

    @Mapping(target = "userRole", source = "userRole")
    ParticipationInfo toParticipationInfo(UserAppointmentMapping userAppointmentMapping, UserRole userRole);

    @Mapping(target = "participationInfo", source = "participationInfo")
    AppointmentParticipantsRespDto to(ParticipationInfo participationInfo);

    @Mapping(target = "participationInfo", source = "participationInfo")
    AppointmentMyParticipantRespDto toAppointmentParticipantRespDto(ParticipationInfo participationInfo);
}
