package com.example.gatherplan.region.service;

import com.example.gatherplan.region.dto.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RegionService {
    Page<DistrictSearchRespDto> searchRegion(DistrictSearchReqDto reqDto);

    Page<PlaceSearchRespDto> searchKeywordPlace(KeywordPlaceReqDto reqDto);

    List<DailyWeatherRespDto> searchDailyWeather(String addressName);

    List<FestivalRespDto> searchFestival();
}
