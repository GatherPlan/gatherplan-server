package com.example.gatherplan.controller;

import com.example.gatherplan.appointment.dto.*;
import com.example.gatherplan.common.config.jwt.UserInfo;
import com.example.gatherplan.controller.mapper.AppointmentVoMapper;
import com.example.gatherplan.controller.service.AppointmentFacadeService;
import com.example.gatherplan.controller.vo.appointment.*;
import com.example.gatherplan.controller.vo.common.BooleanResp;
import com.example.gatherplan.controller.vo.common.ListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/appointments")
@Tag(name = "약속 with 회원", description = "회원의 약속 관련된 기능을 제공합니다.")
public class AppointmentController {

    private final AppointmentFacadeService appointmentFacadeService;
    private final AppointmentVoMapper appointmentVoMapper;

    @PostMapping
    @Operation(summary = "회원의 약속 만들기 요청", description = "회원이 새로운 약속을 생성할 때 사용됩니다. [figma #8]")
    public ResponseEntity<CreateAppointmentResp> registerAppointment(
            @Valid @RequestBody CreateAppointmentReq req,
            @AuthenticationPrincipal UserInfo userInfo) {

        CreateAppointmentReqDto reqDto = appointmentVoMapper.to(req);
        String appointmentCode = appointmentFacadeService.registerAppointment(reqDto, userInfo.getId(), userInfo.getUsername());

        return ResponseEntity.ok(
                CreateAppointmentResp.of(appointmentCode)
        );
    }

    @GetMapping
    @Operation(summary = "회원의 약속 정보 조회 요청", description = "회원이 약속 정보를 조회할 때 사용됩니다. [figma #23,#40]")
    public ResponseEntity<AppointmentInfoResp> retrieveAppointmentInfo(
            @Schema(description = "약속 코드", example = "985a61f6f636")
            @RequestParam @NotBlank(message = "약속 코드는 공백이 될 수 없습니다.") String appointmentCode,
            @AuthenticationPrincipal UserInfo userInfo) {

        AppointmentInfoRespDto respDto = appointmentFacadeService.retrieveAppointmentInfo(appointmentCode, userInfo.getId());

        return ResponseEntity.ok(
                appointmentVoMapper.to(respDto)
        );
    }

    @PutMapping
    @Operation(summary = "회원의 약속 변경 요청", description = "회원이 약속을 변경할 때 사용됩니다. [figma #35]")
    public ResponseEntity<BooleanResp> updateAppointment(
            @Valid @RequestBody UpdateAppointmentReq req,
            @AuthenticationPrincipal UserInfo userInfo) {

        UpdateAppointmentReqDto reqDto = appointmentVoMapper.to(req);
        appointmentFacadeService.updateAppointment(reqDto, userInfo.getId());

        return ResponseEntity.ok(
                BooleanResp.success()
        );
    }

    @DeleteMapping
    @Operation(summary = "회원의 약속 삭제 요청", description = "회원이 약속을 삭제할 때 사용됩니다. [figma #25]")
    public ResponseEntity<BooleanResp> deleteAppointment(
            @Schema(description = "약속 코드", example = "985a61f6f636")
            @RequestParam @NotBlank(message = "약속 코드는 공백이 될 수 없습니다.") String appointmentCode,
            @AuthenticationPrincipal UserInfo userInfo) {

        appointmentFacadeService.deleteAppointment(appointmentCode, userInfo.getId());

        return ResponseEntity.ok(
                BooleanResp.success()
        );
    }

    @PostMapping("/join")
    @Operation(summary = "회원의 약속 참여 요청", description = "회원이 약속에 참여할 때 사용됩니다. [figma #15]")
    public ResponseEntity<BooleanResp> registerAppointmentJoin(
            @Valid @RequestBody CreateAppointmentJoinReq req,
            @AuthenticationPrincipal UserInfo userInfo) {

        CreateAppointmentJoinReqDto reqDto = appointmentVoMapper.to(req);
        appointmentFacadeService.registerAppointmentJoin(reqDto, userInfo.getId());

        return ResponseEntity.ok(
                BooleanResp.success()
        );
    }

    @GetMapping("/participants")
    @Operation(summary = "회원의 지정 약속의 모든 참여 정보 조회 요청", description = "회원이 지정 약속의 모든 참여 정보를 조회할 때 사용됩니다. [figma #24,#41]")
    public ResponseEntity<ListResponse<AppointmentParticipantsResp>> retrieveAppointmentParticipants(
            @Schema(description = "약속 코드", example = "985a61f6f636")
            @RequestParam @NotBlank(message = "약속 코드는 공백이 될 수 없습니다.") String appointmentCode,
            @AuthenticationPrincipal UserInfo userInfo) {

        List<AppointmentParticipantsRespDto> respDtoList = appointmentFacadeService
                .retrieveAppointmentParticipants(appointmentCode, userInfo.getId());

        return ResponseEntity.ok(
                ListResponse.of(
                        respDtoList.stream().map(appointmentVoMapper::to).toList()
                )
        );
    }

    @GetMapping("/participants/my")
    @Operation(summary = "회원의 지정 약속의 나의 참여 정보 조회 요청", description = "회원이 지정 약속의 자신의 참여 정보를 조회할 때 사용됩니다. [figma #29]")
    public ResponseEntity<AppointmentMyParticipantResp> retrieveAppointmentMyParticipant(
            @Schema(description = "약속 코드", example = "985a61f6f636")
            @RequestParam @NotBlank(message = "약속 코드는 공백이 될 수 없습니다.") String appointmentCode,
            @AuthenticationPrincipal UserInfo userInfo) {

        AppointmentMyParticipantRespDto appointmentMyParticipantRespDto =
                appointmentFacadeService.retrieveAppointmentMyParticipant(appointmentCode, userInfo.getId());

        return ResponseEntity.ok(
                appointmentVoMapper.to(appointmentMyParticipantRespDto)
        );
    }

    @PutMapping("/join")
    @Operation(summary = "회원의 약속 참여 변경 요청", description = "회원이 약속 참여를 변경할 때 사용됩니다. [figma #30]")
    public ResponseEntity<BooleanResp> updateAppointmentJoin(
            @Valid @RequestBody UpdateAppointmentJoinReq req,
            @AuthenticationPrincipal UserInfo userInfo) {

        UpdateAppointmentJoinReqDto reqDto = appointmentVoMapper.to(req);

        appointmentFacadeService.updateAppointmentJoin(reqDto, userInfo.getId());

        return ResponseEntity.ok(
                BooleanResp.success()
        );
    }

    @DeleteMapping("/join")
    @Operation(summary = "회원의 약속 참여 삭제 요청", description = "회원이 약속 참여를 삭제할 때 사용됩니다. [figma #28]")
    public ResponseEntity<BooleanResp> deleteAppointmentJoin(
            @Schema(description = "약속 코드", example = "985a61f6f636")
            @RequestParam @NotBlank(message = "약속 코드는 공백이 될 수 없습니다.") String appointmentCode,
            @AuthenticationPrincipal UserInfo userInfo) {

        appointmentFacadeService.deleteAppointmentJoin(appointmentCode, userInfo.getId());

        return ResponseEntity.ok(
                BooleanResp.success()
        );
    }

    @GetMapping("/candidates")
    @Operation(summary = "회원의 약속 확정 후보 날짜 정보 조회 요청", description = "회원이 약속 확정 후보 날짜 정보를 조회할 때 사용됩니다. [figma #37]")
    public ResponseEntity<ListResponse<AppointmentCandidateInfoResp>> retrieveCandidateInfo(
            @Valid @ModelAttribute @ParameterObject AppointmentCandidateInfoReq req,
            @AuthenticationPrincipal UserInfo userInfo
    ) {
        AppointmentCandidateInfoReqDto reqDto = appointmentVoMapper.to(req);

        Page<AppointmentCandidateInfoRespDto> respDtoList
                = appointmentFacadeService.retrieveCandidateInfo(reqDto, userInfo.getId());

        return ResponseEntity.ok(
                ListResponse.of(
                        respDtoList.map(appointmentVoMapper::to)
                )
        );
    }

    @PostMapping("/candidates:confirm")
    @Operation(summary = "회원의 약속 확정 요청", description = "회원이 약속 시간을 확정할 때 사용됩니다. [figma #38]")
    public ResponseEntity<BooleanResp> confirmAppointment(
            @Valid @RequestBody ConfirmAppointmentReq req,
            @AuthenticationPrincipal UserInfo userInfo) {

        ConfirmAppointmentReqDto reqDto = appointmentVoMapper.to(req);
        appointmentFacadeService.confirmAppointment(reqDto, userInfo.getId());

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

        boolean isHost = appointmentFacadeService.checkHost(appointmentCode, userInfo.getId());

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

        boolean isParticipated = appointmentFacadeService.checkJoin(appointmentCode, userInfo.getId());

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

        boolean isValid = appointmentFacadeService.checkName(appointmentCode, userInfo.getUsername());

        return ResponseEntity.ok(
                BooleanResp.of(isValid)
        );
    }

    @GetMapping("/nickname:check")
    @Operation(summary = "회원의 닉네임 중복 여부 확인 요청", description = "회원이 입력한 닉네임이 참여하려는 약속에 중복되지 않는지 확인할 때 사용됩니다. [figma #20]")
    public ResponseEntity<BooleanResp> checkNickname(
            @Valid @ModelAttribute @ParameterObject ValidationNicknameReq req) {

        boolean isValid = appointmentFacadeService.checkNickname(req.getAppointmentCode(), req.getNickname());

        return ResponseEntity.ok(
                BooleanResp.of(isValid)
        );
    }

    @GetMapping("/list:search")
    @Operation(summary = "회원의 약속 목록 키워드 조회 요청", description = "회원이 약속 목록을 키워드로 조회할 때 사용됩니다. [figma #22]")
    public ResponseEntity<ListResponse<AppointmentSearchListResp>> retrieveAppointmentSearchList(
            @Schema(description = "약속 이름 검색 키워드", example = "세 얼간이")
            @RequestParam(required = false) @Size(min = 2, message = "키워드는 2자 이상이어야합니다.") String keyword,
            @AuthenticationPrincipal UserInfo userInfo) {

        List<AppointmentSearchListRespDto> appointmentListInfoDtoSearchList =
                appointmentFacadeService.retrieveAppointmentSearchList(keyword, userInfo.getId());

        return ResponseEntity.ok(
                ListResponse.of(appointmentListInfoDtoSearchList.stream().map(appointmentVoMapper::to).toList())
        );
    }

    @GetMapping("/preview")
    @Operation(summary = "회원의 약속 미리보기 조회 요청", description = "회원이 약속 미리보기를 조회할 때 사용됩니다. [figma #11,#17,#18,#26]")
    public ResponseEntity<AppointmentPreviewResp> retrieveAppointmentPreview(
            @Schema(description = "약속 코드", example = "985a61f6f636")
            @RequestParam @NotBlank(message = "약속 코드는 공백이 될 수 없습니다.") String appointmentCode) {

        AppointmentPreviewRespDto respDto = appointmentFacadeService.retrieveAppointmentPreview(appointmentCode);

        return ResponseEntity.ok(
                appointmentVoMapper.to(respDto)
        );
    }

}
