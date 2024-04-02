package com.example.gatherplan.controller.mapper;

import com.example.gatherplan.appointment.dto.CheckAppointmentReqDto;
import com.example.gatherplan.appointment.dto.CreateAppointmentReqDto;
import com.example.gatherplan.appointment.dto.GetAppointmentListRespDto;
import com.example.gatherplan.controller.vo.appointment.CheckAppointmentReq;
import com.example.gatherplan.controller.vo.appointment.CreateAppointmentReq;
import com.example.gatherplan.controller.vo.appointment.GetAppointmentListResp;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL
)
public interface AppointmentVoMapper {
    CreateAppointmentReqDto to(CreateAppointmentReq req);

    CheckAppointmentReqDto to(CheckAppointmentReq req);

    GetAppointmentListResp to(GetAppointmentListRespDto reqDto);
}
