package com.example.gatherplan.controller;

import com.example.gatherplan.appointment.dto.*;
import com.example.gatherplan.appointment.service.TempAppointmentService;
import com.example.gatherplan.controller.mapper.TempAppointmentVoMapper;
import com.example.gatherplan.controller.validation.RequestValidationSeq;
import com.example.gatherplan.controller.vo.appointment.*;
import com.example.gatherplan.controller.vo.common.BooleanResp;
import com.example.gatherplan.controller.vo.common.ListResponse;
import com.example.gatherplan.controller.vo.tempappointment.*;
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
    @Operation(summary = "비회원 약속 만들기 요청", description = "비회원이 새로운 약속을 생성할 때 사용됩니다.")
    public ResponseEntity<CreateTempAppointmentResp> registerAppointment(
            @Valid @RequestBody CreateTempAppointmentReq req) {

        CreateTempAppointmentReqDto reqDto = tempAppointmentVoMapper.to(req);
        String appointmentCode = tempAppointmentService.registerTempAppointment(reqDto);

        return ResponseEntity.ok(
                CreateTempAppointmentResp.of(appointmentCode)
        );
    }

    @GetMapping("/preview")
    @Operation(summary = "비회원 약속 정보 미리보기 조회 요청", description = "비회원이 약속 정보 미리보기 조회할 때 사용됩니다.")
    public ResponseEntity<TempAppointmentInfoResp> retrieveAppointmentInfo(
            @Valid @ModelAttribute @ParameterObject TempAppointmentInfoReq req) {

        TempAppointmentInfoReqDto reqDto = tempAppointmentVoMapper.to(req);
        TempAppointmentInfoRespDto respDto = tempAppointmentService.retrieveAppointmentInfo(reqDto);

        return ResponseEntity.ok(
                tempAppointmentVoMapper.to(respDto)
        );
    }

    @PostMapping("/join:validate")
    @Operation(summary = "비회원 임시 회원가입 가능 여부 확인", description = "지정 약속에 비회원으로 임시 가입이 가능한지 확인합니다.")
    public ResponseEntity<BooleanResp> validJoinUser(
            @Valid @RequestBody CreateTempUserReq req) {

        CreateTempUserReqDto reqDto = tempAppointmentVoMapper.to(req);
        boolean isValid = tempAppointmentService.validJoinTempUser(reqDto);

        return ResponseEntity.ok(
                BooleanResp.of(isValid)
        );
    }

    @PostMapping("/login")
    @Operation(summary = "비회원 호스트의 로그인", description = "비회원 호스트의 로그인 정보를 확인합니다.")
    public ResponseEntity<BooleanResp> login(@RequestBody @Valid TempUserLoginReq req) {

        TempUserLoginReqDto reqDto = tempAppointmentVoMapper.to(req);
        boolean isValid = tempAppointmentService.login(reqDto);

        return ResponseEntity.ok(
                BooleanResp.of(isValid)
        );
    }

    @GetMapping("/participation-status")
    @Operation(summary = "비회원 약속 참여 여부 조회", description = "비회원의 약속 참여 여부를 조회할 때 사용됩니다.")
    public ResponseEntity<BooleanResp> retrieveParticipationStatus(
            @Valid @ModelAttribute @ParameterObject TempAppointmentParticipationStatusReq req) {

        TempAppointmentParticipationStatusReqDto reqDto = tempAppointmentVoMapper.to(req);
        boolean isValid = tempAppointmentService.retrieveParticipationStatus(reqDto);

        return ResponseEntity.ok(
                BooleanResp.of(isValid)
        );
    }

    @PostMapping("/participation")
    @Operation(summary = "비회원 약속 참여 등록", description = "비회원의 약속 참여 정보를 등록합니다.")
    public ResponseEntity<BooleanResp> registerAppointmentParticipation(
            @Valid @RequestBody CreateTempAppointmentParticipationReq req
    ) {
        CreateTempAppointmentParticipationReqDto reqDto = tempAppointmentVoMapper.to(req);
        tempAppointmentService.registerAppointmentParticipation(reqDto);

        return ResponseEntity.ok(
                BooleanResp.success()
        );
    }

    @GetMapping
    @Operation(summary = "비회원 약속 정보 조회", description = "비회원이 약속 정보를 조회할 때 사용됩니다.")
    public ResponseEntity<TempAppointmentInfoDetailResp> retrieveAppointmentInfoDetail(
            @Valid @ModelAttribute @ParameterObject TempAppointmentInfoDetailReq req) {

        TempAppointmentInfoDetailReqDto reqDto = tempAppointmentVoMapper.to(req);
        TempAppointmentInfoDetailRespDto respDto = tempAppointmentService.retrieveAppointmentInfoDetail(reqDto);

        return ResponseEntity.ok(
                tempAppointmentVoMapper.to(respDto)
        );
    }

    @GetMapping("/participation")
    @Operation(summary = "비회원의 약속 참여 정보 조회 요청", description = "비회원이 약속 참여 정보를 조회할 때 사용됩니다.")
    public ResponseEntity<ListResponse<TempAppointmentParticipationInfoResp>> retrieveAppointmentParticipationInfo(
            @Valid @ModelAttribute @ParameterObject TempAppointmentParticipationInfoReq req) {

        TempAppointmentParticipationInfoReqDto reqDto = tempAppointmentVoMapper.to(req);
        List<TempAppointmentParticipationInfoRespDto> respDtoList = tempAppointmentService.retrieveAppointmentParticipationInfo(reqDto);

        return ResponseEntity.ok(
                ListResponse.of(
                        respDtoList.stream().map(tempAppointmentVoMapper::to).toList()
                )
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

    @DeleteMapping("/participation")
    @Operation(summary = "비회원 약속 참여 삭제 요청", description = "비회원이 약속 참여를 삭제할 때 사용됩니다.")
    public ResponseEntity<BooleanResp> deleteAppointmentParticipation(
            @Valid @ModelAttribute @ParameterObject DeleteTempAppointmentParticipationReq req) {

        DeleteTempAppointmentParticipationReqDto reqDto = tempAppointmentVoMapper.to(req);
        tempAppointmentService.deleteAppointmentParticipation(reqDto);

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

    @PutMapping("/participation")
    @Operation(summary = "비회원의 약속 참여 변경 요청", description = "비회원이 약속 참여를 변경할 때 사용됩니다.")
    public ResponseEntity<BooleanResp> updateAppointmentParticipation(
            @Valid @RequestBody UpdateTempAppointmentParticipationReq req) {

        UpdateTempAppointmentParticipationReqDto reqDto = tempAppointmentVoMapper.to(req);
        tempAppointmentService.updateAppointmentParticipation(reqDto);

        return ResponseEntity.ok(
                BooleanResp.success()
        );
    }

    @GetMapping("/candidate-date:confirm")
    @Operation(summary = "약속 확정 후보 일자 정보 조회", description = "약속 확정 전, 약속 후보 날짜 정보를 조회합니다.")
    public ResponseEntity<ListResponse<TempAppointmentCandidateDateInfoResp>> retrieveCandidateDateInfo(
            @Valid @ModelAttribute @ParameterObject TempAppointmentCandidateDateInfoReq req) {
        TempAppointmentCandidateDateInfoReqDto reqDto = tempAppointmentVoMapper.to(req);

        List<TempAppointmentCandidateDateInfoRespDto> respDtos
                = tempAppointmentService.retrieveAppointmentCandidateDate(reqDto);

        return ResponseEntity.ok(
                ListResponse.of(
                        respDtos.stream().map(tempAppointmentVoMapper::to).toList()
                )
        );
    }

    @PostMapping(":confirm")
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
