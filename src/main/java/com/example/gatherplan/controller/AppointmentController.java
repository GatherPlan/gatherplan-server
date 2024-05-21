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

    @GetMapping("/preview")
    @Operation(summary = "회원 약속 미리보기 정보 조회 요청", description = "회원이 약속 미리보기 정보를 조회할 때 사용됩니다.")
    public ResponseEntity<AppointmentInfoResp> retrieveAppointmentInfo(
            @Schema(description = "약속 코드", example = "985a61f6f636")
            @RequestParam @NotBlank(message = "약속 코드는 공백이 될 수 없습니다.") String appointmentCode) {

        AppointmentInfoRespDto respDto = appointmentService.retrieveAppointmentInfo(appointmentCode);

        return ResponseEntity.ok(
                appointmentVoMapper.to(respDto)
        );
    }

    @GetMapping("/participation-status")
    @Operation(summary = "회원 약속 참여 여부 조회", description = "회원의 약속 참여 여부를 조회할 때 사용됩니다.")
    public ResponseEntity<BooleanResp> retrieveParticipationStatus(
            @Schema(description = "약속 코드", example = "985a61f6f636")
            @RequestParam @NotBlank(message = "약속 코드는 공백이 될 수 없습니다.") String appointmentCode,
            @AuthenticationPrincipal UserInfo userInfo) {

        boolean isValid = appointmentService.retrieveParticipationStatus(appointmentCode, userInfo.getEmail());

        return ResponseEntity.ok(
                BooleanResp.of(isValid)
        );
    }

    @GetMapping("/validation/name")
    @Operation(summary = "회원 이름 약속 참여 가능 여부 확인", description = "회원의 이름으로 약속에 참여 가능한지 확인합니다.")
    public ResponseEntity<BooleanResp> validateName(
            @Schema(description = "약속 코드", example = "985a61f6f636")
            @RequestParam @NotBlank(message = "약속 코드는 공백이 될 수 없습니다.") String appointmentCode,
            @AuthenticationPrincipal UserInfo userInfo) {

        boolean isValid = appointmentService.validateName(appointmentCode, userInfo.getUsername());

        return ResponseEntity.ok(
                BooleanResp.of(isValid)
        );
    }

    @GetMapping("/validation/nickname")
    @Operation(summary = "회원이 입력한 닉네임으로 약속 참여 가능 여부 확인", description = "회원이 입력한 닉네임으로 약속에 참여 가능한지 확인합니다.")
    public ResponseEntity<BooleanResp> validateNickname(
            @Valid @ModelAttribute @ParameterObject ValidationNicknameReq req) {

        boolean isValid = appointmentService.validateNickname(req.getAppointmentCode(), req.getNickname());

        return ResponseEntity.ok(
                BooleanResp.of(isValid)
        );
    }

    @PostMapping("/participation")
    @Operation(summary = "회원의 약속 참여 등록", description = "회원의 약속 참여 정보를 등록합니다.")
    public ResponseEntity<BooleanResp> registerAppointmentParticipation(
            @Valid @RequestBody CreateAppointmentParticipationReq req,
            @AuthenticationPrincipal UserInfo userInfo) {

        CreateAppointmentParticipationReqDto reqDto = appointmentVoMapper.to(req);
        appointmentService.registerAppointmentParticipation(reqDto, userInfo.getId());

        return ResponseEntity.ok(
                BooleanResp.success()
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
    @Operation(summary = "회원 약속 정보 조회 요청", description = "회원이 약속 정보를 조회할 때 사용됩니다.")
    public ResponseEntity<AppointmentInfoDetailResp> retrieveAppointmentInfoDetail(
            @Schema(description = "약속 코드", example = "985a61f6f636")
            @RequestParam @NotBlank(message = "약속 코드는 공백이 될 수 없습니다.") String appointmentCode,
            @AuthenticationPrincipal UserInfo userInfo) {

        AppointmentInfoDetailRespDto respDto = appointmentService.retrieveAppointmentInfoDetail(appointmentCode, userInfo.getEmail());

        return ResponseEntity.ok(
                appointmentVoMapper.to(respDto)
        );
    }

    @GetMapping("/participation")
    @Operation(summary = "회원의 약속 참여 정보 조회 요청", description = "회원이 약속 참여 정보를 조회할 때 사용됩니다.")
    public ResponseEntity<ListResponse<AppointmentParticipationInfoResp>> retrieveAppointmentParticipationInfo(
            @Schema(description = "약속 코드", example = "985a61f6f636")
            @RequestParam @NotBlank(message = "약속 코드는 공백이 될 수 없습니다.") String appointmentCode,
            @AuthenticationPrincipal UserInfo userInfo) {

        List<AppointmentParticipationInfoRespDto> respDtoList = appointmentService
                .retrieveAppointmentParticipationInfo(appointmentCode, userInfo.getEmail());

        return ResponseEntity.ok(
                ListResponse.of(
                        respDtoList.stream().map(appointmentVoMapper::to).toList()
                )
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

    @DeleteMapping("/participation")
    @Operation(summary = "회원 약속 참여 삭제 요청", description = "회원이 약속 참여를 삭제할 때 사용됩니다.")
    public ResponseEntity<BooleanResp> deleteAppointmentParticipation(
            @Schema(description = "약속 코드", example = "985a61f6f636")
            @RequestParam @NotBlank(message = "약속 코드는 공백이 될 수 없습니다.") String appointmentCode,
            @AuthenticationPrincipal UserInfo userInfo) {

        appointmentService.deleteAppointmentParticipation(appointmentCode, userInfo.getEmail(), userInfo.getId());

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

    @PutMapping("/participation")
    @Operation(summary = "회원 약속 참여 변경 요청", description = "회원이 약속 참여를 변경할 때 사용됩니다.")
    public ResponseEntity<BooleanResp> updateAppointmentParticipation(
            @Valid @RequestBody UpdateAppointmentParticipationReq req,
            @AuthenticationPrincipal UserInfo userInfo) {

        UpdateAppointmentParticipationReqDto reqDto = appointmentVoMapper.to(req);

        appointmentService.updateAppointmentParticipation(reqDto, userInfo.getEmail(), userInfo.getId());

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

    @GetMapping("/candidate-date:confirm")
    @Operation(summary = "약속 확정 후보 일자 정보 조회", description = "약속 확정 전, 약속 후보 날짜 정보를 조회합니다.")
    public ResponseEntity<ListResponse<AppointmentCandidateDateInfoResp>> retrieveCandidateDateInfo(
            @RequestParam @NotBlank(message = "약속 코드는 공백이 될 수 없습니다.") String appointmentCode,
            @AuthenticationPrincipal UserInfo userInfo
    ) {
        List<AppointmentCandidateDateInfoRespDto> respDtos
                = appointmentService.retrieveAppointmentCandidateDate(appointmentCode, userInfo.getEmail());

        return ResponseEntity.ok(
                ListResponse.of(
                        respDtos.stream().map(appointmentVoMapper::to).toList()
                )
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
