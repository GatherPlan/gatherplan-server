package com.example.gatherplan.controller.mapper;

import com.example.gatherplan.appointment.dto.*;
import com.example.gatherplan.controller.vo.tempappointment.*;
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

    TempAppointmentParticipantsReqDto to(TempAppointmentParticipantsReq req);

    TempAppointmentParticipantsResp to(TempAppointmentParticipantsRespDto respDto);

    DeleteTempAppointmentReqDto to(DeleteTempAppointmentReq req);

    UpdateTempAppointmentReqDto to(UpdateTempAppointmentReq req);

    CreateTempAppointmentJoinReqDto to(CreateTempAppointmentParticipationReq req);

    TempConfirmedAppointmentParticipantsReqDto to(TempConfirmedAppointmentParticipantsReq req);

    TempConfirmAppointmentReqDto to(TempConfirmAppointmentReq req);

    TempCheckHostReqDto to(TempCheckHostReq req);

    CreateTempUserReqDto to(CreateTempUserReq req);

    TempCheckJoinReqDto to(TempCheckJoinReq req);

    TempAppointmentInfoReqDto to(TempAppointmentInfoReq req);

    @Mapping(target = "isParticipated", source = "participated")
    @Mapping(target = "isHost", source = "host")
    TempAppointmentInfoResp to(TempAppointmentInfoRespDto respDto);

    DeleteTempAppointmentJoinReqDto to(DeleteTempAppointmentJoinReq req);

    UpdateTempAppointmentJoinReqDto to(UpdateTempAppointmentJoinReq req);

    TempAppointmentCandidateInfoReqDto to(TempAppointmentCandidateDatesReq req);

    TempAppointmentCandidateInfoResp to(TempAppointmentCandidateInfoRespDto respDto);
}
