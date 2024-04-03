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

    ParticipationStatusReqDto to(ParticipationStatusReq req);

    AppointmentListResp to(AppointmentListRespDto respDto);

    AppointmentSearchListReqDto to(AppointmentSearchListReq req);

    AppointmentSearchListResp to(AppointmentSearchListRespDto respDto);

    AppointmentInfoReqDto to(AppointmentInfoReq req);

    AppointmentInfoResp to(AppointmentInfoRespDto respDto);

    AppointmentParticipationInfoReqDto to(AppointmentParticipationInfoReq req);

    AppointmentParticipationInfoResp to(AppointmentParticipationInfoRespDto respDto);

    DeleteAppointmentReqDto to(DeleteAppointmentReq req);

    UpdateAppointmentReqDto to(UpdateAppointmentReq req);
}
