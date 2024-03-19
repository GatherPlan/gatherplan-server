package com.example.gatherplan.appointment.mapper;

import com.example.gatherplan.appointment.dto.CreateAppointmentReqDto;
import com.example.gatherplan.appointment.dto.CreateTempAppointmentReqDto;
import com.example.gatherplan.appointment.dto.MemberInfoReqDto;
import com.example.gatherplan.common.jwt.CustomUserDetails;
import com.example.gatherplan.controller.vo.appointment.req.CreateAppointmentReq;
import com.example.gatherplan.controller.vo.appointment.req.CreateTempAppointmentReq;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL
)
public interface AppointmentMapper {

    CreateTempAppointmentReqDto to(CreateTempAppointmentReq createTempAppointmentReq);
    
    CreateAppointmentReqDto to(CreateAppointmentReq createAppointmentReq);

    MemberInfoReqDto to(CustomUserDetails customUserDetails);
}
