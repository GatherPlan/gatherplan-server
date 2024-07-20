package com.example.gatherplan.controller.mapper;

import com.example.gatherplan.appointment.dto.TempUserCreateValidReqDto;
import com.example.gatherplan.appointment.dto.TempUserExistCheckReqDto;
import com.example.gatherplan.appointment.dto.TempUserHostCheckReqDto;
import com.example.gatherplan.appointment.dto.TempUserJoinCheckReqDto;
import com.example.gatherplan.controller.vo.tempuser.TempUserCheckHostReq;
import com.example.gatherplan.controller.vo.tempuser.TempUserCreateValidReq;
import com.example.gatherplan.controller.vo.tempuser.TempUserExistCheckReq;
import com.example.gatherplan.controller.vo.tempuser.TempUserJoinCheckReq;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL
)
public interface TempUserVoMapper {

    TempUserExistCheckReqDto to(TempUserExistCheckReq req);

    TempUserJoinCheckReqDto to(TempUserJoinCheckReq req);

    TempUserCreateValidReqDto to(TempUserCreateValidReq req);

    TempUserHostCheckReqDto to(TempUserCheckHostReq req);

}
