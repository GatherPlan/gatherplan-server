package com.example.gatherplan.controller.mapper;

import com.example.gatherplan.appointment.dto.CreateAppointmentReqDto;
import com.example.gatherplan.appointment.dto.CreateTempAppointmentReqDto;
import com.example.gatherplan.appointment.dto.MemberInfoReqDto;
import com.example.gatherplan.common.jwt.CustomUserDetails;
import com.example.gatherplan.controller.vo.appointment.CreateAppointmentReq;
import com.example.gatherplan.controller.vo.appointment.CreateTempAppointmentReq;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL
)
public interface AppointmentControllerMapper {

    CreateTempAppointmentReqDto to(CreateTempAppointmentReq request);

    CreateAppointmentReqDto to(CreateAppointmentReq request);

    MemberInfoReqDto to(CustomUserDetails request);
}
