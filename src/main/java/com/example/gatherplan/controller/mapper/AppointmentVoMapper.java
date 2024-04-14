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
    
    AppointmentInfoResp to(AppointmentInfoRespDto respDto);

    AppointmentParticipationInfoResp to(AppointmentParticipationInfoRespDto respDto);

    UpdateAppointmentReqDto to(UpdateAppointmentReq req);

    AppointmentResp to(AppointmentRespDto respDto);

    CreateAppointmentParticipationReqDto to(CreateAppointmentParticipationReq req);

    @Mapping(target = "isHost", source = "host")
    AppointmentWithHostByKeywordResp to(AppointmentWithHostByKeywordRespDto respDto);
}
