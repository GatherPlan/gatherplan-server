package com.example.gatherplan.controller;

import com.example.gatherplan.appointment.dto.CreateUserReqDto;
import com.example.gatherplan.appointment.service.UserService;
import com.example.gatherplan.controller.mapper.UserVoMapper;
import com.example.gatherplan.controller.validation.RequestValidationSeq;
import com.example.gatherplan.controller.vo.appointment.CreateUserReq;
import com.example.gatherplan.controller.vo.appointment.EmailAuthReq;
import com.example.gatherplan.controller.vo.common.BooleanResp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "회원", description = "회원과 관련된 기능을 제공합니다.")
@Validated(value = RequestValidationSeq.class)
public class UserController {

    private final UserService userService;
    private final UserVoMapper userVoMapper;

    @PostMapping("/auth")
    @Operation(summary = "이메일 인증 요청", description = "사용자가 이메일 인증 코드를 받기 위해 사용됩니다.")
    public ResponseEntity<BooleanResp> authenticateEmail(
            @Valid @RequestBody EmailAuthReq req) {

        userService.authenticateEmail(req.getEmail());

        return ResponseEntity.ok(
                BooleanResp.success()
        );
    }

    @PostMapping("/join")
    @Operation(summary = "회원가입 요청", description = "사용자가 새로운 회원으로 가입할 때 사용됩니다.")
    public ResponseEntity<BooleanResp> joinUser(
            @Valid @RequestBody CreateUserReq req) {

        CreateUserReqDto createUserReqDto = userVoMapper.to(req);
        userService.joinUser(createUserReqDto);

        return ResponseEntity.ok(
                BooleanResp.success()
        );
    }
}

