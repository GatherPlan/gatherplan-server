package com.example.gatherplan.controller;

import com.example.gatherplan.appointment.dto.*;
import com.example.gatherplan.appointment.service.RegionService;
import com.example.gatherplan.controller.mapper.RegionVoMapper;
import com.example.gatherplan.controller.vo.appointment.*;
import com.example.gatherplan.controller.vo.common.ListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/region")
@Tag(name = "지역", description = "날씨, 상세주소, 행정구역과 관련된 기능을 제공합니다.")
public class RegionController {

    private final RegionService regionService;
    private final RegionVoMapper regionVoMapper;

    @GetMapping("/search/district")
    @Operation(summary = "회원의 행정구역 검색 요청", description = "회원이 행정구역을 검색할 때 사용됩니다.")
    public ResponseEntity<ListResponse<RegionResp>> searchRegion(
            @ModelAttribute @ParameterObject @Valid RegionReq regionReq) {

        RegionReqDto regionReqDto = regionVoMapper.to(regionReq);
        List<RegionDto> regionDtos = regionService.searchRegion(regionReqDto);

        return ResponseEntity.ok(
                ListResponse.of(
                        regionDtos.stream()
                                .map(regionVoMapper::to)
                                .toList()
                )
        );
    }

    @GetMapping("/search/place")
    @Operation(summary = "회원의 상세주소 검색 요청", description = "회원이 상세주소를 검색할 때 사용됩니다.")
    public ResponseEntity<ListResponse<KeywordPlaceResp>> searchPlace(
            @ModelAttribute @ParameterObject @Valid KeywordPlaceReq keywordPlaceReq) {

        KeywordPlaceReqDto keywordPlaceReqDto = regionVoMapper.to(keywordPlaceReq);
        List<KeywordPlaceRespDto> keywordPlaceRespDtos = regionService.searchKeywordPlace(keywordPlaceReqDto);

        return ResponseEntity.ok(
                ListResponse.of(
                        keywordPlaceRespDtos.stream()
                                .map(regionVoMapper::to)
                                .toList())
        );
    }

    @GetMapping("/search/weather")
    @Operation(summary = "회원의 날씨 검색 요청", description = "회원이 날씨를 검색할 때 사용됩니다.")
    public ResponseEntity<ListResponse<DailyWeatherResp>> searchWeather(
            @ModelAttribute @ParameterObject @Valid DailyWeatherReq dailyWeatherReq) throws JSONException {

        DailyWeatherReqDto dailyWeatherReqDto = regionVoMapper.to(dailyWeatherReq);
        List<DailyWeatherRespDto> dailyWeatherRespDtos = regionService.searchDailyWeather(dailyWeatherReqDto);

        return ResponseEntity.ok(
                ListResponse.of(
                        dailyWeatherRespDtos.stream()
                                .map(regionVoMapper::to)
                                .toList())
        );
    }
}
