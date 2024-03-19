package com.example.gatherplan.appointment.mapper;

import com.example.gatherplan.appointment.dto.*;
import com.example.gatherplan.common.jwt.CustomUserDetails;
import com.example.gatherplan.controller.vo.appointment.req.CreateAppointmentReq;
import com.example.gatherplan.controller.vo.appointment.req.CreateTempAppointmentReq;
import com.example.gatherplan.controller.vo.appointment.resp.CreateAppointmentResp;
import com.example.gatherplan.controller.vo.appointment.resp.CreateTempAppointmentResp;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL
)
public interface AppointmentMapper {

    CreateAppointmentResp to(CreateAppointmentRespDto createAppointmentRespDto);


    CreateTempAppointmentReqDto to(CreateTempAppointmentReq createTempAppointmentReq);

    CreateTempAppointmentResp to(CreateTempAppointmentRespDto createTempAppointmentRespDto);

    CreateAppointmentReqDto to(CreateAppointmentReq createAppointmentReq);

    MemberInfoReqDto to(CustomUserDetails customUserDetails);
}
