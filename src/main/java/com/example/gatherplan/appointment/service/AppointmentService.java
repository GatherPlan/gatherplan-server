package com.example.gatherplan.appointment.service;

import com.example.gatherplan.appointment.dto.*;
import org.json.JSONException;

import java.util.List;

public interface AppointmentService {

    List<searchDistrictRespDto> searchDisctrict(SearchDistrictReqDto searchDistrictReqDto);

    List<SearchPlaceRespDto> searchPlace(SearchPlaceReqDto searchPlaceReqDto) throws JSONException;

    void registerAppointment(CreateAppointmentReqDto createAppointmentReqDto, String email);

    void registerTempAppointment(CreateTempAppointmentReqDto createTempAppointmentReqDto);
}
