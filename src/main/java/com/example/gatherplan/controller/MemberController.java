package com.example.gatherplan.controller;

import com.example.gatherplan.appointment.dto.LocalJoinEmailDto;
import com.example.gatherplan.appointment.dto.LocalJoinFormDto;
import com.example.gatherplan.appointment.dto.LocalLoginFormDto;
import com.example.gatherplan.appointment.dto.TemporaryJoinFormDto;
import com.example.gatherplan.appointment.mapper.UserMapper;
import com.example.gatherplan.appointment.service.MemberService;
import com.example.gatherplan.common.vo.response.BooleanResp;
import com.example.gatherplan.controller.validation.RequestValidationSequence;
import com.example.gatherplan.controller.vo.member.LocalJoinEmailReq;
import com.example.gatherplan.controller.vo.member.LocalJoinFormReq;
import com.example.gatherplan.controller.vo.member.LocalLoginFormReq;
import com.example.gatherplan.controller.vo.member.TemporaryJoinFormReq;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/join")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/local/send-authcode")
    public ResponseEntity<BooleanResp> localSendAuthCode(
            @Validated(value = RequestValidationSequence.class)
            @RequestBody LocalJoinEmailReq localJoinEmailReq) {

        LocalJoinEmailDto localJoinEmailDto = Mappers.getMapper(UserMapper.class).toLocalJoinEmailDto(localJoinEmailReq);
        memberService.sendAuthCodeProcess(localJoinEmailDto);

        return ResponseEntity.ok(
                BooleanResp.of(true)
        );
    }

    @PostMapping("/local/validate-form")
    public ResponseEntity<BooleanResp> localValidateForm(
            @Validated(value = RequestValidationSequence.class)
            @RequestBody LocalJoinFormReq localJoinFormReq
    ) {
        LocalJoinFormDto localJoinFormDto = Mappers.getMapper(UserMapper.class).toLocalJoinFormDto(localJoinFormReq);
        memberService.validateLocalJoinFormProcess(localJoinFormDto);

        return ResponseEntity.ok(
                BooleanResp.of(true)
        );
    }

    @PostMapping("/temporary/validate-form")
    public ResponseEntity<BooleanResp> temporaryValidateForm(
            @Validated(value = RequestValidationSequence.class)
            @RequestBody TemporaryJoinFormReq temporaryJoinFormReq
    ) {
        TemporaryJoinFormDto temporaryJoinFormDto = Mappers.getMapper(UserMapper.class).toTemporaryJoinFormDto(temporaryJoinFormReq);
        memberService.temporaryJoin(temporaryJoinFormDto);

        return ResponseEntity.ok(
                BooleanResp.of(true)
        );
    }

    @PostMapping("/local/login")
    public ResponseEntity<BooleanResp> localLogin(
            @Validated(value = RequestValidationSequence.class)
            @RequestBody LocalLoginFormReq localLoginFormReq, HttpServletRequest httpServletRequest
    ) {
        LocalLoginFormDto localLoginFormDto = Mappers.getMapper(UserMapper.class).toLocalLoginFormDto(localLoginFormReq);
        memberService.localLoginProcess(localLoginFormDto, httpServletRequest);

        return ResponseEntity.ok(
                BooleanResp.of(true)
        );
    }

}
