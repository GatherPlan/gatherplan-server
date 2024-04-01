package com.example.gatherplan.appointment.mapper;

import com.example.gatherplan.api.vo.DailyWeatherClientResp;
import com.example.gatherplan.api.vo.KeywordPlaceClientResp;
import com.example.gatherplan.appointment.dto.DailyWeatherRespDto;
import com.example.gatherplan.appointment.dto.KeywordPlaceRespDto;
import com.example.gatherplan.appointment.dto.RegionDto;
import com.example.gatherplan.appointment.repository.entity.Region;
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
