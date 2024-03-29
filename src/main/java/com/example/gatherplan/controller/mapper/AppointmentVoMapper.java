package com.example.gatherplan.controller.mapper;

import com.example.gatherplan.appointment.dto.*;
import com.example.gatherplan.controller.vo.appointment.*;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL
)
public interface AppointmentVoMapper {
    CreateAppointmentReqDto to(CreateAppointmentReq req);

    CreateTempAppointmentReqDto to(CreateTempAppointmentReq req);

    RegionReqDto to(RegionReq req);

    DailyWeatherReqDto to(DailyWeatherReq req);

    KeywordPlaceReqDto to(KeywordPlaceReq req);

    RegionResp to(RegionDto regionDto);

    KeywordPlaceResp to(KeywordPlaceRespDto respDto);

    DailyWeatherResp to(DailyWeatherRespDto respDto);

}
