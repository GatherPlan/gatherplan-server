package com.example.gatherplan.controller;

import com.example.gatherplan.appointment.dto.AppointmentFormDto;
import com.example.gatherplan.appointment.mapper.AppointmentMapper;
import com.example.gatherplan.appointment.service.AppointmentService;
import com.example.gatherplan.common.vo.response.BooleanResp;
import com.example.gatherplan.controller.validation.RequestValidationSequence;
import com.example.gatherplan.controller.vo.appointment.AppointmentFormReq;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/appointment")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping("/set-information")
    public ResponseEntity<BooleanResp> setInformation(
            @Validated(value = RequestValidationSequence.class)
            @RequestBody AppointmentFormReq appointmentFormReq, HttpServletRequest httpServletRequest) {

        AppointmentFormDto appointmentFormDto = Mappers.getMapper(AppointmentMapper.class)
                .toAppointmentFormDto(appointmentFormReq);
        appointmentService.setInformation(appointmentFormDto, httpServletRequest);

        return ResponseEntity.ok(
                BooleanResp.of(true)
        );
    }


}
