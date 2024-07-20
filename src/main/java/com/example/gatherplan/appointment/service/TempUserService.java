package com.example.gatherplan.appointment.service;

import com.example.gatherplan.appointment.dto.CreateTempUserReqDto;
import com.example.gatherplan.appointment.dto.TempCheckHostReqDto;
import com.example.gatherplan.appointment.dto.TempCheckJoinReqDto;

public interface TempUserService {

    boolean checkHost(TempCheckHostReqDto reqDto);

    boolean checkJoin(TempCheckJoinReqDto reqDto);

    boolean validJoin(CreateTempUserReqDto reqDto);

    boolean checkUser(TempCheckJoinReqDto reqDto);
}
