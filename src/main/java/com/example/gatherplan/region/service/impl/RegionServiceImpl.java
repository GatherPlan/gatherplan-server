package com.example.gatherplan.region.service.impl;

import com.example.gatherplan.common.enums.LocationType;
import com.example.gatherplan.common.exception.ErrorCode;
import com.example.gatherplan.external.KakaoLocationClient;
import com.example.gatherplan.external.WeatherNewsClient;
import com.example.gatherplan.external.vo.KeywordPlaceClientResp;
import com.example.gatherplan.region.dto.DailyWeatherRespDto;
import com.example.gatherplan.region.dto.KeywordPlaceReqDto;
import com.example.gatherplan.region.dto.KeywordPlaceRespDto;
import com.example.gatherplan.region.dto.RegionDto;
import com.example.gatherplan.region.exception.RegionException;
import com.example.gatherplan.region.mapper.RegionMapper;
import com.example.gatherplan.region.repository.CustomRegionRepository;
import com.example.gatherplan.region.repository.RegionRepository;
import com.example.gatherplan.region.repository.entity.Region;
import com.example.gatherplan.region.service.RegionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 로컬 DB에 법정동, 법정동 코드 관련 csv 파일을 불러오기 위한 임시 서비스 클래스입니다.
 */
@Service
@AllArgsConstructor
public class RegionServiceImpl implements RegionService {

    private final KakaoLocationClient kakaoLocationClient;
    private final WeatherNewsClient weatherNewsClient;
    private final CustomRegionRepository customRegionRepository;
    private final RegionMapper regionMapper;
    private RegionRepository regionRepository;

    @Override
    public List<RegionDto> searchRegion(String keyword) {
        List<Region> regionList = regionRepository.findByAddressContaining(keyword);

        return regionList.stream()
                .map(r -> regionMapper.to(r, LocationType.DISTRICT))
                .toList();
    }

    @Override
    public List<KeywordPlaceRespDto> searchKeywordPlace(KeywordPlaceReqDto reqDto) {
        KeywordPlaceClientResp keywordPlaceClientResp =
                kakaoLocationClient.searchLocationByKeyword(
                        reqDto.getKeyword(), reqDto.getPage(), reqDto.getSize());

        return keywordPlaceClientResp.getDocuments().stream()
                .map(r -> regionMapper.to(r, LocationType.DETAIL_ADDRESS))
                .toList();
    }

    @Override
    public List<DailyWeatherRespDto> searchDailyWeather(String addressName) {
        Region region = customRegionRepository.findRegionByAddressName(addressName)
                .orElseThrow(() -> new RegionException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 지역입니다."));

        return weatherNewsClient.searchWeatherByRegionCode(region.getCode()).getDaily().stream()
                .map(regionMapper::to)
                .toList();
    }
}
