package com.example.gatherplan.appointment.service;

import com.example.gatherplan.appointment.dto.*;

import java.util.List;

public interface RegionService {
    List<RegionDto> searchRegion(RegionReqDto reqDto);

    List<KeywordPlaceRespDto> searchKeywordPlace(KeywordPlaceReqDto reqDto);

    List<DailyWeatherRespDto> searchDailyWeather(DailyWeatherReqDto reqDto);
}
