package com.example.gatherplan.controller;

import com.example.gatherplan.appointment.dto.CreateAppointmentReqDto;
import com.example.gatherplan.appointment.dto.CreateTempAppointmentReqDto;
import com.example.gatherplan.appointment.dto.MemberInfoReqDto;
import com.example.gatherplan.appointment.mapper.AppointmentMapper;
import com.example.gatherplan.appointment.service.AppointmentService;
import com.example.gatherplan.common.jwt.CustomUserDetails;
import com.example.gatherplan.controller.validation.RequestValidationSequence;
import com.example.gatherplan.controller.vo.appointment.req.CreateAppointmentReq;
import com.example.gatherplan.controller.vo.appointment.req.CreateTempAppointmentReq;
import com.example.gatherplan.controller.vo.common.BooleanResp;
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
    public ResponseEntity<BooleanResp> registerAppointment(
            @Validated(value = RequestValidationSequence.class)
            @RequestBody CreateAppointmentReq createAppointmentReq,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        CreateAppointmentReqDto createAppointmentReqDto = appointmentMapper.to(createAppointmentReq);
        MemberInfoReqDto memberInfoReqDto = appointmentMapper.to(customUserDetails);

        appointmentService.registerAppointment(createAppointmentReqDto, memberInfoReqDto);

        return ResponseEntity.ok(
                BooleanResp.of(true)
        );
    }

    @PostMapping("/temporary")
    @Operation(summary = "임시 회원의 약속 만들기 요청", description = "임시 회원이 새로운 약속을 생성할 때 사용됩니다.")
    public ResponseEntity<BooleanResp> registerAppointment(
            @Validated(value = RequestValidationSequence.class)
            @RequestBody CreateTempAppointmentReq createTempAppointmentReq) {

        CreateTempAppointmentReqDto createTempAppointmentReqDto = appointmentMapper.to(createTempAppointmentReq);
        appointmentService.registerTempAppointment(createTempAppointmentReqDto);

        return ResponseEntity.ok(
                BooleanResp.of(true)
        );
    }


}
