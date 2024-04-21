package com.example.gatherplan.controller;

import com.example.gatherplan.appointment.dto.*;
import com.example.gatherplan.appointment.service.AppointmentService;
import com.example.gatherplan.common.config.jwt.UserInfo;
import com.example.gatherplan.controller.mapper.AppointmentVoMapper;
import com.example.gatherplan.controller.validation.RequestValidationSeq;
import com.example.gatherplan.controller.vo.appointment.*;
import com.example.gatherplan.controller.vo.common.BooleanResp;
import com.example.gatherplan.controller.vo.common.ListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/appointments")
@Tag(name = "약속 with 회원", description = "회원의 약속 관련된 기능을 제공합니다.")
@Validated(value = RequestValidationSeq.class)
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final AppointmentVoMapper appointmentVoMapper;

    @PostMapping
    @Operation(summary = "회원의 약속 만들기 요청", description = "회원이 새로운 약속을 생성할 때 사용됩니다.")
    public ResponseEntity<CreateAppointmentResp> registerAppointment(
            @Valid @RequestBody CreateAppointmentReq req,
            @AuthenticationPrincipal UserInfo userInfo) {

        CreateAppointmentReqDto reqDto = appointmentVoMapper.to(req);
        String appointmentCode = appointmentService.registerAppointment(reqDto, userInfo.getId());

        return ResponseEntity.ok(
                CreateAppointmentResp.of(appointmentCode)
        );
    }

    @GetMapping("/participation-status")
    @Operation(summary = "회원의 약속 참여 상태 확인 요청", description = "회원의 약속 참여 상태를 판단할 때 사용됩니다.")
    public ResponseEntity<BooleanResp> retrieveParticipationStatus(
            @Schema(description = "약속 코드", example = "985a61f6f636")
            @RequestParam @NotBlank(message = "약속 코드는 공백이 될 수 없습니다.") String appointmentCode,
            @AuthenticationPrincipal UserInfo userInfo) {

        boolean isParticipated = appointmentService.isUserParticipated(appointmentCode, userInfo.getEmail());

        return ResponseEntity.ok(
                BooleanResp.of(isParticipated)
        );
    }

    @GetMapping("/list:search")
    @Operation(summary = "회원의 약속 목록 키워드 조회 요청", description = "회원이 약속 목록을 키워드로 조회할 때 사용됩니다.")
    public ResponseEntity<ListResponse<AppointmentWithHostByKeywordResp>> retrieveAppointmentSearchList(
            @Schema(description = "약속 이름 검색 키워드", example = "세 얼간이")
            @RequestParam(required = false) String keyword,
            @AuthenticationPrincipal UserInfo userInfo) {

        List<AppointmentWithHostByKeywordRespDto> respDtos = appointmentService
                .retrieveAppointmentSearchList(keyword, userInfo.getEmail(), userInfo.getUsername());

        return ResponseEntity.ok(
                ListResponse.of(
                        respDtos.stream().map(appointmentVoMapper::to).toList()
                )
        );
    }

    @GetMapping
    @Operation(summary = "회원의 약속 정보 조회 요청", description = "회원이 약속 정보를 조회할 때 사용됩니다.")
    public ResponseEntity<AppointmentInfoResp> retrieveAppointmentInfo(
            @Schema(description = "약속 코드", example = "985a61f6f636")
            @RequestParam @NotBlank(message = "약속 코드는 공백이 될 수 없습니다.") String appointmentCode,
            @AuthenticationPrincipal UserInfo userInfo) {

        AppointmentInfoRespDto respDto = appointmentService.retrieveAppointmentInfo(appointmentCode, userInfo.getEmail());

        return ResponseEntity.ok(
                appointmentVoMapper.to(respDto)
        );
    }

    @GetMapping("/participants")
    @Operation(summary = "회원의 약속 참여 정보 조회 요청", description = "회원이 약속 참여 정보를 조회할 때 사용됩니다.")
    public ResponseEntity<AppointmentParticipationInfoResp> retrieveAppointmentParticipationInfo(
            @Schema(description = "약속 코드", example = "985a61f6f636")
            @RequestParam @NotBlank(message = "약속 코드는 공백이 될 수 없습니다.") String appointmentCode,
            @AuthenticationPrincipal UserInfo userInfo) {

        AppointmentParticipationInfoRespDto respDto = appointmentService
                .retrieveAppointmentParticipationInfo(appointmentCode, userInfo.getEmail());

        return ResponseEntity.ok(
                appointmentVoMapper.to(respDto)
        );
    }

    @DeleteMapping
    @Operation(summary = "회원의 약속 삭제 요청", description = "회원이 약속을 삭제할 때 사용됩니다.")
    public ResponseEntity<BooleanResp> deleteAppointment(
            @Schema(description = "약속 코드", example = "985a61f6f636")
            @RequestParam @NotBlank(message = "약속 코드는 공백이 될 수 없습니다.") String appointmentCode,
            @AuthenticationPrincipal UserInfo userInfo) {

        appointmentService.deleteAppointment(appointmentCode, userInfo.getEmail());

        return ResponseEntity.ok(
                BooleanResp.success()
        );
    }

    @PutMapping
    @Operation(summary = "회원의 약속 변경 요청", description = "회원이 약속을 변경할 때 사용됩니다.")
    public ResponseEntity<BooleanResp> updateAppointment(
            @Valid @RequestBody UpdateAppointmentReq req,
            @AuthenticationPrincipal UserInfo userInfo) {

        UpdateAppointmentReqDto reqDto = appointmentVoMapper.to(req);
        appointmentService.updateAppointment(reqDto, userInfo.getEmail());

        return ResponseEntity.ok(
                BooleanResp.success()
        );
    }

    @GetMapping("/detail/{appointmentCode}")
    @Operation(summary = "약속 단건 조회", description = "약속 코드를 통해 하나의 약속을 조회합니다.")
    public ResponseEntity<AppointmentResp> retrieveAppointment(
            @Schema(description = "약속 코드", example = "abcdefghj124")
            @NotBlank(message = "약속 코드는 공백이 될 수 없습니다.")
            @PathVariable String appointmentCode
    ) {
        AppointmentRespDto respDto = appointmentService.retrieveAppointment(appointmentCode);

        return ResponseEntity.ok(
                appointmentVoMapper.to(respDto)
        );
    }

    @PostMapping("/participation")
    @Operation(summary = "회원의 약속 참여 등록", description = "회원의 약속 참여 정보를 등록합니다.")
    public ResponseEntity<BooleanResp> registerAppointmentParticipation(
            @Valid @RequestBody CreateAppointmentParticipationReq req,
            @AuthenticationPrincipal UserInfo userInfo
    ) {
        CreateAppointmentParticipationReqDto reqDto = appointmentVoMapper.to(req);
        appointmentService.registerAppointmentParticipation(reqDto, userInfo.getId());

        return ResponseEntity.ok(
                BooleanResp.success()
        );
    }

    @GetMapping("/participants/available")
    @Operation(summary = "약속 확정 시간에 참여 가능한 사용자 조회", description = "선택된 약속 확정 시간에 참여할 수 있는 사용자 목록을 조회합니다.")
    public ResponseEntity<ConfirmedAppointmentParticipantsResp> retrieveEligibleParticipantsList(
            @Valid @ModelAttribute @ParameterObject ConfirmedAppointmentParticipantsReq req,
            @AuthenticationPrincipal UserInfo userInfo) {

        ConfirmedAppointmentParticipantsReqDto reqDto = appointmentVoMapper.to(req);
        List<String> nicknameList =
                appointmentService.retrieveEligibleParticipantsList(reqDto, userInfo.getEmail());

        return ResponseEntity.ok(
                ConfirmedAppointmentParticipantsResp.of(nicknameList)
        );
    }

    @PostMapping(":confirm")
    @Operation(summary = "회원의 약속 확정 요청", description = "약속 확정 시간을 정할때 사용됩니다.")
    public ResponseEntity<BooleanResp> confirmedAppointment(
            @Valid @RequestBody ConfirmedAppointmentReq req,
            @AuthenticationPrincipal UserInfo userInfo) {

        ConfirmedAppointmentReqDto reqDto = appointmentVoMapper.to(req);
        appointmentService.confirmedAppointment(reqDto, userInfo.getEmail());

        return ResponseEntity.ok(
                BooleanResp.success()
        );
    }

}
