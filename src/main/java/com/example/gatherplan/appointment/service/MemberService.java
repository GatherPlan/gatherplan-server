package com.example.gatherplan.appointment.service;

import com.example.gatherplan.appointment.dto.LocalJoinEmailDto;
import com.example.gatherplan.appointment.dto.LocalJoinFormDto;
import com.example.gatherplan.appointment.dto.LocalLoginFormDto;
import com.example.gatherplan.appointment.dto.TemporaryJoinFormDto;
import jakarta.servlet.http.HttpServletRequest;

public interface MemberService {
    void sendAuthCodeProcess(LocalJoinEmailDto localJoinEmailDto);

    void validateLocalJoinFormProcess(LocalJoinFormDto localJoinFormDto);

    void temporaryJoin(TemporaryJoinFormDto temporaryJoinFormDto);

    void localLoginProcess(LocalLoginFormDto localLoginFormDto, HttpServletRequest httpServletRequest);

    void loginCheck(HttpServletRequest httpServletRequest);
}
