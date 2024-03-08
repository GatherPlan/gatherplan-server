package com.example.gatherplan.appointment.mapper;

import com.example.gatherplan.appointment.dto.LocalJoinEmailDto;
import com.example.gatherplan.appointment.dto.LocalJoinFormDto;
import com.example.gatherplan.appointment.dto.LocalLoginFormDto;
import com.example.gatherplan.appointment.dto.TemporaryJoinFormDto;
import com.example.gatherplan.controller.vo.member.LocalJoinEmailReq;
import com.example.gatherplan.controller.vo.member.LocalJoinFormReq;
import com.example.gatherplan.controller.vo.member.LocalLoginFormReq;
import com.example.gatherplan.controller.vo.member.TemporaryJoinFormReq;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "email", target = "email")
    LocalJoinEmailDto toLocalJoinEmailDto(LocalJoinEmailReq localJoinEmailReq);

    LocalJoinFormDto toLocalJoinFormDto(LocalJoinFormReq localJoinFormReq);

    TemporaryJoinFormDto toTemporaryJoinFormDto(TemporaryJoinFormReq temporaryJoinFormReq);

    LocalLoginFormDto toLocalLoginFormDto(LocalLoginFormReq localLoginFormReq);

}
