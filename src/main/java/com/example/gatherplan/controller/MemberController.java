package com.example.gatherplan.controller;

import com.example.gatherplan.appointment.dto.AuthenticateEmailReqDto;
import com.example.gatherplan.appointment.dto.CreateMemberReqDto;
import com.example.gatherplan.appointment.dto.CreateTemporaryMemberReqDto;
import com.example.gatherplan.appointment.mapper.MemberMapper;
import com.example.gatherplan.appointment.service.MemberService;
import com.example.gatherplan.common.vo.response.BooleanResp;
import com.example.gatherplan.controller.validation.RequestValidationSequence;
import com.example.gatherplan.controller.vo.appointment.AuthenticateEmailReq;
import com.example.gatherplan.controller.vo.appointment.CreateMemberReq;
import com.example.gatherplan.controller.vo.appointment.CreateTemporaryMemberReq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;
    private final MemberMapper memberMapper;

    @PostMapping("/auth/email")
    public ResponseEntity<BooleanResp> authenticateEmail(
            @Validated(value = RequestValidationSequence.class)
            @RequestBody AuthenticateEmailReq authenticateEmailReq) {

        AuthenticateEmailReqDto authenticateEmailReqDto = memberMapper.to(authenticateEmailReq);
        memberService.authenticateEmail(authenticateEmailReqDto);

        return ResponseEntity.ok(
                BooleanResp.of(true)
        );
    }

    @PostMapping("/join")
    public ResponseEntity<BooleanResp> joinMember(
            @Validated(value = RequestValidationSequence.class)
            @RequestBody CreateMemberReq createMemberReq
    ) {
        CreateMemberReqDto createMemberReqDto = memberMapper.to(createMemberReq);
        memberService.joinMember(createMemberReqDto);

        return ResponseEntity.ok(
                BooleanResp.of(true)
        );
    }

    @PostMapping("/join/temporary")
    public ResponseEntity<BooleanResp> joinTemporaryMember(
            @Validated(value = RequestValidationSequence.class)
            @RequestBody CreateTemporaryMemberReq createTemporaryMemberReq
    ) {
        CreateTemporaryMemberReqDto createTemporaryMemberReqDto = memberMapper.to(createTemporaryMemberReq);
        memberService.joinTemporaryMember(createTemporaryMemberReqDto);

        return ResponseEntity.ok(
                BooleanResp.of(true)
        );
    }

}
