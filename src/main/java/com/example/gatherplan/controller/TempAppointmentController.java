package com.example.gatherplan.controller;

import com.example.gatherplan.appointment.dto.*;
import com.example.gatherplan.appointment.service.TempAppointmentService;
import com.example.gatherplan.controller.mapper.TempAppointmentVoMapper;
import com.example.gatherplan.controller.vo.common.BooleanResp;
import com.example.gatherplan.controller.vo.common.ListResponse;
import com.example.gatherplan.controller.vo.tempappointment.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/temporary/appointments")
@Tag(name = "약속 with 비회원", description = "비회원의 약속 관련된 기능을 제공합니다.")
public class TempAppointmentController {

    private final TempAppointmentService tempAppointmentService;
    private final TempAppointmentVoMapper tempAppointmentVoMapper;


    @PostMapping
    @Operation(summary = "비회원의 약속 만들기 요청", description = "비회원이 새로운 약속을 생성할 때 사용됩니다. [figma #8]")
    public ResponseEntity<CreateTempAppointmentResp> registerAppointment(
            @Valid @RequestBody CreateTempAppointmentReq req) {

        CreateTempAppointmentReqDto reqDto = tempAppointmentVoMapper.to(req);
        String appointmentCode = tempAppointmentService.registerAppointment(reqDto);

        return ResponseEntity.ok(
                CreateTempAppointmentResp.of(appointmentCode)
        );
    }

    @GetMapping
    @Operation(summary = "비회원의 약속 정보 조회 요청", description = "비회원이 약속 정보를 조회할 때 사용됩니다. [figma #23,#40]")
    public ResponseEntity<TempAppointmentInfoResp> retrieveAppointmentInfo(
            @Valid @ModelAttribute @ParameterObject TempAppointmentInfoReq req) {

        TempAppointmentInfoReqDto reqDto = tempAppointmentVoMapper.to(req);
        TempAppointmentInfoRespDto respDto = tempAppointmentService.retrieveAppointmentInfo(reqDto);

        return ResponseEntity.ok(
                tempAppointmentVoMapper.to(respDto)
        );
    }

    @PutMapping
    @Operation(summary = "비회원의 약속 변경 요청", description = "비회원이 약속을 변경할 때 사용됩니다. [figma #35]")
    public ResponseEntity<BooleanResp> updateAppointment(
            @Valid @RequestBody UpdateTempAppointmentReq req) {

        UpdateTempAppointmentReqDto reqDto = tempAppointmentVoMapper.to(req);
        tempAppointmentService.updateAppointment(reqDto);

        return ResponseEntity.ok(
                BooleanResp.success()
        );
    }

    @DeleteMapping
    @Operation(summary = "비회원의 약속 삭제 요청", description = "비회원이 약속을 삭제할 때 사용됩니다. [figma #25]")
    public ResponseEntity<BooleanResp> deleteAppointment(
            @Valid @ModelAttribute @ParameterObject DeleteTempAppointmentReq req) {

        DeleteTempAppointmentReqDto reqDto = tempAppointmentVoMapper.to(req);
        tempAppointmentService.deleteAppointment(reqDto);

        return ResponseEntity.ok(
                BooleanResp.success()
        );
    }

    @PostMapping("/join")
    @Operation(summary = "비회원의 약속 참여 요청", description = "비회원이 약속에 참여할 때 사용됩니다. [figma #15]")
    public ResponseEntity<BooleanResp> registerAppointmentJoin(
            @Valid @RequestBody CreateTempAppointmentParticipationReq req) {

        CreateTempAppointmentJoinReqDto reqDto = tempAppointmentVoMapper.to(req);
        tempAppointmentService.registerAppointmentJoin(reqDto);

        return ResponseEntity.ok(
                BooleanResp.success()
        );
    }

    @GetMapping("/participants")
    @Operation(summary = "비회원의 지정 약속의 모든 참여 정보 조회 요청", description = "비회원이 지정 약속의 모든 참여 정보를 조회할 때 사용됩니다.  [figma #24,#29,#41]")
    public ResponseEntity<ListResponse<TempAppointmentParticipantsResp>> retrieveAppointmentParticipants(
            @Valid @ModelAttribute @ParameterObject TempAppointmentParticipantsReq req) {

        TempAppointmentParticipantsReqDto reqDto = tempAppointmentVoMapper.to(req);
        List<TempAppointmentParticipantsRespDto> respDtoList = tempAppointmentService.retrieveAppointmentParticipants(reqDto);

        return ResponseEntity.ok(
                ListResponse.of(
                        respDtoList.stream().map(tempAppointmentVoMapper::to).toList()
                )
        );
    }

    @GetMapping("/participants/my")
    @Operation(summary = "비회원의 지정 약속의 나의 참여 정보 조회 요청", description = "비회원이 지정 약속의 자신의 참여 정보를 조회할 때 사용됩니다. [figma #29]")
    public ResponseEntity<TempAppointmentMyParticipantResp> retrieveAppointmentMyParticipant(
            @Valid @ModelAttribute @ParameterObject TempAppointmentMyParticipantReq req) {

        TempAppointmentMyParticipantReqDto reqDto = tempAppointmentVoMapper.to(req);
        TempAppointmentMyParticipantRespDto respDto =
                tempAppointmentService.retrieveAppointmentMyParticipant(reqDto);

        return ResponseEntity.ok(
                tempAppointmentVoMapper.to(respDto)
        );
    }

    @PutMapping("/join")
    @Operation(summary = "비회원의 약속 참여 변경 요청", description = "비회원이 약속 참여를 변경할 때 사용됩니다. [figma #30]")
    public ResponseEntity<BooleanResp> updateAppointmentJoin(
            @Valid @RequestBody UpdateTempAppointmentJoinReq req) {

        UpdateTempAppointmentJoinReqDto reqDto = tempAppointmentVoMapper.to(req);
        tempAppointmentService.updateAppointmentJoin(reqDto);

        return ResponseEntity.ok(
                BooleanResp.success()
        );
    }

    @DeleteMapping("/join")
    @Operation(summary = "비회원 약속 참여 삭제 요청", description = "비회원이 약속 참여를 삭제할 때 사용됩니다. [figma #28]")
    public ResponseEntity<BooleanResp> deleteAppointmentJoin(
            @Valid @ModelAttribute @ParameterObject DeleteTempAppointmentJoinReq req) {

        DeleteTempAppointmentJoinReqDto reqDto = tempAppointmentVoMapper.to(req);
        tempAppointmentService.deleteAppointmentJoin(reqDto);

        return ResponseEntity.ok(
                BooleanResp.success()
        );
    }

    @GetMapping("/candidates")
    @Operation(summary = "비회원의 약속 확정 후보 날짜 정보 조회 요청", description = "회원이 약속 확정 후보 날짜 정보를 조회할 때 사용됩니다. [figma #37]")
    public ResponseEntity<ListResponse<TempAppointmentCandidateInfoResp>> retrieveCandidateInfo(
            @Valid @ModelAttribute @ParameterObject TempAppointmentCandidateDatesReq req) {
        TempAppointmentCandidateInfoReqDto reqDto = tempAppointmentVoMapper.to(req);

        List<TempAppointmentCandidateInfoRespDto> respDtos
                = tempAppointmentService.retrieveCandidateInfo(reqDto);

        return ResponseEntity.ok(
                ListResponse.of(
                        respDtos.stream().map(tempAppointmentVoMapper::to).toList()
                )
        );
    }

    @PostMapping("/candidates:confirm")
    @Operation(summary = "비회원의 약속 확정 요청", description = "비회원이 약속 시간을 확정할 떄 사용됩니다. [figma #38]")
    public ResponseEntity<BooleanResp> confirmAppointment(
            @Valid @RequestBody TempConfirmAppointmentReq req) {

        TempConfirmAppointmentReqDto reqDto = tempAppointmentVoMapper.to(req);
        tempAppointmentService.confirmAppointment(reqDto);

        return ResponseEntity.ok(
                BooleanResp.success()
        );
    }

    @GetMapping("/host:check")
    @Operation(summary = "비회원의 호스트의 여부 조회 요청", description = "비회원의 호스트 여부를 판단할 때 사용됩니다. [figma #19,#27]")
    public ResponseEntity<BooleanResp> checkHost(
            @Valid @ModelAttribute @ParameterObject TempCheckHostReq req) {

        TempCheckHostReqDto reqDto = tempAppointmentVoMapper.to(req);
        boolean isValid = tempAppointmentService.checkHost(reqDto);

        return ResponseEntity.ok(
                BooleanResp.of(isValid)
        );
    }

    @GetMapping("/join:check")
    @Operation(summary = "비회원의 약속 참여 여부 조회", description = "비회원의 약속 참여 여부를 조회할 때 사용됩니다. [figma #19,#27]")
    public ResponseEntity<BooleanResp> checkJoin(
            @Valid @ModelAttribute @ParameterObject TempCheckJoinReq req) {

        TempCheckJoinReqDto reqDto = tempAppointmentVoMapper.to(req);
        boolean isValid = tempAppointmentService.checkJoin(reqDto);

        return ResponseEntity.ok(
                BooleanResp.of(isValid)
        );
    }

    @GetMapping("/user:check")
    @Operation(summary = "비회원의 약속 포함 여부 조회", description = "비회원의 특정 약속에 포함 여부를 조회할 때 사용됩니다. [figma #27]")
    public ResponseEntity<BooleanResp> checkUser(
            @Valid @ModelAttribute @ParameterObject TempCheckUserReq req) {

        TempCheckJoinReqDto reqDto = tempAppointmentVoMapper.to(req);
        boolean isValid = tempAppointmentService.checkUser(reqDto);

        return ResponseEntity.ok(
                BooleanResp.of(isValid)
        );
    }

    @GetMapping("/join:valid")
    @Operation(summary = "비회원의 임시 회원가입 가능 여부 확인 요청", description = "지정 약속에 비회원으로 가입 및 참여가 가능한지 확인합니다. [figma #19]")
    public ResponseEntity<BooleanResp> validJoin(
            @Valid @ModelAttribute @ParameterObject CreateTempUserReq req) {

        CreateTempUserReqDto reqDto = tempAppointmentVoMapper.to(req);
        boolean isValid = tempAppointmentService.validJoin(reqDto);

        return ResponseEntity.ok(
                BooleanResp.of(isValid)
        );
    }
}
