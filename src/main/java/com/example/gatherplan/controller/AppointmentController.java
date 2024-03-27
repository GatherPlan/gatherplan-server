package com.example.gatherplan.controller;

import com.example.gatherplan.appointment.dto.*;
import com.example.gatherplan.appointment.service.AppointmentService;
import com.example.gatherplan.common.jwt.CustomUserDetails;
import com.example.gatherplan.controller.mapper.AppointmentControllerMapper;
import com.example.gatherplan.controller.validation.RequestValidationSequence;
import com.example.gatherplan.controller.vo.appointment.*;
import com.example.gatherplan.controller.vo.common.BooleanResp;
import com.example.gatherplan.controller.vo.common.ListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/appointments")
@Tag(name = "약속", description = "약속 관련된 기능을 제공합니다.")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final AppointmentControllerMapper appointmentControllerMapper;

    @PostMapping
    @Operation(summary = "회원의 약속 만들기 요청", description = "회원이 새로운 약속을 생성할 때 사용됩니다.")
    public ResponseEntity<BooleanResp> registerAppointment(
            @Validated(value = RequestValidationSequence.class)
            @RequestBody CreateAppointmentReq createAppointmentReq,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        CreateAppointmentReqDto createAppointmentReqDto = appointmentControllerMapper.to(createAppointmentReq);
        appointmentService.registerAppointment(createAppointmentReqDto, customUserDetails.getEmail());

        return ResponseEntity.ok(
                BooleanResp.success()
        );
    }

    @PostMapping("/temporary")
    @Operation(summary = "임시 회원의 약속 만들기 요청", description = "임시 회원이 새로운 약속을 생성할 때 사용됩니다.")
    public ResponseEntity<BooleanResp> registerAppointment(
            @Validated(value = RequestValidationSequence.class)
            @RequestBody CreateTempAppointmentReq createTempAppointmentReq) {

        CreateTempAppointmentReqDto createTempAppointmentReqDto = appointmentControllerMapper.to(createTempAppointmentReq);
        appointmentService.registerTempAppointment(createTempAppointmentReqDto);

        return ResponseEntity.ok(
                BooleanResp.success()
        );
    }

    @GetMapping("/search/district")
    @Operation(summary = "회원의 행정구역 검색 요청", description = "회원이 행정구역을 검색할 때 사용됩니다.")
    public ResponseEntity<ListResponse<SearchDistrictResp>> searchDisctrict(
            @ModelAttribute @ParameterObject @Valid SearchDistrictReq searchDistrictReq) {

        System.out.println(searchDistrictReq.getKeyword());
        SearchDistrictReqDto searchDistrictReqDto = appointmentControllerMapper.to(searchDistrictReq);
        List<SearchDistrictRespDto> searchDistrictRespDtos = appointmentService.searchDisctrict(searchDistrictReqDto);
        List<SearchDistrictResp> result = searchDistrictRespDtos.stream().map(appointmentControllerMapper::to).toList();

        return ResponseEntity.ok(
                ListResponse.of(result)
        );
    }

    @GetMapping("/search/place")
    @Operation(summary = "회원의 상세주소 검색 요청", description = "회원이 상세주소를 검색할 때 사용됩니다.")
    public ResponseEntity<ListResponse<SearchPlaceResp>> searchPlace(
            @ModelAttribute @ParameterObject @Valid SearchPlaceReq searchPlaceReq) {

        SearchPlaceReqDto searchPlaceReqDto = appointmentControllerMapper.to(searchPlaceReq);
        List<SearchPlaceRespDto> searchPlaceRespDtos = appointmentService.searchPlace(searchPlaceReqDto);
        List<SearchPlaceResp> result = searchPlaceRespDtos.stream().map(appointmentControllerMapper::to).toList();

        return ResponseEntity.ok(
                ListResponse.of(result)
        );
    }

    @GetMapping("/search/weather")
    @Operation(summary = "회원의 날씨 검색 요청", description = "회원이 날씨를 검색할 때 사용됩니다.")
    public ResponseEntity<ListResponse<SearchWeatherResp>> searchPlace(
            @ModelAttribute @ParameterObject @Valid SearchWhetherReq searchWhetherReq) throws JSONException {

        SearchWeatherReqDto searchWeatherReqDto = appointmentControllerMapper.to(searchWhetherReq);
        List<SearchWeatherRespDto> searchWeatherRespDtos = appointmentService.searchWhether(searchWeatherReqDto);
        List<SearchWeatherResp> result = searchWeatherRespDtos.stream().map(appointmentControllerMapper::to).toList();
        
        return ResponseEntity.ok(
                ListResponse.of(result)
        );
    }
}
