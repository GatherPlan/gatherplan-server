package com.example.gatherplan.controller.mapper;

import com.example.gatherplan.appointment.dto.CreateTempUserReqDto;
import com.example.gatherplan.controller.vo.appointment.CreateTempUserReq;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL
)
public interface TempUserVoMapper {

    CreateTempUserReqDto to(CreateTempUserReq req);

}
