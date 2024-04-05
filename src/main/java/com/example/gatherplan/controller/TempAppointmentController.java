package com.example.gatherplan.controller;

import com.example.gatherplan.appointment.dto.CreateTempAppointmentReqDto;
import com.example.gatherplan.appointment.dto.DeleteTempAppointmentReqDto;
import com.example.gatherplan.appointment.dto.TempAppointmentInfoReqDto;
import com.example.gatherplan.appointment.dto.TempAppointmentParticipationInfoReqDto;
import com.example.gatherplan.appointment.service.TempAppointmentService;
import com.example.gatherplan.controller.mapper.TempAppointmentVoMapper;
import com.example.gatherplan.controller.validation.CreateTempAppointmentReqValidSeq;
import com.example.gatherplan.controller.vo.appointment.*;
import com.example.gatherplan.controller.vo.common.BooleanResp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/temporary/appointments")
@Tag(name = "약속", description = "임시 회원의 약속 관련된 기능을 제공합니다.")
@Validated
public class TempAppointmentController {

    private final TempAppointmentVoMapper tempAppointmentVoMapper;
    private final TempAppointmentService tempAppointmentService;

    @PostMapping
    @Operation(summary = "임시 회원의 약속 만들기 요청", description = "임시 회원이 새로운 약속을 생성할 때 사용됩니다.")
    public ResponseEntity<CreateTempAppointmentResp> registerAppointment(
            @Validated(value = CreateTempAppointmentReqValidSeq.class)
            @RequestBody CreateTempAppointmentReq req) {

        CreateTempAppointmentReqDto reqDto = tempAppointmentVoMapper.to(req);
        String appointmentCode = tempAppointmentService.registerTempAppointment(reqDto);

        return ResponseEntity.ok(
                CreateTempAppointmentResp.builder()
                        .appointmentCode(appointmentCode)
                        .build()
        );
    }

    @GetMapping
    @Operation(summary = "비회원의 약속 정보 조회 요청", description = "비회원이 약속 정보를 조회할 때 사용됩니다.")
    public ResponseEntity<TempAppointmentInfoResp> retrieveAppointmentInfo(
            @ModelAttribute @ParameterObject @Valid TempAppointmentInfoReq req) {

        TempAppointmentInfoReqDto reqDto = tempAppointmentVoMapper.to(req);

        return ResponseEntity.ok(
                tempAppointmentVoMapper.to(tempAppointmentService.retrieveAppointmentInfo(reqDto))
        );
    }

    @GetMapping("/participation")
    @Operation(summary = "비회원의 약속 참여 정보 조회 요청", description = "비회원이 약속 참여 정보를 조회할 때 사용됩니다.")
    public ResponseEntity<TempAppointmentParticipationInfoResp> retrieveAppointmentParticipationInfo(
            @ModelAttribute @ParameterObject @Valid TempAppointmentParticipationInfoReq req) {

        TempAppointmentParticipationInfoReqDto reqDto = tempAppointmentVoMapper.to(req);

        return ResponseEntity.ok(
                tempAppointmentVoMapper
                        .to(tempAppointmentService.retrieveAppointmentParticipationInfo(reqDto))
        );
    }

    @DeleteMapping
    @Operation(summary = "비회원의 약속 삭제 요청", description = "비회원이 약속을 삭제할 때 사용됩니다.")
    public ResponseEntity<BooleanResp> deleteAppointment(
            @ModelAttribute @ParameterObject @Valid DeleteTempAppointmentReq req) {

        DeleteTempAppointmentReqDto reqDto = tempAppointmentVoMapper.to(req);
        tempAppointmentService.deleteAppointment(reqDto);

        return ResponseEntity.ok(
                BooleanResp.success()
        );
    }


}
