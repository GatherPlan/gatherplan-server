package com.example.gatherplan.controller;

import com.example.gatherplan.appointment.dto.CheckAppointmentReqDto;
import com.example.gatherplan.appointment.dto.CreateAppointmentReqDto;
import com.example.gatherplan.appointment.service.AppointmentService;
import com.example.gatherplan.common.config.jwt.UserInfo;
import com.example.gatherplan.controller.mapper.AppointmentVoMapper;
import com.example.gatherplan.controller.validation.CreateAppointmentReqValidSeq;
import com.example.gatherplan.controller.vo.appointment.CheckAppointmentReq;
import com.example.gatherplan.controller.vo.appointment.CreateAppointmentReq;
import com.example.gatherplan.controller.vo.appointment.CreateAppointmentResp;
import com.example.gatherplan.controller.vo.common.BooleanResp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


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
    @Operation(summary = "회원의 약속 만들기 요청", description = "회원이 새로운 약속을 생성할 때 사용됩니다.")
    public ResponseEntity<BooleanResp> checkParticipation(
            @ModelAttribute @ParameterObject @Valid CheckAppointmentReq createAppointmentReq,
            @AuthenticationPrincipal UserInfo userInfo) {

        CheckAppointmentReqDto checkAppointmentReqDto = appointmentVoMapper.to(createAppointmentReq);
        appointmentService.checkParticipation(checkAppointmentReqDto, userInfo.getEmail());

        return ResponseEntity.ok(
                BooleanResp.success()
        );
    }

}
