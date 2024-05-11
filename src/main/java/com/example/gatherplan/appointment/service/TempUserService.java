package com.example.gatherplan.appointment.service;

import com.example.gatherplan.appointment.dto.CreateTempUserReqDto;
import com.example.gatherplan.appointment.dto.TempUserLoginReqDto;

public interface TempUserService {

    boolean validJoinTempUser(CreateTempUserReqDto reqDto);

    void login(TempUserLoginReqDto reqDto);
}
