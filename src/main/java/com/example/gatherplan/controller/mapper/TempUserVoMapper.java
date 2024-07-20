package com.example.gatherplan.controller.mapper;

import com.example.gatherplan.appointment.dto.CreateTempUserReqDto;
import com.example.gatherplan.appointment.dto.TempCheckHostReqDto;
import com.example.gatherplan.appointment.dto.TempCheckJoinReqDto;
import com.example.gatherplan.controller.vo.tempuser.CreateTempUserReq;
import com.example.gatherplan.controller.vo.tempuser.TempCheckHostReq;
import com.example.gatherplan.controller.vo.tempuser.TempCheckJoinReq;
import com.example.gatherplan.controller.vo.tempuser.TempCheckUserReq;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL
)
public interface TempUserVoMapper {

    TempCheckJoinReqDto to(TempCheckUserReq req);

    TempCheckJoinReqDto to(TempCheckJoinReq req);

    CreateTempUserReqDto to(CreateTempUserReq req);

    TempCheckHostReqDto to(TempCheckHostReq req);

}
