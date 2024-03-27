package com.example.gatherplan.controller.mapper;

import com.example.gatherplan.appointment.dto.*;
import com.example.gatherplan.controller.vo.appointment.*;
import com.example.gatherplan.appointment.dto.CreateAppointmentReqDto;
import com.example.gatherplan.appointment.dto.CreateTempAppointmentReqDto;
import com.example.gatherplan.controller.vo.appointment.CreateAppointmentReq;
import com.example.gatherplan.controller.vo.appointment.CreateTempAppointmentReq;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL
)
public interface AppointmentVoMapper {
    CreateAppointmentReqDto to(CreateAppointmentReq request);

    CreateTempAppointmentReqDto to(CreateTempAppointmentReq request);

    RegionReqDto to(RegionReq regionReq);

    KeywordPlaceReqDto to(KeywordPlaceReq keywordPlaceReq);

    DailyWeatherReqDto to(DailyWhetherReq dailyWhetherReq);

    RegionResp to(RegionDto regionDto);

    KeywordPlaceResp to(KeywordPlaceRespDto keywordPlaceRespDto);

    DailyWeatherResp to(DailyWeatherRespDto dailyWeatherRespDto);

}
