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
public interface TempAppointmentVoMapper {
    CreateTempAppointmentReqDto to(CreateTempAppointmentReq req);

    TempAppointmentInfoReqDto to(TempAppointmentInfoReq req);

    TempAppointmentInfoResp to(TempAppointmentInfoRespDto respDto);

    TempAppointmentParticipationInfoReqDto to(TempAppointmentParticipationInfoReq req);

    TempAppointmentParticipationInfoResp to(TempAppointmentParticipationInfoRespDto respDto);

    DeleteTempAppointmentReqDto to(DeleteTempAppointmentReq req);

    UpdateTempAppointmentReqDto to(UpdateTempAppointmentReq req);

    CreateTempAppointmentParticipationReqDto to(CreateTempAppointmentParticipationReq req);

    TempConfirmedAppointmentParticipantsReqDto to(TempConfirmedAppointmentParticipantsReq req);

    TempConfirmedAppointmentReqDto to(TempConfirmedAppointmentReq req);

    TempUserLoginReqDto to(TempUserLoginReq req);

    CreateTempUserReqDto to(CreateTempUserReq req);

    TempAppointmentParticipationStatusReqDto to(TempAppointmentParticipationStatusReq req);

    TempAppointmentInfoDetailReqDto to(TempAppointmentInfoDetailReq req);

    TempAppointmentInfoDetailResp to(TempAppointmentInfoDetailRespDto respDto);
}
