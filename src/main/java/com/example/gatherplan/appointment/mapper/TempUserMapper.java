package com.example.gatherplan.appointment.mapper;

import com.example.gatherplan.appointment.repository.entity.TempUser;
import com.example.gatherplan.common.unit.TempUserInfo;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL
)
public interface TempUserMapper {

    @Mapping(target = "id", ignore = true)
    TempUser to(TempUserInfo tempUserInfo);
}
