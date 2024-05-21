package com.example.gatherplan.appointment.mapper;

import com.example.gatherplan.appointment.dto.*;
import com.example.gatherplan.appointment.enums.AppointmentState;
import com.example.gatherplan.appointment.repository.entity.Appointment;
import com.example.gatherplan.appointment.repository.entity.UserAppointmentMapping;
import org.mapstruct.*;


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

    AppointmentInfoRespDto to(Appointment appointment, String hostName);

    AppointmentInfoDetailRespDto toAppointmentInfoDetailRespDto(Appointment appointment, String hostName,
                                                                boolean isParticipated, boolean isHost);

    AppointmentParticipationInfoRespDto to(UserAppointmentMapping userAppointmentMapping);

    AppointmentWithHostByKeywordRespDto toAppointmentWithHostByKeywordRespDto(Appointment appointment, String hostName,
                                                                              boolean isHost);

    AppointmentRespDto toAppointmentRespDto(Appointment appointment, String hostName);

}
