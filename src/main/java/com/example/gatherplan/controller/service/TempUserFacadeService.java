package com.example.gatherplan.controller.service;

import com.example.gatherplan.appointment.dto.TempUserCreateValidReqDto;
import com.example.gatherplan.appointment.dto.TempUserExistCheckReqDto;
import com.example.gatherplan.appointment.dto.TempUserHostCheckReqDto;
import com.example.gatherplan.appointment.dto.TempUserJoinCheckReqDto;
import com.example.gatherplan.appointment.service.TempUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TempUserFacadeService {
    public final TempUserService tempUserService;

    public boolean checkHost(TempUserHostCheckReqDto reqDto) {
        return tempUserService.checkHost(reqDto);
    }

    public boolean checkJoin(TempUserJoinCheckReqDto reqDto) {
        return tempUserService.checkJoin(reqDto);
    }

    public boolean checkUser(TempUserExistCheckReqDto reqDto) {
        return tempUserService.checkUser(reqDto);
    }

    public boolean validJoin(TempUserCreateValidReqDto reqDto) {
        return tempUserService.validJoin(reqDto);
    }
}
