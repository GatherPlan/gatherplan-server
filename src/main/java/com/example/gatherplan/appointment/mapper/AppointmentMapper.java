package com.example.gatherplan.appointment.mapper;

import com.example.gatherplan.appointment.dto.AppointmentFormDto;
import com.example.gatherplan.controller.vo.appointment.AppointmentFormReq;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {
    AppointmentFormDto toAppointmentFormDto(AppointmentFormReq appointmentFormReq);

}
