package com.example.gatherplan.appointment.mapper;

import com.example.gatherplan.appointment.dto.AppointmentParticipationInfoRespDto;
import com.example.gatherplan.appointment.dto.CreateTempAppointmentReqDto;
import com.example.gatherplan.appointment.dto.TempAppointmentInfoRespDto;
import com.example.gatherplan.appointment.dto.TempAppointmentParticipationInfoRespDto;
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
public interface TempAppointmentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "confirmedDateTime", ignore = true)
    Appointment to(CreateTempAppointmentReqDto req, AppointmentState appointmentState, String appointmentCode);

    TempAppointmentInfoRespDto to(Appointment appointment, String hostName);

    TempAppointmentParticipationInfoRespDto to(
            Appointment appointment,
            List<AppointmentParticipationInfoRespDto.UserParticipationInfo> userParticipationInfoList,
            List<TempAppointmentParticipationInfoRespDto.UserParticipationInfo> tempUserParticipationInfoList);
}
