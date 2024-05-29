package com.example.gatherplan.controller.mapper;

import com.example.gatherplan.appointment.dto.CreateTempUserReqDto;
import com.example.gatherplan.appointment.dto.TempCheckHostReqDto;
import com.example.gatherplan.controller.vo.tempappointment.CreateTempUserReq;
import com.example.gatherplan.controller.vo.tempappointment.TempCheckHostReq;
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

    TempCheckHostReqDto to(TempCheckHostReq req);

}
