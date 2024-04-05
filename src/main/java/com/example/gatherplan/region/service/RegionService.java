package com.example.gatherplan.region.service;

import com.example.gatherplan.appointment.dto.CSVRowDto;
import com.example.gatherplan.region.dto.DailyWeatherRespDto;
import com.example.gatherplan.region.dto.KeywordPlaceReqDto;
import com.example.gatherplan.region.dto.KeywordPlaceRespDto;
import com.example.gatherplan.region.dto.RegionDto;

import java.util.List;

public interface RegionService {
    List<RegionDto> searchRegion(String keyword);

    List<KeywordPlaceRespDto> searchKeywordPlace(KeywordPlaceReqDto reqDto);

    List<DailyWeatherRespDto> searchDailyWeather(String addressName);

    void saveFromCSV(List<CSVRowDto> rows);
}
