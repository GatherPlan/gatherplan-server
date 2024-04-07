package com.example.gatherplan.controller.mapper;

import com.example.gatherplan.controller.vo.region.DailyWeatherResp;
import com.example.gatherplan.controller.vo.region.KeywordPlaceReq;
import com.example.gatherplan.controller.vo.region.KeywordPlaceResp;
import com.example.gatherplan.controller.vo.region.RegionResp;
import com.example.gatherplan.region.dto.DailyWeatherRespDto;
import com.example.gatherplan.region.dto.KeywordPlaceReqDto;
import com.example.gatherplan.region.dto.KeywordPlaceRespDto;
import com.example.gatherplan.region.dto.RegionDto;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL
)
public interface RegionVoMapper {

    RegionResp to(RegionDto regionDto);

    DailyWeatherResp to(DailyWeatherRespDto respDto);

    KeywordPlaceReqDto to(KeywordPlaceReq req);

    KeywordPlaceResp to(KeywordPlaceRespDto respDto);
}
