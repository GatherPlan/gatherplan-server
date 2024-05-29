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

    @Mapping(target = "isHost", source = "host")
    @Mapping(target = "isParticipated", source = "participated")
    AppointmentInfoResp to(AppointmentInfoRespDto respDto);

    AppointmentParticipantsResp to(AppointmentParticipantsRespDto respDto);

    UpdateAppointmentReqDto to(UpdateAppointmentReq req);

    AppointmentResp to(AppointmentRespDto respDto);

    CreateAppointmentJoinReqDto to(CreateAppointmentJoinReq req);

    @Mapping(target = "isHost", source = "host")
    AppointmentSearchListResp to(AppointmentSearchListRespDto respDto);

    ConfirmedAppointmentReqDto to(ConfirmedAppointmentReq req);

    AppointmentPreviewResp to(AppointmentPreviewRespDto respDto);

    AppointmentCandidateDatesResp to(AppointmentCandidateDatesRespDto respDto);

    UpdateAppointmentJoinReqDto to(UpdateAppointmentJoinReq req);
}
