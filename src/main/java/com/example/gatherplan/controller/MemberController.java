package com.example.gatherplan.controller;

import com.example.gatherplan.appointment.dto.AuthenticateEmailReqDto;
import com.example.gatherplan.appointment.dto.CreateMemberReqDto;
import com.example.gatherplan.appointment.service.MemberService;
import com.example.gatherplan.controller.mapper.MemberVoMapper;
import com.example.gatherplan.controller.validation.CreateMemberReqValidSeq;
import com.example.gatherplan.controller.vo.appointment.AuthenticateEmailReq;
import com.example.gatherplan.controller.vo.appointment.CreateMemberReq;
import com.example.gatherplan.controller.vo.common.BooleanResp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@Tag(name = "회원", description = "회원과 관련된 기능을 제공합니다.")
public class MemberController {

    private final MemberService memberService;
    private final MemberVoMapper memberVoMapper;

    @PostMapping("/auth/email")
    @Operation(summary = "이메일 인증 요청", description = "사용자가 이메일 인증 코드를 받기 위해 사용됩니다.")
    public ResponseEntity<BooleanResp> authenticateEmail(
            @Valid @RequestBody AuthenticateEmailReq authenticateEmailReq) {

        AuthenticateEmailReqDto authenticateEmailReqDto = memberVoMapper.to(authenticateEmailReq);
        memberService.authenticateEmail(authenticateEmailReqDto);

        return ResponseEntity.ok(
                BooleanResp.success()
        );
    }

    @PostMapping("/join")
    @Operation(summary = "회원가입 요청", description = "사용자가 새로운 회원으로 가입할 때 사용됩니다.")
    public ResponseEntity<BooleanResp> joinMember(
            @Validated(value = CreateMemberReqValidSeq.class)
            @RequestBody CreateMemberReq createMemberReq
    ) {
        CreateMemberReqDto createMemberReqDto = memberVoMapper.to(createMemberReq);
        memberService.joinMember(createMemberReqDto);

        return ResponseEntity.ok(
                BooleanResp.success()
        );
    }
}

