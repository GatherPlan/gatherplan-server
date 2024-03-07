package com.example.gatherplan.appointment.mapper;

import com.example.gatherplan.appointment.dto.LocalJoinEmailDto;
import com.example.gatherplan.appointment.dto.LocalJoinFormDto;
import com.example.gatherplan.controller.vo.member.LocalJoinEmailReq;
import com.example.gatherplan.controller.vo.member.LocalJoinFormReq;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "email", target = "email")
    LocalJoinEmailDto toLocalJoinEmailDto(LocalJoinEmailReq localJoinEmailReq);
    
    LocalJoinFormDto toLocalJoinFormDto(LocalJoinFormReq localJoinFormReq);

}
