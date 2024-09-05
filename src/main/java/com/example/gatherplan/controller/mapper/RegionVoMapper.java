package com.example.gatherplan.controller.mapper;

import com.example.gatherplan.controller.vo.region.*;
import com.example.gatherplan.region.dto.*;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL
)
public interface RegionVoMapper {

    DistrictSearchReqDto to(DistrictSearchReq req);

    DistrictSearchResp to(DistrictSearchRespDto respDto);

    DailyWeatherResp to(DailyWeatherRespDto respDto);

    KeywordPlaceReqDto to(PlaceSearchReq req);

    PlaceSearchResp to(PlaceSearchRespDto respDto);

    FestivalResp to(FestivalRespDto respDto);
}
