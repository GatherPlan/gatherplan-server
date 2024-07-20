package com.example.gatherplan.controller.service;

import com.example.gatherplan.appointment.dto.CreateTempUserReqDto;
import com.example.gatherplan.appointment.dto.TempCheckHostReqDto;
import com.example.gatherplan.appointment.dto.TempCheckJoinReqDto;
import com.example.gatherplan.appointment.service.TempUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TempUserFacadeService {
    public final TempUserService tempUserService;

    public boolean checkHost(TempCheckHostReqDto reqDto) {
        return tempUserService.checkHost(reqDto);
    }

    public boolean checkJoin(TempCheckJoinReqDto reqDto) {
        return tempUserService.checkJoin(reqDto);
    }

    public boolean checkUser(TempCheckJoinReqDto reqDto) {
        return tempUserService.checkUser(reqDto);
    }

    public boolean validJoin(CreateTempUserReqDto reqDto) {
        return tempUserService.validJoin(reqDto);
    }
}
