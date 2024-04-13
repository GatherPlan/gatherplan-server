package com.example.gatherplan.appointment.mapper;

import com.example.gatherplan.appointment.dto.*;
import com.example.gatherplan.appointment.enums.AppointmentState;
import com.example.gatherplan.appointment.repository.entity.Appointment;
import com.example.gatherplan.common.unit.ParticipationInfo;
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

    AppointmentInfoRespDto toAppointmentInfoRespDto(Appointment appointment, String hostName);

    AppointmentParticipationInfoRespDto to(Appointment appointment,
                                           List<ParticipationInfo> userParticipationInfoList,
                                           List<ParticipationInfo> tempUserParticipationInfoList);

    AppointmentWithHostRespDto toAppointmentWithHostRespDto(Appointment appointment, String hostName, boolean isHost);

    AppointmentWithHostByKeywordRespDto toAppointmentWithHostByKeywordRespDto(Appointment appointment, String hostName,
                                                                              boolean isHost);

    AppointmentRespDto toAppointmentRespDto(Appointment appointment, String hostName);

}
