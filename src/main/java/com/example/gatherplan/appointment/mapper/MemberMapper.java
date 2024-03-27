package com.example.gatherplan.appointment.mapper;

import com.example.gatherplan.appointment.dto.CreateMemberReqDto;
import com.example.gatherplan.appointment.enums.UserAuthType;
import com.example.gatherplan.appointment.repository.entity.Member;
import com.example.gatherplan.common.jwt.RoleType;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL
)
public interface MemberMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "nickname", source = "reqDto.nickname")
    @Mapping(target = "password", source = "encodedPassword")
    @Mapping(target = "userAuthType", source = "userAuthType")
    @Mapping(target = "roleType", source = "role")
    Member to(CreateMemberReqDto reqDto, String encodedPassword, UserAuthType userAuthType, RoleType role);
}
