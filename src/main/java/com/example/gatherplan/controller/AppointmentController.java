package com.example.gatherplan.controller;

import com.example.gatherplan.appointment.dto.*;
import com.example.gatherplan.appointment.mapper.AppointmentMapper;
import com.example.gatherplan.appointment.service.AppointmentService;
import com.example.gatherplan.common.jwt.CustomUserDetails;
import com.example.gatherplan.controller.validation.RequestValidationSequence;
import com.example.gatherplan.controller.vo.appointment.req.CreateAppointmentReq;
import com.example.gatherplan.controller.vo.appointment.req.CreateTempAppointmentReq;
import com.example.gatherplan.controller.vo.appointment.resp.CreateAppointmentResp;
import com.example.gatherplan.controller.vo.appointment.resp.CreateTempAppointmentResp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/appointments")
@Tag(name = "약속", description = "약속 관련된 기능을 제공합니다.")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final AppointmentMapper appointmentMapper;

    @PostMapping("")
    @Operation(summary = "회원의 약속 만들기 요청", description = "회원이 새로운 약속을 생성할 때 사용됩니다.")
    public ResponseEntity<CreateAppointmentResp> registerAppointment(
            @Validated(value = RequestValidationSequence.class)
            @RequestBody CreateAppointmentReq createAppointmentReq,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        CreateAppointmentReqDto createAppointmentReqDto = appointmentMapper.to(createAppointmentReq);
        MemberInfoReqDto memberInfoReqDto = appointmentMapper.to(customUserDetails);

        CreateAppointmentRespDto createAppointmentRespDto = appointmentService
                .registerAppointment(createAppointmentReqDto, memberInfoReqDto);

        CreateAppointmentResp createAppointmentResp = appointmentMapper.to(createAppointmentRespDto);

        return ResponseEntity.ok(
                createAppointmentResp
        );
    }

    @PostMapping("/temporary")
    @Operation(summary = "임시 회원의 약속 만들기 요청", description = "임시 회원이 새로운 약속을 생성할 때 사용됩니다.")
    public ResponseEntity<CreateTempAppointmentResp> registerAppointment(
            @Validated(value = RequestValidationSequence.class)
            @RequestBody CreateTempAppointmentReq createTempAppointmentReq) {
        CreateTempAppointmentReqDto createTempAppointmentReqDto = appointmentMapper.to(createTempAppointmentReq);

        CreateTempAppointmentRespDto createTempAppointmentRespDto = appointmentService
                .registerTempAppointment(createTempAppointmentReqDto);
        CreateTempAppointmentResp createTempAppointmentResp = appointmentMapper.to(createTempAppointmentRespDto);

        return ResponseEntity.ok(
                createTempAppointmentResp
        );
    }


}
