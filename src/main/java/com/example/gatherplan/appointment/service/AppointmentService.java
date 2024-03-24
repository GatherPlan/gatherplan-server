package com.example.gatherplan.appointment.service;

import com.example.gatherplan.appointment.dto.*;
import org.json.JSONException;

public interface AppointmentService {

    SearchPlaceRespDto searchDisctrict(SearchPlaceReqDto searchPlaceReqDto);

    SearchPlaceDetailRespDto searchPlace(SearchPlaceDetailReqDto searchPlaceDetailReqDto) throws JSONException;

    void registerAppointment(CreateAppointmentReqDto createAppointmentReqDto, String email);

    void registerTempAppointment(CreateTempAppointmentReqDto createTempAppointmentReqDto);
}
