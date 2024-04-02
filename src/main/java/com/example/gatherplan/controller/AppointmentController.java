package com.example.gatherplan.controller;

import com.example.gatherplan.appointment.dto.CheckAppointmentReqDto;
import com.example.gatherplan.appointment.dto.CreateAppointmentReqDto;
import com.example.gatherplan.appointment.dto.GetAppointmentListRespDto;
import com.example.gatherplan.appointment.service.AppointmentService;
import com.example.gatherplan.common.config.jwt.UserInfo;
import com.example.gatherplan.controller.mapper.AppointmentVoMapper;
import com.example.gatherplan.controller.validation.CreateAppointmentReqValidSeq;
import com.example.gatherplan.controller.vo.appointment.CheckAppointmentReq;
import com.example.gatherplan.controller.vo.appointment.CreateAppointmentReq;
import com.example.gatherplan.controller.vo.appointment.CreateAppointmentResp;
import com.example.gatherplan.controller.vo.appointment.GetAppointmentListResp;
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

        CreateAppointmentReqDto createAppointmentReqDto = appointmentVoMapper.to(createAppointmentReq);
        String appointmentCode = appointmentService.registerAppointment(createAppointmentReqDto, userInfo.getEmail());

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

        CheckAppointmentReqDto checkAppointmentReqDto = appointmentVoMapper.to(createAppointmentReq);
        appointmentService.checkParticipation(checkAppointmentReqDto, userInfo.getEmail());

        return ResponseEntity.ok(
                BooleanResp.success()
        );
    }

    @GetMapping("/list")
    @Operation(summary = "회원의 약속 목록 조회 요청", description = "회원이 약속 목록을 조회할 때 사용됩니다.")
    public ResponseEntity<ListResponse<GetAppointmentListResp>> getAppointmentsList(
            @AuthenticationPrincipal UserInfo userInfo) {

        List<GetAppointmentListRespDto> appointmentsList = appointmentService.getAppointmentsList(userInfo.getEmail());

        return ResponseEntity.ok(
                ListResponse.of(
                        appointmentsList.stream().map(appointmentVoMapper::to).toList()
                )
        );
    }

}
