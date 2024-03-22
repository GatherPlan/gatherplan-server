package com.example.gatherplan.controller.mapper;

import com.example.gatherplan.appointment.dto.*;
import com.example.gatherplan.controller.vo.appointment.CreateAppointmentReq;
import com.example.gatherplan.controller.vo.appointment.CreateTempAppointmentReq;
import com.example.gatherplan.controller.vo.appointment.SearchPlaceReq;
import com.example.gatherplan.controller.vo.appointment.SearchPlaceResp;
import com.example.gatherplan.controller.vo.common.AddressReq;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL
)
public interface AppointmentControllerMapper {
    CreateAppointmentReqDto to(CreateAppointmentReq request);

    CreateTempAppointmentReqDto to(CreateTempAppointmentReq request);

    AddressDto to(AddressReq request);

    SearchPlaceReqDto to(SearchPlaceReq searchPlaceReq);

    @Mapping(target = "regionList", source = "regionList")
    SearchPlaceResp to(SearchPlaceRespDto searchPlaceRespDto);

}
