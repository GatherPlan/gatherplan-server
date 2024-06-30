package com.example.gatherplan.region.mapper;

import com.example.gatherplan.common.enums.LocationType;
import com.example.gatherplan.external.vo.DailyWeatherClientResp;
import com.example.gatherplan.external.vo.KeywordPlaceClientResp;
import com.example.gatherplan.region.dto.DailyWeatherRespDto;
import com.example.gatherplan.region.dto.DistrictSearchRespDto;
import com.example.gatherplan.region.dto.PlaceSearchRespDto;
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
    DistrictSearchRespDto to(Region entity, LocationType locationType);

    PlaceSearchRespDto to(KeywordPlaceClientResp.PlaceSearchInfo clientResp, LocationType locationType);

    DailyWeatherRespDto to(DailyWeatherClientResp.DailyWeatherInfo clientResp, String weatherImagePath);
}
