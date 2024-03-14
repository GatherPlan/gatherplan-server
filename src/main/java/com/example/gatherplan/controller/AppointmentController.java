package com.example.gatherplan.controller;

import com.example.gatherplan.appointment.dto.CreateAppointmentReqDto;
import com.example.gatherplan.appointment.mapper.AppointmentMapper;
import com.example.gatherplan.appointment.service.AppointmentService;
import com.example.gatherplan.common.vo.response.BooleanResp;
import com.example.gatherplan.controller.validation.RequestValidationSequence;
import com.example.gatherplan.controller.vo.appointment.CreateAppointmentReq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final AppointmentMapper appointmentMapper;

    @PostMapping("")
    public ResponseEntity<BooleanResp> registerAppointment(
            @Validated(value = RequestValidationSequence.class)
            @RequestBody CreateAppointmentReq createAppointmentReq) {

        CreateAppointmentReqDto createAppointmentReqDto = appointmentMapper.to(createAppointmentReq);
        appointmentService.registerAppointment(createAppointmentReqDto);

        return ResponseEntity.ok(
                BooleanResp.of(true)
        );
    }


}
