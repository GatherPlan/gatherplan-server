package com.example.gatherplan.appointment.service;

import com.example.gatherplan.appointment.dto.*;
import org.json.JSONException;

import java.util.List;

public interface AppointmentService {

    List<SearchDistrictRespDto> searchDisctrict(SearchDistrictReqDto searchDistrictReqDto);

    List<SearchPlaceRespDto> searchPlace(SearchPlaceReqDto searchPlaceReqDto) throws JSONException;

    List<SearchWeatherRespDto> searchWhether(SearchWeatherReqDto searchWeatherReqDto);

    void registerAppointment(CreateAppointmentReqDto createAppointmentReqDto, String email);

    void registerTempAppointment(CreateTempAppointmentReqDto createTempAppointmentReqDto);

}
