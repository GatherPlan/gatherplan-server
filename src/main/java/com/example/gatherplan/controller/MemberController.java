package com.example.gatherplan.controller;

import com.example.gatherplan.appointment.dto.AuthenticateEmailReqDto;
import com.example.gatherplan.appointment.dto.CreateMemberReqDto;
import com.example.gatherplan.appointment.dto.CreateTempMemberReqDto;
import com.example.gatherplan.appointment.mapper.MemberMapper;
import com.example.gatherplan.appointment.service.MemberService;
import com.example.gatherplan.controller.validation.RequestValidationSequence;
import com.example.gatherplan.controller.vo.appointment.AuthenticateEmailReq;
import com.example.gatherplan.controller.vo.appointment.CreateMemberReq;
import com.example.gatherplan.controller.vo.appointment.CreateTempMemberReq;
import com.example.gatherplan.controller.vo.common.BooleanResp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "회원", description = "회원 관련된 기능을 제공합니다.")
public class MemberController {

    private final MemberService memberService;
    private final MemberMapper memberMapper;

    @PostMapping("/auth/email")
    @Operation(summary = "이메일 인증 요청", description = "사용자가 이메일 인증 코드를 받기 위해 사용됩니다.")
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
    @Operation(summary = "회원가입 요청", description = "사용자가 새로운 회원으로 가입할 때 사용됩니다.")
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
    @Operation(summary = "임시 회원가입 요청", description = "사용자가 새로운 임시 회원으로 가입할 때 사용됩니다.")
    public ResponseEntity<BooleanResp> joinTemporaryMember(
            @Validated(value = RequestValidationSequence.class)
            @RequestBody CreateTempMemberReq createTempMemberReq
    ) {
        CreateTempMemberReqDto createTempMemberReqDto = memberMapper.to(createTempMemberReq);
        memberService.joinTempMember(createTempMemberReqDto);

        return ResponseEntity.ok(
                BooleanResp.of(true)
        );
    }

}
