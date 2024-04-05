package com.example.gatherplan.appointment.service;

import com.example.gatherplan.appointment.dto.CreateTempAppointmentReqDto;
import com.example.gatherplan.appointment.dto.TempAppointmentInfoReqDto;
import com.example.gatherplan.appointment.dto.TempAppointmentInfoRespDto;

public interface TempAppointmentService {
    String registerTempAppointment(CreateTempAppointmentReqDto reqDto);

    TempAppointmentInfoRespDto retrieveAppointmentInfo(TempAppointmentInfoReqDto reqDto);
}
