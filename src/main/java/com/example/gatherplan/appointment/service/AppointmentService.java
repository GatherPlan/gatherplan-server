package com.example.gatherplan.appointment.service;

import com.example.gatherplan.appointment.dto.*;

import java.util.List;

public interface AppointmentService {

    List<RegionDto> searchRegion(RegionReqDto regionReqDto);

    List<KeywordPlaceRespDto> searchKeywordPlace(KeywordPlaceReqDto keywordPlaceReqDto);

    List<DailyWeatherRespDto> searchDailyWeather(DailyWeatherReqDto dailyWeatherReqDto);

    void registerAppointment(CreateAppointmentReqDto createAppointmentReqDto, String email);

    void registerTempAppointment(CreateTempAppointmentReqDto createTempAppointmentReqDto);

}
