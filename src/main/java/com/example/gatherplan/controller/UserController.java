package com.example.gatherplan.controller;

import com.example.gatherplan.appointment.dto.CreateUserReqDto;
import com.example.gatherplan.appointment.dto.UserInfoRespDto;
import com.example.gatherplan.appointment.service.UserService;
import com.example.gatherplan.common.config.jwt.UserInfo;
import com.example.gatherplan.controller.mapper.UserVoMapper;
import com.example.gatherplan.controller.vo.common.BooleanResp;
import com.example.gatherplan.controller.vo.user.CreateUserReq;
import com.example.gatherplan.controller.vo.user.EmailAuthReq;
import com.example.gatherplan.controller.vo.user.PasswordResetReq;
import com.example.gatherplan.controller.vo.user.ValidationNicknameReq;
import com.example.gatherplan.controller.vo.user.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "회원", description = "회원과 관련된 기능을 제공합니다.")
public class UserController {

    private final UserService userService;
    private final UserVoMapper userVoMapper;

    @PostMapping("/auth")
    @Operation(summary = "이메일 인증 요청", description = "사용자가 이메일 인증 코드를 받기 위해 사용됩니다. [figma #4]")
    public ResponseEntity<BooleanResp> authenticateEmail(
            @Valid @RequestBody EmailAuthReq req) {

        userService.authenticateEmail(req.getEmail());

        return ResponseEntity.ok(
                BooleanResp.success()
        );
    }

    @PostMapping("/join")
    @Operation(summary = "회원가입 요청", description = "사용자가 새로운 회원으로 가입할 때 사용됩니다. [figma #4]")
    public ResponseEntity<BooleanResp> joinUser(
            @Valid @RequestBody CreateUserReq req) {

        CreateUserReqDto createUserReqDto = userVoMapper.to(req);
        userService.joinUser(createUserReqDto);

        return ResponseEntity.ok(
                BooleanResp.success()
        );
    }

    @GetMapping("/host:check")
    @Operation(summary = "회원의 호스트 여부 조회 요청", description = "회원의 호스트 여부를 판단할 때 사용됩니다. [figma #12]")
    public ResponseEntity<BooleanResp> checkHost(
            @Schema(description = "약속 코드", example = "985a61f6f636")
            @RequestParam @NotBlank(message = "약속 코드는 공백이 될 수 없습니다.") String appointmentCode,
            @AuthenticationPrincipal UserInfo userInfo) {

        boolean isHost = userService.checkHost(appointmentCode, userInfo.getId());

        return ResponseEntity.ok(
                BooleanResp.of(isHost)
        );
    }

    @GetMapping("/join:check")
    @Operation(summary = "회원의 약속 참여 여부 조회 요청", description = "회원의 약속 참여 여부를 조회할 때 사용됩니다. [figma #12,#18]")
    public ResponseEntity<BooleanResp> checkJoin(
            @Schema(description = "약속 코드", example = "985a61f6f636")
            @RequestParam @NotBlank(message = "약속 코드는 공백이 될 수 없습니다.") String appointmentCode,
            @AuthenticationPrincipal UserInfo userInfo) {

        boolean isParticipated = userService.checkJoin(appointmentCode, userInfo.getId());

        return ResponseEntity.ok(
                BooleanResp.of(isParticipated)
        );
    }

    @GetMapping("/name:check")
    @Operation(summary = "회원의 이름 중복 여부 확인 요청", description = "회원의 이름이 참여하려는 약속에 중복되지 않는지 확인할 때 사용됩니다. [figma #12]")
    public ResponseEntity<BooleanResp> checkName(
            @Schema(description = "약속 코드", example = "985a61f6f636")
            @RequestParam @NotBlank(message = "약속 코드는 공백이 될 수 없습니다.") String appointmentCode,
            @AuthenticationPrincipal UserInfo userInfo) {

        boolean isValid = userService.checkName(appointmentCode, userInfo.getUsername());

        return ResponseEntity.ok(
                BooleanResp.of(isValid)
        );
    }

    @GetMapping("/nickname:check")
    @Operation(summary = "회원의 닉네임 중복 여부 확인 요청", description = "회원이 입력한 닉네임이 참여하려는 약속에 중복되지 않는지 확인할 때 사용됩니다. [figma #20]")
    public ResponseEntity<BooleanResp> checkNickname(
            @Valid @ModelAttribute @ParameterObject ValidationNicknameReq req) {

        boolean isValid = userService.checkNickname(req.getAppointmentCode(), req.getNickname());

        return ResponseEntity.ok(
                BooleanResp.of(isValid)
        );
    }

    @GetMapping("/password:auth")
    @Operation(summary = "비밀번호 재설정을 위한 이메일 인증번호 전송 요청", description = "회원이 비밀번호 재설정 과정에서 필요한 이메일 인증번호를 받기 위해 사용됩니다.")
    public ResponseEntity<BooleanResp> authenticateEmailForPasswordReset(
            @Schema(description = "이메일", example = "email@example.com")
            @NotBlank(message = "이메일은 공백이 될 수 없습니다.")
            @Email(message = "이메일 형식이 맞지 않습니다.") String email) {

        userService.authenticateEmailForPasswordReset(email);

        return ResponseEntity.ok(
                BooleanResp.success()
        );
    }

    @PostMapping("/password:reset")
    @Operation(summary = "회원의 비밀번호 재설정 요청", description = "회원이 비밀번호를 분실한 경우 비밀번호를 재설정할 때 사용됩니다.")
    public ResponseEntity<BooleanResp> resetPassword(
            @Valid @RequestBody PasswordResetReq req) {

        userService.resetPassword(req.getEmail(), req.getAuthCode(), req.getPassword());

        return ResponseEntity.ok(
                BooleanResp.success()
        );
    }


    @GetMapping
    @Operation(summary = "회원의 정보 조회 요청", description = "회원이 자신의 회원 정보를 확인할 때 사용됩니다.")
    public ResponseEntity<UserInfoResp> retrieveUserInfo(
            @AuthenticationPrincipal UserInfo userInfo) {

        UserInfoRespDto respDto = userService.retrieveUserInfo(userInfo);

        return ResponseEntity.ok(
                userVoMapper.toUserInfoResp(respDto)
        );
    }

    @DeleteMapping
    @Operation(summary = "회원 탈퇴 요청", description = "회원이 탈퇴할 때 사용됩니다.")
    public ResponseEntity<BooleanResp> deleteUser(
            @AuthenticationPrincipal UserInfo userInfo) {

        userService.deleteUser(userInfo);

        return ResponseEntity.ok(
                BooleanResp.success()
        );
    }

    @PatchMapping
    @Operation(summary = "회원 정보 수정 요청", description = "회원이 회원 정보를 수정할 때 사용됩니다.")
    public ResponseEntity<BooleanResp> updateUser(
            @Valid @RequestBody UpdateUsereReq req,
            @AuthenticationPrincipal UserInfo userInfo) {

        userService.updateUser(req.getName(), userInfo.getId());

        return ResponseEntity.ok(
                BooleanResp.success()
        );
    }
}

