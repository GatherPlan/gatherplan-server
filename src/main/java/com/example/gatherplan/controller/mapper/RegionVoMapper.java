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
public interface RegionVoMapper {
    RegionReqDto to(RegionReq req);

    RegionResp to(RegionDto regionDto);

    DailyWeatherReqDto to(DailyWeatherReq req);

    DailyWeatherResp to(DailyWeatherRespDto respDto);

    KeywordPlaceReqDto to(KeywordPlaceReq req);

    KeywordPlaceResp to(KeywordPlaceRespDto respDto);
}
