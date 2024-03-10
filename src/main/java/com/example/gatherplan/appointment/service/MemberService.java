package com.example.gatherplan.appointment.service;

import com.example.gatherplan.appointment.dto.AuthenticateEmailReqDto;
import com.example.gatherplan.appointment.dto.CreateMemberReqDto;
import com.example.gatherplan.appointment.dto.LoginMemberReqDto;
import com.example.gatherplan.appointment.dto.CreateTemporaryMemberReqDto;
import jakarta.servlet.http.HttpServletRequest;

public interface MemberService {
    void authenticateEmail(AuthenticateEmailReqDto authenticateEmailReqDto);

    void joinMember(CreateMemberReqDto createMemberReqDto);

    void joinTemporaryMember(CreateTemporaryMemberReqDto createTemporaryMemberReqDto);

    void login(LoginMemberReqDto loginMemberReqDto, HttpServletRequest httpServletRequest);

    void loginCheck(HttpServletRequest httpServletRequest);
}
