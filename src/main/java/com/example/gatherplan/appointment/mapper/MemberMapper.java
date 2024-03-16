package com.example.gatherplan.appointment.mapper;

import com.example.gatherplan.appointment.dto.AuthenticateEmailReqDto;
import com.example.gatherplan.appointment.dto.CreateMemberReqDto;
import com.example.gatherplan.appointment.dto.CreateTempMemberReqDto;
import com.example.gatherplan.appointment.dto.LoginMemberReqDto;
import com.example.gatherplan.controller.vo.appointment.AuthenticateEmailReq;
import com.example.gatherplan.controller.vo.appointment.CreateMemberReq;
import com.example.gatherplan.controller.vo.appointment.CreateTempMemberReq;
import com.example.gatherplan.controller.vo.appointment.LoginMemberReq;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    AuthenticateEmailReqDto to(AuthenticateEmailReq authenticateEmailReq);

    CreateMemberReqDto to(CreateMemberReq createMemberReq);

    CreateTempMemberReqDto to(CreateTempMemberReq createTempMemberReq);

    LoginMemberReqDto to(LoginMemberReq loginMemberReq);

}
