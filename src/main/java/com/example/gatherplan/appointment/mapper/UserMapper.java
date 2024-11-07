package com.example.gatherplan.appointment.mapper;

import com.example.gatherplan.appointment.dto.CreateUserReqDto;
import com.example.gatherplan.appointment.dto.UserInfoRespDto;
import com.example.gatherplan.appointment.enums.UserAuthType;
import com.example.gatherplan.appointment.repository.entity.User;
import com.example.gatherplan.common.config.jwt.RoleType;
import com.example.gatherplan.common.config.jwt.UserInfo;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL
)
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", source = "encodedPassword")
    @Mapping(target = "name", source = "reqDto.name")
    User to(CreateUserReqDto reqDto, String encodedPassword, UserAuthType userAuthType, RoleType roleType);
}
