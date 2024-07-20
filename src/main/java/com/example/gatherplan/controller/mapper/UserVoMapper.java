package com.example.gatherplan.controller.mapper;

import com.example.gatherplan.appointment.dto.CreateUserReqDto;
import com.example.gatherplan.controller.vo.user.CreateUserReq;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL
)
public interface UserVoMapper {
    CreateUserReqDto to(CreateUserReq req);
}
