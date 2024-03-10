package com.example.gatherplan.controller;

import com.example.gatherplan.appointment.dto.AuthenticateEmailReqDto;
import com.example.gatherplan.appointment.dto.CreateMemberReqDto;
import com.example.gatherplan.appointment.dto.CreateTemporaryMemberReqDto;
import com.example.gatherplan.appointment.dto.LoginMemberReqDto;
import com.example.gatherplan.appointment.mapper.MemberMapper;
import com.example.gatherplan.appointment.service.MemberService;
import com.example.gatherplan.common.vo.response.BooleanResp;
import com.example.gatherplan.controller.validation.RequestValidationSequence;
import com.example.gatherplan.controller.vo.appointment.AuthenticateEmailReq;
import com.example.gatherplan.controller.vo.appointment.CreateMemberReq;
import com.example.gatherplan.controller.vo.appointment.CreateTemporaryMemberReq;
import com.example.gatherplan.controller.vo.appointment.LoginMemberReq;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
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

    @Operation(summary = "이메일 인증", description = "이메일 인증 기능입니다.")
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

    /**
     * TODO : Token 방식 인증 체계 도입
     *
     * @param loginMemberReq
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<BooleanResp> login(
            @Validated(value = RequestValidationSequence.class)
            @RequestBody LoginMemberReq loginMemberReq, HttpServletRequest httpServletRequest
    ) {
        LoginMemberReqDto loginMemberReqDto = memberMapper.to(loginMemberReq);
        memberService.login(loginMemberReqDto, httpServletRequest);

        return ResponseEntity.ok(
                BooleanResp.of(true)
        );
    }

    /**
     * 프론트 확인용 임시 api
     *
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/login/check")
    public ResponseEntity<BooleanResp> loginCheck(HttpServletRequest httpServletRequest) {
        memberService.loginCheck(httpServletRequest);

        return ResponseEntity.ok(
                BooleanResp.of(true)
        );
    }

}
