package com.example.gatherplan.controller.mapper;

import com.example.gatherplan.appointment.dto.*;
import com.example.gatherplan.controller.vo.appointment.*;
import com.example.gatherplan.controller.vo.common.ListResponse;
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

    AppointmentSearchResp to(AppointmentSearchResp respDto);

    UpdateAppointmentReqDto to(UpdateAppointmentReq req);

    CreateAppointmentJoinReqDto to(CreateAppointmentJoinReq req);

    @Mapping(target = "isHost", source = "host")
    AppointmentSearchResp to(AppointmentSearchRespDto respDto);

    ConfirmAppointmentReqDto to(ConfirmAppointmentReq req);

    AppointmentPreviewResp to(AppointmentPreviewRespDto respDto);

    AppointmentCandidateInfoResp to(AppointmentCandidateInfoRespDto respDto);

    UpdateAppointmentJoinReqDto to(UpdateAppointmentJoinReq req);

    AppointmentMyParticipantResp to(AppointmentMyParticipantRespDto respDto);

    AppointmentCandidateInfoReqDto to(AppointmentCandidateInfoReq req);

    AppointmentSearchReqDto to(AppointmentSearchReq req);
}
