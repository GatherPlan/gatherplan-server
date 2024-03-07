package com.example.gatherplan.controller;

import com.example.gatherplan.common.vo.response.BooleanResp;
import com.example.gatherplan.common.validation.NotBlankEmail;
import com.example.gatherplan.common.validation.PatternCheckEmail;
import com.example.gatherplan.appointment.dto.LocalJoinFormDto;
import com.example.gatherplan.appointment.service.MemberService;
import com.example.gatherplan.appointment.validation.LocalJoinFormValidationSequence;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("/send-authcode")
    public ResponseEntity<BooleanResp> sendAuthCode(
            @Validated(value = LocalJoinFormValidationSequence.class)
            @NotBlank(message = "이메일은 공백이 될 수 없습니다.", groups = NotBlankEmail.class)
            @Email(message = "이메일 형식이 맞지 않습니다.", groups = PatternCheckEmail.class)
            String email
    ){
        memberService.sendAuthCodeProcess(email);

        return ResponseEntity.ok(
                BooleanResp.of(true)
        );
    }

    @PostMapping("/validate-form")
    public ResponseEntity<BooleanResp> validateLocalJoinForm(
            @Validated(value = LocalJoinFormValidationSequence.class)
            @RequestBody LocalJoinFormDto localJoinFormDto
    ){

        memberService.validateLocalJoinFormProcess(localJoinFormDto);

        return ResponseEntity.ok(
                BooleanResp.of(true)
        );
    }
}
