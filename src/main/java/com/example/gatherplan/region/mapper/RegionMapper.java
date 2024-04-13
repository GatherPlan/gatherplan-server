package com.example.gatherplan.region.mapper;

import com.example.gatherplan.external.vo.DailyWeatherClientResp;
import com.example.gatherplan.external.vo.KeywordPlaceClientResp;
import com.example.gatherplan.region.dto.DailyWeatherRespDto;
import com.example.gatherplan.region.dto.KeywordPlaceRespDto;
import com.example.gatherplan.region.dto.RegionDto;
import com.example.gatherplan.region.repository.entity.Region;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL
)
public interface RegionMapper {
    RegionDto to(Region entity);

    KeywordPlaceRespDto to(KeywordPlaceClientResp.KeywordPlaceInfo clientResp);

    DailyWeatherRespDto to(DailyWeatherClientResp.DailyWeatherInfo clientResp);
}
