package com.example.gatherplan.region.service.impl;

import com.example.gatherplan.appointment.utils.AddressUtils;
import com.example.gatherplan.common.enums.LocationType;
import com.example.gatherplan.common.unit.CustomPageRequest;
import com.example.gatherplan.external.DataPortalClient;
import com.example.gatherplan.external.KakaoLocationClient;
import com.example.gatherplan.external.WeatherNewsClient;
import com.example.gatherplan.external.vo.FestivalClientResp;
import com.example.gatherplan.external.vo.KeywordPlaceClientResp;
import com.example.gatherplan.region.dto.*;
import com.example.gatherplan.region.mapper.RegionMapper;
import com.example.gatherplan.region.repository.RegionRepository;
import com.example.gatherplan.region.repository.entity.Region;
import com.example.gatherplan.region.service.RegionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegionServiceImpl implements RegionService {

    private final KakaoLocationClient kakaoLocationClient;
    private final WeatherNewsClient weatherNewsClient;
    private final DataPortalClient dataPortalClient;
    private final RegionMapper regionMapper;
    private final RegionRepository regionRepository;

    @Override
    public Page<DistrictSearchRespDto> searchRegion(DistrictSearchReqDto reqDto) {
        CustomPageRequest customPageRequest = CustomPageRequest.of(reqDto.getPage(), reqDto.getSize());
        Page<Region> regionList = regionRepository.findByAddressContaining(reqDto.getKeyword(), customPageRequest);

        List<DistrictSearchRespDto> dataList = regionList.stream()
                .map(r -> regionMapper.to(r, LocationType.DISTRICT))
                .toList();

        return new PageImpl<>(dataList, customPageRequest, regionList.getTotalElements());
    }

    @Override
    public Page<PlaceSearchRespDto> searchKeywordPlace(KeywordPlaceReqDto reqDto) {
        CustomPageRequest customPageRequest = CustomPageRequest.of(reqDto.getPage(), reqDto.getSize());
        KeywordPlaceClientResp keywordPlaceClientResp =
                kakaoLocationClient.searchLocationByKeyword(
                        reqDto.getKeyword(), reqDto.getPage(), reqDto.getSize());

        List<PlaceSearchRespDto> dataList = keywordPlaceClientResp.getDocuments().stream()
                .map(r -> regionMapper.to(r, LocationType.DETAIL_ADDRESS))
                .toList();

        return new PageImpl<>(dataList, customPageRequest, keywordPlaceClientResp.getMeta().getPageableCount());
    }

    @Override
    public List<DailyWeatherRespDto> searchDailyWeather(String addressName) {
        String processedAddressName = AddressUtils.processAddressName(addressName);
        Region region = regionRepository.findByAddress(processedAddressName)
                .orElseGet(() -> {
                    log.warn("No region found for addressName {}", addressName);
                    return null;
                });

        // 조회되지 않은 지역의 경우 에러 반환이 아닌 빈 리스트 반환
        if (Objects.isNull(region)) {
            return List.of();
        }

        return weatherNewsClient.searchWeatherByRegionCode(region.getCode()).getDaily().stream()
                .map(w -> {
                    leaveWeatherCodeWithText(w.getWeatherCode(), w.getWeatherState());

                    String weatherImagePath = generateWeatherImagePath(w.getWeatherCode());
                    return regionMapper.to(w, weatherImagePath);
                })
                .toList();
    }

    @Override
    public List<FestivalRespDto> searchFestival() {
        String fromDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        List<FestivalClientResp.ItemInfo.Item> dataList =
                dataPortalClient.searchFestival(fromDate).getResponse().getBody().getItems().getItem();

        return dataList.stream()
                .map(regionMapper::to).toList();
    }

    private String generateWeatherImagePath(String weatherCode) {
        String baseUrl = "https://www.kr-weathernews.com/mv4/html/assets/images/weather-icon-set/icon1/dark/night/%s.svg";
        return String.format(baseUrl, weatherCode);
    }

    /**
     * weather code 및 weather state 매핑 추적을 위한 로그 기록
     * 모든 날씨 추적 완료 후 삭제 예정
     *
     * @param weatherCode  날씨 코드 ex)200
     * @param weatherState 날씨 코드 텍스트 ex)흐림
     */
    private void leaveWeatherCodeWithText(String weatherCode, String weatherState) {
        log.info("WEATHER CODE WITH STATE : {}-{}", weatherCode, weatherState);
    }
}
