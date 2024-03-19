package com.example.gatherplan.controller.mapper;

import com.example.gatherplan.appointment.dto.AuthenticateEmailReqDto;
import com.example.gatherplan.appointment.dto.CreateMemberReqDto;
import com.example.gatherplan.controller.vo.appointment.AuthenticateEmailReq;
import com.example.gatherplan.controller.vo.appointment.CreateMemberReq;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL
)
public interface MemberControllerMapper {
    AuthenticateEmailReqDto to(AuthenticateEmailReq request);

    CreateMemberReqDto to(CreateMemberReq request);

}
