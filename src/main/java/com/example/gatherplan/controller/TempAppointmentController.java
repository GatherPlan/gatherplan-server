package com.example.gatherplan.controller;

import com.example.gatherplan.appointment.dto.*;
import com.example.gatherplan.appointment.service.TempAppointmentService;
import com.example.gatherplan.controller.mapper.TempAppointmentVoMapper;
import com.example.gatherplan.controller.validation.RequestValidationSeq;
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

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/temporary/appointments")
@Tag(name = "약속 with 비회원", description = "비회원의 약속 관련된 기능을 제공합니다.")
@Validated(value = RequestValidationSeq.class)
public class TempAppointmentController {

    private final TempAppointmentVoMapper tempAppointmentVoMapper;
    private final TempAppointmentService tempAppointmentService;

    @PostMapping
    @Operation(summary = "비회원의 약속 만들기 요청", description = "비회원이 새로운 약속을 생성할 때 사용됩니다.")
    public ResponseEntity<CreateTempAppointmentResp> registerAppointment(
            @Valid @RequestBody CreateTempAppointmentReq req) {

        CreateTempAppointmentReqDto reqDto = tempAppointmentVoMapper.to(req);
        String appointmentCode = tempAppointmentService.registerTempAppointment(reqDto);

        return ResponseEntity.ok(
                CreateTempAppointmentResp.of(appointmentCode)
        );
    }

    @GetMapping
    @Operation(summary = "비회원의 약속 정보 조회 요청", description = "비회원이 약속 정보를 조회할 때 사용됩니다.")
    public ResponseEntity<TempAppointmentInfoResp> retrieveAppointmentInfo(
            @Valid @ModelAttribute @ParameterObject TempAppointmentInfoReq req) {

        TempAppointmentInfoReqDto reqDto = tempAppointmentVoMapper.to(req);
        TempAppointmentInfoRespDto respDto = tempAppointmentService.retrieveAppointmentInfo(reqDto);

        return ResponseEntity.ok(
                tempAppointmentVoMapper.to(respDto)
        );
    }

    @GetMapping("/participation")
    @Operation(summary = "비회원의 약속 참여 정보 조회 요청", description = "비회원이 약속 참여 정보를 조회할 때 사용됩니다.")
    public ResponseEntity<TempAppointmentParticipationInfoResp> retrieveAppointmentParticipationInfo(
            @Valid @ModelAttribute @ParameterObject TempAppointmentParticipationInfoReq req) {

        TempAppointmentParticipationInfoReqDto reqDto = tempAppointmentVoMapper.to(req);
        TempAppointmentParticipationInfoRespDto respDto = tempAppointmentService.retrieveAppointmentParticipationInfo(reqDto);

        return ResponseEntity.ok(
                tempAppointmentVoMapper.to(respDto)
        );
    }

    @PostMapping("/participation")
    @Operation(summary = "비회원의 약속 참여 등록", description = "회원의 약속 참여 정보를 등록합니다.")
    public ResponseEntity<BooleanResp> registerAppointmentParticipation(
            @Valid @RequestBody CreateTempAppointmentParticipationReq req
    ) {
        CreateTempAppointmentParticipationReqDto reqDto = tempAppointmentVoMapper.to(req);
        tempAppointmentService.registerAppointmentParticipation(reqDto);

        return ResponseEntity.ok(
                BooleanResp.success()
        );
    }

    @DeleteMapping
    @Operation(summary = "비회원의 약속 삭제 요청", description = "비회원이 약속을 삭제할 때 사용됩니다.")
    public ResponseEntity<BooleanResp> deleteAppointment(
            @Valid @ModelAttribute @ParameterObject DeleteTempAppointmentReq req) {

        DeleteTempAppointmentReqDto reqDto = tempAppointmentVoMapper.to(req);
        tempAppointmentService.deleteAppointment(reqDto);

        return ResponseEntity.ok(
                BooleanResp.success()
        );
    }

    @PutMapping
    @Operation(summary = "비회원의 약속 변경 요청", description = "비회원이 약속을 변경할 때 사용됩니다.")
    public ResponseEntity<BooleanResp> updateAppointment(
            @Valid @RequestBody UpdateTempAppointmentReq req) {

        UpdateTempAppointmentReqDto reqDto = tempAppointmentVoMapper.to(req);
        tempAppointmentService.updateAppointment(reqDto);

        return ResponseEntity.ok(
                BooleanResp.success()
        );
    }

    @GetMapping("/fix")
    @Operation(summary = "비회원의 약속 확정 시간에 참여 가능한 사용자 조회", description = "비회원이 선택된 약속 확정 시간에 참여할 " +
            "수 있는 사용자 목록을 조회합니다.")
    public ResponseEntity<TempConfirmedAppointmentParticipantsResp> retrieveEligibleParticipantsList(
            @Valid @ModelAttribute @ParameterObject TempConfirmedAppointmentParticipantsReq req) {

        TempConfirmedAppointmentParticipantsReqDto reqDto = tempAppointmentVoMapper.to(req);
        List<String> nicknameList =
                tempAppointmentService.retrieveEligibleParticipantsList(reqDto);

        return ResponseEntity.ok(
                TempConfirmedAppointmentParticipantsResp.of(nicknameList)
        );
    }

    @PostMapping("/fix")
    @Operation(summary = "비회원의 약속 확정 요청", description = "비회원이 약속 확정 시간을 정할때 사용됩니다.")
    public ResponseEntity<BooleanResp> confirmedAppointment(
            @Valid @RequestBody TempConfirmedAppointmentReq req) {

        TempConfirmedAppointmentReqDto reqDto = tempAppointmentVoMapper.to(req);
        tempAppointmentService.confirmedAppointment(reqDto);

        return ResponseEntity.ok(
                BooleanResp.success()
        );
    }

}
