package com.example.gatherplan.controller.mapper;

import com.example.gatherplan.appointment.dto.CreateTempAppointmentReqDto;
import com.example.gatherplan.appointment.dto.TempAppointmentInfoReqDto;
import com.example.gatherplan.appointment.dto.TempAppointmentInfoRespDto;
import com.example.gatherplan.controller.vo.appointment.CreateTempAppointmentReq;
import com.example.gatherplan.controller.vo.appointment.TempAppointmentInfoReq;
import com.example.gatherplan.controller.vo.appointment.TempAppointmentInfoResp;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL
)
public interface TempAppointmentVoMapper {
    CreateTempAppointmentReqDto to(CreateTempAppointmentReq req);

    TempAppointmentInfoReqDto to(TempAppointmentInfoReq req);

    TempAppointmentInfoResp to(TempAppointmentInfoRespDto respDto);

}
