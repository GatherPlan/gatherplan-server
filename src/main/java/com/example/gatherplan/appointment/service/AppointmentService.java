package com.example.gatherplan.appointment.service;

import com.example.gatherplan.appointment.dto.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface AppointmentService {

    List<RegionDto> searchRegion(RegionReqDto reqDto);

    List<KeywordPlaceRespDto> searchKeywordPlace(KeywordPlaceReqDto reqDto);

    List<DailyWeatherRespDto> searchDailyWeather(DailyWeatherReqDto reqDto);

    String registerAppointment(CreateAppointmentReqDto reqDto, String email);

    String registerTempAppointment(CreateTempAppointmentReqDto reqDto);

    CheckTempAppointmentRespDto checkTempAppointment(CheckTempAppointmentReqDto reqDto, HttpServletRequest request);
}
