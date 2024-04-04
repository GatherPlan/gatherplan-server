package com.example.gatherplan.appointment.mapper;

import com.example.gatherplan.appointment.dto.*;
import com.example.gatherplan.appointment.enums.AppointmentState;
import com.example.gatherplan.appointment.enums.TimeType;
import com.example.gatherplan.appointment.repository.entity.Appointment;
import org.mapstruct.*;

import java.time.LocalDate;
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

    AppointmentListRespDto toGetAppointmentListRespDto(Appointment entity, String hostName);

    AppointmentSearchListRespDto toGetAppointmentSearchListRespDto(Appointment entity, String hostName);

    AppointmentListRespDto to(UserAppointmentInfoDto dto);

    AppointmentSearchListRespDto to(UserAppointmentKeywordInfoDto dto);

    AppointmentInfoRespDto to(AppointmentInfoDto dto);

    AppointmentParticipationInfoRespDto.UserParticipationInfo to(AppointmentParticipationInfoDto.UserParticipationInfo dto);

    AppointmentParticipationInfoRespDto to(List<AppointmentParticipationInfoRespDto.UserParticipationInfo> userParticipationInfo,
                                           List<TimeType> candidateTimeTypeList, List<LocalDate> candidateDateList);

}
