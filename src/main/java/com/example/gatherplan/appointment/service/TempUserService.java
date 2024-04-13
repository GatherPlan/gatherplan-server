package com.example.gatherplan.appointment.service;

import com.example.gatherplan.appointment.dto.CreateTempUserReqDto;

public interface TempUserService {

    boolean validJoinTempUser(CreateTempUserReqDto reqDto);

}
