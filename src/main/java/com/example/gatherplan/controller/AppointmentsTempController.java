package com.example.gatherplan.controller;

import com.example.gatherplan.appointment.dto.CreateTempAppointmentReqDto;
import com.example.gatherplan.appointment.service.AppointmentTempService;
import com.example.gatherplan.controller.mapper.AppointmentTempVoMapper;
import com.example.gatherplan.controller.validation.CreateTempAppointmentReqValidSeq;
import com.example.gatherplan.controller.vo.appointment.CreateTempAppointmentReq;
import com.example.gatherplan.controller.vo.appointment.CreateTempAppointmentResp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/appointments:temp")
@Tag(name = "약속", description = "임시 회원의 약속 관련된 기능을 제공합니다.")
public class AppointmentsTempController {

    private final AppointmentTempVoMapper appointmentTempVoMapper;
    private final AppointmentTempService appointmentTempService;

    @PostMapping
    @Operation(summary = "임시 회원의 약속 만들기 요청", description = "임시 회원이 새로운 약속을 생성할 때 사용됩니다.")
    public ResponseEntity<CreateTempAppointmentResp> registerAppointment(
            @Validated(value = CreateTempAppointmentReqValidSeq.class)
            @RequestBody CreateTempAppointmentReq createTempAppointmentReq) {

        CreateTempAppointmentReqDto createTempAppointmentReqDto = appointmentTempVoMapper.to(createTempAppointmentReq);
        String appointmentCode = appointmentTempService.registerTempAppointment(createTempAppointmentReqDto);

        return ResponseEntity.ok(
                CreateTempAppointmentResp.builder()
                        .appointmentCode(appointmentCode)
                        .build()
        );
    }
}
