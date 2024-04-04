package com.example.gatherplan.region.service;

import com.example.gatherplan.appointment.dto.CSVRowDto;
import com.example.gatherplan.region.dto.*;

import java.util.List;

public interface RegionService {
    List<RegionDto> searchRegion(RegionReqDto reqDto);

    List<KeywordPlaceRespDto> searchKeywordPlace(KeywordPlaceReqDto reqDto);

    List<DailyWeatherRespDto> searchDailyWeather(DailyWeatherReqDto reqDto);

    void saveFromCSV(List<CSVRowDto> rows);
}
