package com.example.gatherplan.controller;

import com.example.gatherplan.appointment.dto.AppointmentWithHostRespDto;
import com.example.gatherplan.appointment.dto.CreateAppointmentReqDto;
import com.example.gatherplan.appointment.dto.UpdateAppointmentReqDto;
import com.example.gatherplan.appointment.service.AppointmentService;
import com.example.gatherplan.common.config.jwt.UserInfo;
import com.example.gatherplan.controller.mapper.AppointmentVoMapper;
import com.example.gatherplan.controller.validation.CreateAppointmentReqValidSeq;
import com.example.gatherplan.controller.validation.UpdateAppointmentReqValidSeq;
import com.example.gatherplan.controller.vo.appointment.*;
import com.example.gatherplan.controller.vo.common.BooleanResp;
import com.example.gatherplan.controller.vo.common.ListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/appointments")
@Tag(name = "약속", description = "약속 관련된 기능을 제공합니다.")
@Validated
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final AppointmentVoMapper appointmentVoMapper;

    @PostMapping
    @Operation(summary = "회원의 약속 만들기 요청", description = "회원이 새로운 약속을 생성할 때 사용됩니다.")
    public ResponseEntity<CreateAppointmentResp> registerAppointment(
            @Validated(value = CreateAppointmentReqValidSeq.class)
            @RequestBody CreateAppointmentReq req,
            @AuthenticationPrincipal UserInfo userInfo) {

        CreateAppointmentReqDto reqDto = appointmentVoMapper.to(req);
        String appointmentCode = appointmentService.registerAppointment(reqDto, userInfo.getEmail());

        return ResponseEntity.ok(
                CreateAppointmentResp.builder()
                        .appointmentCode(appointmentCode)
                        .build()
        );
    }

    @GetMapping("/participation-status")
    @Operation(summary = "회원의 약속 참여 상태 확인 요청", description = "회원의 약속 참여 상태를 판단할 때 사용됩니다.")
    public ResponseEntity<BooleanResp> retrieveParticipationStatus(
            @Schema(description = "약속 코드", example = "abcd efgh j124")
            @RequestParam @NotBlank(message = "약속 코드는 공백이 될 수 없습니다.") String appointmentCode,
            @AuthenticationPrincipal UserInfo userInfo) {

        return ResponseEntity.ok(
                BooleanResp.of(
                        appointmentService.isUserParticipated(
                                appointmentCode, userInfo.getEmail())
                )
        );
    }

    @GetMapping("/list")
    @Operation(summary = "회원의 약속 목록 조회 요청", description = "회원이 약속 목록을 조회할 때 사용됩니다.")
    public ResponseEntity<ListResponse<AppointmentWithHostResp>> retrieveAppointmentList(
            @AuthenticationPrincipal UserInfo userInfo) {

        List<AppointmentWithHostRespDto> respDtos = appointmentService.retrieveAppointmentList(userInfo.getEmail());

        return ResponseEntity.ok(
                ListResponse.of(
                        respDtos.stream()
                                .map(appointmentVoMapper::to).toList()
                )
        );
    }

    @GetMapping("/list:search")
    @Operation(summary = "회원의 약속 목록 키워드 조회 요청", description = "회원이 약속 목록을 키워드로 조회할 때 사용됩니다.")
    public ResponseEntity<ListResponse<AppointmentWithHostResp>> retrieveAppointmentSearchList(
            @Schema(description = "약속 이름 검색 키워드", example = "세 얼간이")
            @RequestParam @NotBlank(message = "약속 코드는 공백이 될 수 없습니다.") String keyword,
            @AuthenticationPrincipal UserInfo userInfo) {

        List<AppointmentWithHostRespDto> respDtos = appointmentService
                .retrieveAppointmentSearchList(keyword, userInfo.getEmail());

        return ResponseEntity.ok(
                ListResponse.of(
                        respDtos.stream()
                                .map(appointmentVoMapper::to).toList()
                )
        );
    }

    @GetMapping
    @Operation(summary = "회원의 약속 정보 조회 요청", description = "회원이 약속 정보를 조회할 때 사용됩니다.")
    public ResponseEntity<AppointmentInfoResp> retrieveAppointmentInfo(
            @Schema(description = "약속 코드", example = "abcd efgh j124")
            @RequestParam @NotBlank(message = "약속 코드는 공백이 될 수 없습니다.") String appointmentCode,
            @AuthenticationPrincipal UserInfo userInfo) {

        return ResponseEntity.ok(
                appointmentVoMapper.to(appointmentService.retrieveAppointmentInfo(
                        appointmentCode, userInfo.getEmail()))
        );
    }

    @GetMapping("/participation")
    @Operation(summary = "회원의 약속 참여 정보 조회 요청", description = "회원이 약속 참여 정보를 조회할 때 사용됩니다.")
    public ResponseEntity<AppointmentParticipationInfoResp> retrieveAppointmentParticipationInfo(
            @Schema(description = "약속 코드", example = "abcd efgh j124")
            @RequestParam @NotBlank(message = "약속 코드는 공백이 될 수 없습니다.") String appointmentCode,
            @AuthenticationPrincipal UserInfo userInfo) {

        return ResponseEntity.ok(
                appointmentVoMapper.to(appointmentService
                        .retrieveAppointmentParticipationInfo(appointmentCode, userInfo.getEmail()))
        );
    }

    @DeleteMapping
    @Operation(summary = "회원의 약속 삭제 요청", description = "회원이 약속을 삭제할 때 사용됩니다.")
    public ResponseEntity<BooleanResp> deleteAppointment(
            @Schema(description = "약속 코드", example = "abcd efgh j124")
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
            @Validated(value = UpdateAppointmentReqValidSeq.class)
            @RequestBody UpdateAppointmentReq req,
            @AuthenticationPrincipal UserInfo userInfo) {

        UpdateAppointmentReqDto reqDto = appointmentVoMapper.to(req);
        appointmentService.updateAppointment(reqDto, userInfo.getEmail());

        return ResponseEntity.ok(
                BooleanResp.success()
        );
    }


}
