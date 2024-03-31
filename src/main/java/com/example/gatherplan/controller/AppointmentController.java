package com.example.gatherplan.controller;

import com.example.gatherplan.appointment.dto.*;
import com.example.gatherplan.appointment.service.AppointmentService;
import com.example.gatherplan.common.jwt.UserInfo;
import com.example.gatherplan.controller.mapper.AppointmentVoMapper;
import com.example.gatherplan.controller.validation.CreateAppointmentReqValidSeq;
import com.example.gatherplan.controller.validation.CreateTempAppointmentReqValidSeq;
import com.example.gatherplan.controller.vo.appointment.*;
import com.example.gatherplan.controller.vo.common.ListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
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
    private final AppointmentVoMapper appointmentVoMapper;

    @PostMapping
    @Operation(summary = "회원의 약속 만들기 요청", description = "회원이 새로운 약속을 생성할 때 사용됩니다.")
    public ResponseEntity<CreateAppointmentResp> registerAppointment(
            @Validated(value = CreateAppointmentReqValidSeq.class)
            @RequestBody CreateAppointmentReq createAppointmentReq,
            @AuthenticationPrincipal UserInfo userInfo) {

        CreateAppointmentReqDto createAppointmentReqDto = appointmentVoMapper.to(createAppointmentReq);
        String appointmentCode = appointmentService.registerAppointment(createAppointmentReqDto, userInfo.getEmail());

        return ResponseEntity.ok(
                CreateAppointmentResp.builder()
                        .appointmentCode(appointmentCode)
                        .build()
        );
    }

    @PostMapping("/temporary")
    @Operation(summary = "임시 회원의 약속 만들기 요청", description = "임시 회원이 새로운 약속을 생성할 때 사용됩니다.")
    public ResponseEntity<CreateTempAppointmentResp> registerAppointment(
            @Validated(value = CreateTempAppointmentReqValidSeq.class)
            @RequestBody CreateTempAppointmentReq createTempAppointmentReq) {

        CreateTempAppointmentReqDto createTempAppointmentReqDto = appointmentVoMapper.to(createTempAppointmentReq);
        String appointmentCode = appointmentService.registerTempAppointment(createTempAppointmentReqDto);

        return ResponseEntity.ok(
                CreateTempAppointmentResp.builder()
                        .appointmentCode(appointmentCode)
                        .build()
        );
    }

    @GetMapping("/search/district")
    @Operation(summary = "회원의 행정구역 검색 요청", description = "회원이 행정구역을 검색할 때 사용됩니다.")
    public ResponseEntity<ListResponse<RegionResp>> searchRegion(
            @ModelAttribute @ParameterObject @Valid RegionReq regionReq) {

        RegionReqDto regionReqDto = appointmentVoMapper.to(regionReq);
        List<RegionDto> regionDtos = appointmentService.searchRegion(regionReqDto);

        return ResponseEntity.ok(
                ListResponse.of(
                        regionDtos.stream()
                                .map(appointmentVoMapper::to)
                                .toList()
                )
        );
    }

    @GetMapping("/search/place")
    @Operation(summary = "회원의 상세주소 검색 요청", description = "회원이 상세주소를 검색할 때 사용됩니다.")
    public ResponseEntity<ListResponse<KeywordPlaceResp>> searchPlace(
            @ModelAttribute @ParameterObject @Valid KeywordPlaceReq keywordPlaceReq) {

        KeywordPlaceReqDto keywordPlaceReqDto = appointmentVoMapper.to(keywordPlaceReq);
        List<KeywordPlaceRespDto> keywordPlaceRespDtos = appointmentService.searchKeywordPlace(keywordPlaceReqDto);

        return ResponseEntity.ok(
                ListResponse.of(
                        keywordPlaceRespDtos.stream()
                                .map(appointmentVoMapper::to)
                                .toList())
        );
    }

    @GetMapping("/search/weather")
    @Operation(summary = "회원의 날씨 검색 요청", description = "회원이 날씨를 검색할 때 사용됩니다.")
    public ResponseEntity<ListResponse<DailyWeatherResp>> searchWeather(
            @ModelAttribute @ParameterObject @Valid DailyWeatherReq dailyWeatherReq) throws JSONException {

        DailyWeatherReqDto dailyWeatherReqDto = appointmentVoMapper.to(dailyWeatherReq);
        List<DailyWeatherRespDto> dailyWeatherRespDtos = appointmentService.searchDailyWeather(dailyWeatherReqDto);

        return ResponseEntity.ok(
                ListResponse.of(
                        dailyWeatherRespDtos.stream()
                                .map(appointmentVoMapper::to)
                                .toList())
        );
    }

    @PostMapping("/temp")
    @Operation(summary = "임시회원의 약속 현황보기 요청", description = "임시회원이 약속 현황보기를 요청할 때 사용됩니다.")
    public ResponseEntity<CheckTempAppointmentResp> checkTempAppointment(
            @RequestBody CheckTempAppointmentReq checkTempAppointmentReq,
            HttpServletRequest httpServletRequest) {

        CheckTempAppointmentReqDto checkTempAppointmentReqDto = appointmentVoMapper.to(checkTempAppointmentReq);

        CheckTempAppointmentRespDto checkTempAppointmentRespDto = appointmentService
                .checkTempAppointment(checkTempAppointmentReqDto, httpServletRequest);

        CheckTempAppointmentResp result = appointmentVoMapper.to(checkTempAppointmentRespDto);

        return ResponseEntity.ok(
                result
        );
    }


}
