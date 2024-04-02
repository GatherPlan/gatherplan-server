package com.example.gatherplan.controller;

import com.example.gatherplan.appointment.dto.*;
import com.example.gatherplan.appointment.service.AppointmentService;
import com.example.gatherplan.common.config.jwt.UserInfo;
import com.example.gatherplan.controller.mapper.AppointmentVoMapper;
import com.example.gatherplan.controller.validation.CreateAppointmentReqValidSeq;
import com.example.gatherplan.controller.validation.UpdateAppointmentReqValidSeq;
import com.example.gatherplan.controller.vo.appointment.*;
import com.example.gatherplan.controller.vo.common.BooleanResp;
import com.example.gatherplan.controller.vo.common.ListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@Tag(name = "약속", description = "약속 관련된 기능을 제공합니다.")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final AppointmentVoMapper appointmentVoMapper;

    @PostMapping
    @Operation(summary = "회원의 약속 만들기 요청", description = "회원이 새로운 약속을 생성할 때 사용됩니다.")
    public ResponseEntity<CreateAppointmentResp> registerAppointment(
            @Validated(value = CreateAppointmentReqValidSeq.class)
            @RequestBody CreateAppointmentReq createAppointmentReq,
            @AuthenticationPrincipal UserInfo userInfo) {

        CreateAppointmentReqDto reqDto = appointmentVoMapper.to(createAppointmentReq);
        String appointmentCode = appointmentService.registerAppointment(reqDto, userInfo.getEmail());

        return ResponseEntity.ok(
                CreateAppointmentResp.builder()
                        .appointmentCode(appointmentCode)
                        .build()
        );
    }

    @GetMapping("/check")
    @Operation(summary = "회원의 약속 참여 여부 확인 요청", description = "회원의 약속 참여 여부를 판단할 때 사용됩니다.")
    public ResponseEntity<BooleanResp> checkParticipation(
            @ModelAttribute @ParameterObject @Valid CheckAppointmentReq createAppointmentReq,
            @AuthenticationPrincipal UserInfo userInfo) {

        CheckAppointmentReqDto reqDto = appointmentVoMapper.to(createAppointmentReq);
        appointmentService.checkParticipation(reqDto, userInfo.getEmail());

        return ResponseEntity.ok(
                BooleanResp.success()
        );
    }

    @GetMapping("/list")
    @Operation(summary = "회원의 약속 목록 조회 요청", description = "회원이 약속 목록을 조회할 때 사용됩니다.")
    public ResponseEntity<ListResponse<GetAppointmentListResp>> getAppointmentList(
            @AuthenticationPrincipal UserInfo userInfo) {

        List<GetAppointmentListRespDto> respDtos = appointmentService.getAppointmentList(userInfo.getEmail());

        return ResponseEntity.ok(
                ListResponse.of(respDtos.stream().map(appointmentVoMapper::to).toList())
        );
    }

    @GetMapping("/list/search")
    @Operation(summary = "회원의 약속 목록 키워드 조회 요청", description = "회원이 약속 목록을 키워드로 조회할 때 사용됩니다.")
    public ResponseEntity<ListResponse<GetAppointmentSearchListResp>> getAppointmentSearchList(
            @ModelAttribute @ParameterObject @Valid GetAppointmentSearchListReq getAppointmentSearchListReq,
            @AuthenticationPrincipal UserInfo userInfo) {

        GetAppointmentSearchListReqDto reqDto = appointmentVoMapper
                .to(getAppointmentSearchListReq);

        List<GetAppointmentSearchListRespDto> respDtos = appointmentService
                .getAppointmentSearchList(reqDto, userInfo.getEmail());

        return ResponseEntity.ok(
                ListResponse.of(
                        respDtos.stream().map(appointmentVoMapper::to).toList()
                )
        );
    }

    @GetMapping
    @Operation(summary = "회원의 약속 정보 조회 요청", description = "회원이 약속 정보를 조회할 때 사용됩니다.")
    public ResponseEntity<GetAppointmentInfoResp> getAppointment(
            @ModelAttribute @ParameterObject @Valid GetAppointmentInfoReq getAppointmentInfoReq,
            @AuthenticationPrincipal UserInfo userInfo) {

        GetAppointmentInfoReqDto reqDto = appointmentVoMapper.to(getAppointmentInfoReq);
        GetAppointmentInfoRespDto respDto = appointmentService.getAppointmentInfo(reqDto, userInfo.getEmail());

        return ResponseEntity.ok(
                appointmentVoMapper.to(respDto)
        );
    }

    @GetMapping("/participation")
    @Operation(summary = "회원의 약속 참여 정보 조회 요청", description = "회원이 약속 참여 정보를 조회할 때 사용됩니다.")
    public ResponseEntity<GetAppointmentParticipationInfoResp> getAppointment(
            @ModelAttribute @ParameterObject @Valid GetAppointmentParticipationInfoReq getAppointmentParticipationInfoReq,
            @AuthenticationPrincipal UserInfo userInfo) {

        GetAppointmentParticipationInfoReqDto reqDto = appointmentVoMapper
                .to(getAppointmentParticipationInfoReq);

        GetAppointmentParticipationInfoRespDto respDto = appointmentService
                .getAppointmentParticipationInfo(reqDto, userInfo.getEmail());

        return ResponseEntity.ok(
                appointmentVoMapper.to(respDto)
        );
    }

    @DeleteMapping
    @Operation(summary = "회원의 약속 삭제 요청", description = "회원이 약속을 삭제할 때 사용됩니다.")
    public ResponseEntity<BooleanResp> deleteAppointment(
            @ModelAttribute @ParameterObject @Valid DeleteAppointmentReq deleteAppointmentReq,
            @AuthenticationPrincipal UserInfo userInfo) {

        DeleteAppointmentReqDto reqDto = appointmentVoMapper.to(deleteAppointmentReq);
        appointmentService.deleteAppointment(reqDto, userInfo.getEmail());

        return ResponseEntity.ok(
                BooleanResp.success()
        );
    }

    @PutMapping
    @Operation(summary = "회원의 약속 변경 요청", description = "회원이 약속을 변경할 때 사용됩니다.")
    public ResponseEntity<BooleanResp> updateAppointment(
            @Validated(value = UpdateAppointmentReqValidSeq.class)
            @RequestBody UpdateAppointmentReq updateAppointmentReq,
            @AuthenticationPrincipal UserInfo userInfo) {

        UpdateAppointmentReqDto reqDto = appointmentVoMapper.to(updateAppointmentReq);
        appointmentService.updateAppointment(reqDto, userInfo.getEmail());

        return ResponseEntity.ok(
                BooleanResp.success()
        );
    }


}
