package com.example.gatherplan.appointment.mapper;

import com.example.gatherplan.appointment.dto.*;
import com.example.gatherplan.appointment.enums.AppointmentState;
import com.example.gatherplan.appointment.repository.entity.Appointment;
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

    AppointmentWithHostRespDto toAppointmentWithHostRespDto(Appointment appointment, String hostName);

    AppointmentWithHostByKeywordRespDto toAppointmentWithHostByKeywordRespDto(Appointment appointment, String hostName);

    AppointmentParticipationInfoRespDto to(Appointment appointment
            , List<AppointmentParticipationInfoRespDto.UserParticipationInfo> userParticipationInfoList,
                                           List<TempAppointmentParticipationInfoRespDto.UserParticipationInfo> tempUserParticipationInfoList);
}
