package com.example.gatherplan.appointment.mapper;

import com.example.gatherplan.appointment.dto.AuthenticateEmailReqDto;
import com.example.gatherplan.appointment.dto.CreateMemberReqDto;
import com.example.gatherplan.appointment.dto.CreateTempMemberReqDto;
import com.example.gatherplan.controller.vo.appointment.AuthenticateEmailReq;
import com.example.gatherplan.controller.vo.appointment.CreateMemberReq;
import com.example.gatherplan.controller.vo.appointment.CreateTempMemberReq;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL
)
public interface MemberMapper {
    AuthenticateEmailReqDto to(AuthenticateEmailReq authenticateEmailReq);

    CreateMemberReqDto to(CreateMemberReq createMemberReq);

    CreateTempMemberReqDto to(CreateTempMemberReq createTempMemberReq);

}
