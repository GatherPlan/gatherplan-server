package com.example.gatherplan.controller;

import com.example.gatherplan.appointment.dto.LocalJoinEmailDto;
import com.example.gatherplan.appointment.mapper.UserMapper;
import com.example.gatherplan.common.vo.response.BooleanResp;

import com.example.gatherplan.appointment.dto.LocalJoinFormDto;
import com.example.gatherplan.appointment.service.MemberService;
import com.example.gatherplan.controller.validation.LocalJoinFormValidationSequence;
import com.example.gatherplan.controller.vo.member.LocalJoinEmailReq;
import com.example.gatherplan.controller.vo.member.LocalJoinFormReq;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/join")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/send-authcode")
    public ResponseEntity<BooleanResp> sendAuthCode(
            @Validated(value = LocalJoinFormValidationSequence.class)
            @RequestBody LocalJoinEmailReq localJoinEmailReq){

        LocalJoinEmailDto localJoinEmailDto = Mappers.getMapper(UserMapper.class).toLocalJoinEmailDto(localJoinEmailReq);
        memberService.sendAuthCodeProcess(localJoinEmailDto);

        return ResponseEntity.ok(
                BooleanResp.of(true)
        );
    }

    @PostMapping("/validate-form")
    public ResponseEntity<BooleanResp> validateLocalJoinForm(
            @Validated(value = LocalJoinFormValidationSequence.class)
            @RequestBody LocalJoinFormReq localJoinFormReq
    ){
        LocalJoinFormDto localJoinFormDto = Mappers.getMapper(UserMapper.class).toLocalJoinFormDto(localJoinFormReq);
        memberService.validateLocalJoinFormProcess(localJoinFormDto);

        return ResponseEntity.ok(
                BooleanResp.of(true)
        );
    }
}
