package com.example.gatherplan.appointment.mapper;

import com.example.gatherplan.appointment.dto.AuthenticateEmailReqDto;
import com.example.gatherplan.appointment.dto.CreateMemberReqDto;
import com.example.gatherplan.appointment.dto.LoginMemberReqDto;
import com.example.gatherplan.appointment.dto.CreateTemporaryMemberReqDto;
import com.example.gatherplan.controller.vo.appointment.AuthenticateEmailReq;
import com.example.gatherplan.controller.vo.appointment.CreateMemberReq;
import com.example.gatherplan.controller.vo.appointment.LoginMemberReq;
import com.example.gatherplan.controller.vo.appointment.CreateTemporaryMemberReq;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    AuthenticateEmailReqDto to(AuthenticateEmailReq authenticateEmailReq);

    CreateMemberReqDto to(CreateMemberReq createMemberReq);

    CreateTemporaryMemberReqDto to(CreateTemporaryMemberReq createTemporaryMemberReq);

    LoginMemberReqDto to(LoginMemberReq loginMemberReq);

}
