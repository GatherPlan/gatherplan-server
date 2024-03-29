package com.example.gatherplan.appointment.mapper;

import com.example.gatherplan.api.kakaolocal.KeywordPlaceClientResp;
import com.example.gatherplan.api.weathernews.DailyWeatherClientResp;
import com.example.gatherplan.appointment.dto.*;
import com.example.gatherplan.appointment.enums.AppointmentState;
import com.example.gatherplan.appointment.repository.entity.Appointment;
import com.example.gatherplan.appointment.repository.entity.Region;
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
    Appointment to(CreateAppointmentReqDto req, AppointmentState appointmentState, String appointmentCode);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "confirmedDateTime", ignore = true)
    Appointment to(CreateTempAppointmentReqDto req, AppointmentState appointmentState, String appointmentCode);

    RegionDto to(Region entity);

    KeywordPlaceRespDto to(KeywordPlaceClientResp.KeywordPlaceInfo clientResp);

    DailyWeatherRespDto to(DailyWeatherClientResp.DailyWeatherInfo clientResp);
}
