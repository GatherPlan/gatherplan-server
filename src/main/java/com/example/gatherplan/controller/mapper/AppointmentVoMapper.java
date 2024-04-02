package com.example.gatherplan.controller.mapper;

import com.example.gatherplan.appointment.dto.*;
import com.example.gatherplan.controller.vo.appointment.*;
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

    GetAppointmentSearchListReqDto to(GetAppointmentSearchListReq reqDto);

    GetAppointmentSearchListResp to(GetAppointmentSearchListRespDto respDto);

    GetAppointmentInfoReqDto to(GetAppointmentInfoReq getAppointmentInfoReq);

    GetAppointmentInfoResp to(GetAppointmentInfoRespDto getAppointmentInfoRespDto);
}
