package com.example.gatherplan.controller;

import com.example.gatherplan.controller.mapper.RegionVoMapper;
import com.example.gatherplan.controller.validation.RequestValidationSeq;
import com.example.gatherplan.controller.vo.common.ListResponse;
import com.example.gatherplan.controller.vo.region.DailyWeatherResp;
import com.example.gatherplan.controller.vo.region.KeywordPlaceReq;
import com.example.gatherplan.controller.vo.region.KeywordPlaceResp;
import com.example.gatherplan.controller.vo.region.RegionResp;
import com.example.gatherplan.region.dto.DailyWeatherRespDto;
import com.example.gatherplan.region.dto.KeywordPlaceReqDto;
import com.example.gatherplan.region.dto.KeywordPlaceRespDto;
import com.example.gatherplan.region.dto.RegionDto;
import com.example.gatherplan.region.service.RegionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/region")
@Tag(name = "지역", description = "날씨, 상세주소, 행정구역과 관련된 검색 기능을 제공합니다.")
@Validated(value = RequestValidationSeq.class)
public class RegionController {

    private final RegionService regionService;
    private final RegionVoMapper regionVoMapper;

    @GetMapping("/district")
    @Operation(summary = "회원의 행정구역 검색 요청", description = "회원이 행정구역을 검색할 때 사용됩니다. [figma #6,#33]")
    public ResponseEntity<ListResponse<RegionResp>> searchRegion(
            @RequestParam @NotBlank(message = "키워드는 공백이 될 수 없습니다.") String keyword) {

        List<RegionDto> regionDtos = regionService.searchRegion(keyword);

        return ResponseEntity.ok(
                ListResponse.of(
                        regionDtos.stream().map(regionVoMapper::to).toList()
                )
        );
    }

    @GetMapping("/place")
    @Operation(summary = "회원의 상세주소 검색 요청", description = "회원이 상세주소를 검색할 때 사용됩니다. [figma #6,#33]")
    public ResponseEntity<ListResponse<KeywordPlaceResp>> searchPlace(
            @Valid @ModelAttribute @ParameterObject KeywordPlaceReq req) {

        KeywordPlaceReqDto keywordPlaceReqDto = regionVoMapper.to(req);
        List<KeywordPlaceRespDto> keywordPlaceRespDtos = regionService.searchKeywordPlace(keywordPlaceReqDto);

        return ResponseEntity.ok(
                ListResponse.of(
                        keywordPlaceRespDtos.stream().map(regionVoMapper::to).toList())
        );
    }

    @GetMapping("/weather")
    @Operation(summary = "회원의 날씨 검색 요청", description = "회원이 날씨를 검색할 때 사용됩니다. [figma #7,#14,#34,#37]")
    public ResponseEntity<ListResponse<DailyWeatherResp>> searchWeather(
            @RequestParam @NotBlank(message = "주소는 공백이 될 수 없습니다.") String addressName) throws JSONException {

        List<DailyWeatherRespDto> dailyWeatherRespDtos = regionService.searchDailyWeather(addressName);

        return ResponseEntity.ok(
                ListResponse.of(
                        dailyWeatherRespDtos.stream().map(regionVoMapper::to).toList())
        );
    }
}
