package com.example.gatherplan.appointment.service;

import com.example.gatherplan.appointment.dto.LocalJoinEmailDto;
import com.example.gatherplan.appointment.dto.LocalJoinFormDto;
import com.example.gatherplan.appointment.dto.TemporaryJoinFormDto;

public interface MemberService {
    void sendAuthCodeProcess(LocalJoinEmailDto localJoinEmailDto);

    void validateLocalJoinFormProcess(LocalJoinFormDto localJoinFormDto);

    void temporaryJoin(TemporaryJoinFormDto temporaryJoinFormDto);
}
