package com.example.gatherplan.appointment.service;

import com.example.gatherplan.appointment.dto.LocalJoinFormDto;

public interface MemberService {
    void sendAuthCodeProcess(String email);

    void validateLocalJoinFormProcess(LocalJoinFormDto localJoinFormDto);
}
