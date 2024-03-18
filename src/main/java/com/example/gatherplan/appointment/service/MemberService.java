package com.example.gatherplan.appointment.service;

import com.example.gatherplan.appointment.dto.AuthenticateEmailReqDto;
import com.example.gatherplan.appointment.dto.CreateMemberReqDto;

public interface MemberService {
    void authenticateEmail(AuthenticateEmailReqDto authenticateEmailReqDto);

    void joinMember(CreateMemberReqDto createMemberReqDto);

}
