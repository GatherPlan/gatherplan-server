package com.example.gatherplan.appointment.mapper;

import com.example.gatherplan.appointment.dto.CreateAppointmentReqDto;
import com.example.gatherplan.appointment.dto.GetAppointmentListRespDto;
import com.example.gatherplan.appointment.dto.GetAppointmentParticipationInfoRespDto;
import com.example.gatherplan.appointment.dto.GetAppointmentSearchListRespDto;
import com.example.gatherplan.appointment.enums.AppointmentState;
import com.example.gatherplan.appointment.enums.TimeType;
import com.example.gatherplan.appointment.repository.entity.Appointment;
import com.example.gatherplan.appointment.repository.entity.User;
import com.example.gatherplan.appointment.repository.entity.embedded.SelectedDateTime;
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
    Appointment to(CreateAppointmentReqDto req, AppointmentState appointmentState, String appointmentCode);

    GetAppointmentListRespDto to(Appointment entity, String hostName);

    GetAppointmentSearchListRespDto toDto(Appointment entity, String hostName);

    GetAppointmentParticipationInfoRespDto to(
            User user, List<SelectedDateTime> selectedDateTime, List<LocalDate> candidateDateList,
            List<TimeType> candidateTimeTypeList);
}
