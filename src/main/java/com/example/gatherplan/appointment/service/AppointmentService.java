package com.example.gatherplan.appointment.service;

import com.example.gatherplan.appointment.dto.CreateAppointmentReqDto;
import com.example.gatherplan.appointment.dto.CreateTempAppointmentReqDto;
import com.example.gatherplan.appointment.dto.SearchPlaceReqDto;
import com.example.gatherplan.appointment.dto.SearchPlaceRespDto;

public interface AppointmentService {

    SearchPlaceRespDto searchPlace(SearchPlaceReqDto searchPlaceReqDto);

    void registerAppointment(CreateAppointmentReqDto createAppointmentReqDto, String email);

    void registerTempAppointment(CreateTempAppointmentReqDto createTempAppointmentReqDto);
}
