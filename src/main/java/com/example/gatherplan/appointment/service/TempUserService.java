package com.example.gatherplan.appointment.service;

import com.example.gatherplan.appointment.dto.TempUserCreateValidReqDto;
import com.example.gatherplan.appointment.dto.TempUserExistCheckReqDto;
import com.example.gatherplan.appointment.dto.TempUserHostCheckReqDto;
import com.example.gatherplan.appointment.dto.TempUserJoinCheckReqDto;

public interface TempUserService {

    boolean checkHost(TempUserHostCheckReqDto reqDto);

    boolean checkJoin(TempUserJoinCheckReqDto reqDto);

    boolean validJoin(TempUserCreateValidReqDto reqDto);

    boolean checkUser(TempUserExistCheckReqDto reqDto);
}
