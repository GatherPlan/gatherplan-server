package com.example.gatherplan.controller;

import com.example.gatherplan.appointment.dto.CreateTempUserReqDto;
import com.example.gatherplan.appointment.dto.TempCheckHostReqDto;
import com.example.gatherplan.appointment.dto.TempCheckJoinReqDto;
import com.example.gatherplan.controller.mapper.TempUserVoMapper;
import com.example.gatherplan.controller.service.TempUserFacadeService;
import com.example.gatherplan.controller.vo.common.BooleanResp;
import com.example.gatherplan.controller.vo.tempuser.CreateTempUserReq;
import com.example.gatherplan.controller.vo.tempuser.TempCheckHostReq;
import com.example.gatherplan.controller.vo.tempuser.TempCheckJoinReq;
import com.example.gatherplan.controller.vo.tempuser.TempCheckUserReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/temporary/users")
@Tag(name = "비회원", description = "비회원 관련된 기능을 제공합니다.")
public class TempUserController {

    private final TempUserVoMapper tempUserVoMapper;
    private final TempUserFacadeService tempUserFacadeService;

    @GetMapping("/host:check")
    @Operation(summary = "비회원의 호스트의 여부 조회 요청", description = "비회원의 호스트 여부를 판단할 때 사용됩니다. [figma #19,#27]")
    public ResponseEntity<BooleanResp> checkHost(
            @Valid @ModelAttribute @ParameterObject TempCheckHostReq req) {

        TempCheckHostReqDto reqDto = tempUserVoMapper.to(req);
        boolean isValid = tempUserFacadeService.checkHost(reqDto);

        return ResponseEntity.ok(
                BooleanResp.of(isValid)
        );
    }

    @GetMapping("/existence:check")
    @Operation(summary = "비회원의 약속 포함 여부 조회", description = "비회원의 특정 약속에 포함 여부를 조회할 때 사용됩니다. [figma #27]")
    public ResponseEntity<BooleanResp> checkUser(
            @Valid @ModelAttribute @ParameterObject TempCheckUserReq req) {

        TempCheckJoinReqDto reqDto = tempUserVoMapper.to(req);
        boolean isValid = tempUserFacadeService.checkUser(reqDto);

        return ResponseEntity.ok(
                BooleanResp.of(isValid)
        );
    }

    @GetMapping("/join:check")
    @Operation(summary = "비회원의 약속 참여 여부 조회", description = "비회원의 약속 참여 여부를 조회할 때 사용됩니다. [figma #19,#27]")
    public ResponseEntity<BooleanResp> checkJoin(
            @Valid @ModelAttribute @ParameterObject TempCheckJoinReq req) {

        TempCheckJoinReqDto reqDto = tempUserVoMapper.to(req);
        boolean isValid = tempUserFacadeService.checkJoin(reqDto);

        return ResponseEntity.ok(
                BooleanResp.of(isValid)
        );
    }

    @GetMapping("/join:valid")
    @Operation(summary = "비회원의 임시 회원가입 가능 여부 확인 요청", description = "지정 약속에 비회원으로 가입 및 참여가 가능한지 확인합니다. [figma #19]")
    public ResponseEntity<BooleanResp> validJoin(
            @Valid @ModelAttribute @ParameterObject CreateTempUserReq req) {

        CreateTempUserReqDto reqDto = tempUserVoMapper.to(req);
        boolean isValid = tempUserFacadeService.validJoin(reqDto);

        return ResponseEntity.ok(
                BooleanResp.of(isValid)
        );
    }
}
