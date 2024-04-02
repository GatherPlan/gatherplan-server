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

    GetAppointmentListResp to(GetAppointmentListRespDto respDto);

    GetAppointmentSearchListReqDto to(GetAppointmentSearchListReq req);

    GetAppointmentSearchListResp to(GetAppointmentSearchListRespDto respDto);

    GetAppointmentInfoReqDto to(GetAppointmentInfoReq req);

    GetAppointmentInfoResp to(GetAppointmentInfoRespDto respDto);

    GetAppointmentParticipationInfoReqDto to(GetAppointmentParticipationInfoReq req);

    GetAppointmentParticipationInfoResp to(GetAppointmentParticipationInfoRespDto respDto);

    DeleteAppointmentReqDto to(DeleteAppointmentReq req);
}
