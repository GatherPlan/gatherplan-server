package com.example.gatherplan.controller;

import com.example.gatherplan.appointment.dto.CreateTempUserReqDto;
import com.example.gatherplan.appointment.dto.TempUserLoginReqDto;
import com.example.gatherplan.appointment.service.TempUserService;
import com.example.gatherplan.controller.mapper.TempUserVoMapper;
import com.example.gatherplan.controller.validation.RequestValidationSeq;
import com.example.gatherplan.controller.vo.appointment.CreateTempUserReq;
import com.example.gatherplan.controller.vo.appointment.TempUserLoginReq;
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
@RequestMapping("/api/v1/temporary/users")
@Tag(name = "비회원", description = "비회원과 관련된 기능을 제공합니다.")
@Validated(value = RequestValidationSeq.class)
public class TempUserController {

    private final TempUserVoMapper tempUserVoMapper;
    private final TempUserService tempUserService;

    @PostMapping("/login")
    @Operation(summary = "비회원 호스트의 로그인", description = "비회원 호스트의 로그인 정보를 확인합니다.")
    public ResponseEntity<BooleanResp> login(@RequestBody @Valid TempUserLoginReq req) {

        TempUserLoginReqDto reqDto = tempUserVoMapper.to(req);
        tempUserService.login(reqDto);

        return ResponseEntity.ok(
                BooleanResp.success()
        );
    }

    @PostMapping("/join:validate")
    @Operation(summary = "비회원 임시 회원가입 가능 여부 확인", description = "지정 약속에 비회원으로 임시 가입이 가능한지 확인합니다.")
    public ResponseEntity<BooleanResp> validJoinUser(
            @Valid @RequestBody CreateTempUserReq req) {

        CreateTempUserReqDto reqDto = tempUserVoMapper.to(req);
        boolean isValid = tempUserService.validJoinTempUser(reqDto);

        return ResponseEntity.ok(
                BooleanResp.of(isValid)
        );
    }
}

