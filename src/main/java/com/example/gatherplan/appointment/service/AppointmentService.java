package com.example.gatherplan.appointment.service;

import com.example.gatherplan.appointment.dto.*;
import org.json.JSONException;

public interface AppointmentService {

    SearchPlaceRespDto searchPlace(SearchPlaceReqDto searchPlaceReqDto);

    SearchPlaceDetailRespDto searchPlaceDetail(SearchPlaceDetailReqDto searchPlaceDetailReqDto) throws JSONException;

    void registerAppointment(CreateAppointmentReqDto createAppointmentReqDto, String email);

    void registerTempAppointment(CreateTempAppointmentReqDto createTempAppointmentReqDto);
}
