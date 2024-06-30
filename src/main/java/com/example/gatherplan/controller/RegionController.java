package com.example.gatherplan.controller;

import com.example.gatherplan.controller.mapper.RegionVoMapper;
import com.example.gatherplan.controller.vo.common.ListResponse;
import com.example.gatherplan.controller.vo.region.*;
import com.example.gatherplan.region.dto.*;
import com.example.gatherplan.region.service.RegionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/region")
@Tag(name = "부가 서비스", description = "날씨, 상세주소, 행정구역과 관련된 검색 기능을 제공합니다.")
public class RegionController {

    private final RegionService regionService;
    private final RegionVoMapper regionVoMapper;

    @GetMapping("/district")
    @Operation(summary = "행정구역 검색 요청", description = "행정구역을 검색할 때 사용됩니다. [figma #6,#33]")
    public ResponseEntity<ListResponse<DistrictSearchResp>> searchRegion(
            @Valid @ModelAttribute @ParameterObject DistrictSearchReq req) {

        DistrictSearchReqDto reqDto = regionVoMapper.to(req);
        Page<DistrictSearchRespDto> respDtos = regionService.searchRegion(reqDto);

        return ResponseEntity.ok(
                ListResponse.of(
                        respDtos.map(regionVoMapper::to)
                )
        );
    }

    @GetMapping("/place")
    @Operation(summary = "상세주소 검색 요청", description = "상세주소를 검색할 때 사용됩니다. [figma #6,#33]")
    public ResponseEntity<ListResponse<PlaceSearchResp>> searchPlace(
            @Valid @ModelAttribute @ParameterObject PlaceSearchReq req) {

        KeywordPlaceReqDto keywordPlaceReqDto = regionVoMapper.to(req);
        Page<PlaceSearchRespDto> respDtos = regionService.searchKeywordPlace(keywordPlaceReqDto);

        return ResponseEntity.ok(
                ListResponse.of(
                        respDtos.map(regionVoMapper::to))
        );
    }

    @GetMapping("/weather")
    @Operation(summary = "날씨 검색 요청", description = "날씨를 검색할 때 사용됩니다. [figma #7,#14,#34,#37]")
    public ResponseEntity<ListResponse<DailyWeatherResp>> searchWeather(
            @RequestParam @NotBlank(message = "주소는 공백이 될 수 없습니다.") @Size(min = 2, message = "주소는 2자 이상이어야합니다.") String addressName) throws JSONException {

        List<DailyWeatherRespDto> respDtos = regionService.searchDailyWeather(addressName);

        return ResponseEntity.ok(
                ListResponse.of(
                        respDtos.stream().map(regionVoMapper::to).toList())
        );
    }
}
