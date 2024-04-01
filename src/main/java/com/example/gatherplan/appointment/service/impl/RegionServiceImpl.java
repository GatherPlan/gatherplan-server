package com.example.gatherplan.appointment.service.impl;

import com.example.gatherplan.api.KakaoLocationClient;
import com.example.gatherplan.api.WeatherNewsClient;
import com.example.gatherplan.api.vo.KeywordPlaceClientResp;
import com.example.gatherplan.appointment.dto.*;
import com.example.gatherplan.appointment.exception.AppointmentException;
import com.example.gatherplan.appointment.mapper.RegionMapper;
import com.example.gatherplan.appointment.repository.CustomRegionRepository;
import com.example.gatherplan.appointment.repository.RegionRepository;
import com.example.gatherplan.appointment.repository.entity.Region;
import com.example.gatherplan.appointment.service.RegionService;
import com.example.gatherplan.common.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public List<RegionDto> searchRegion(RegionReqDto reqDto) {
        List<Region> regionList = regionRepository.findByAddressContaining(reqDto.getKeyword());

        return regionList.stream()
                .map(regionMapper::to)
                .toList();
    }

    @Override
    public List<KeywordPlaceRespDto> searchKeywordPlace(KeywordPlaceReqDto reqDto) {
        KeywordPlaceClientResp keywordPlaceClientResp =
                kakaoLocationClient.searchLocationByKeyword(
                        reqDto.getKeyword(), reqDto.getPage(), reqDto.getSize());

        return keywordPlaceClientResp.getDocuments().stream()
                .map(regionMapper::to)
                .toList();
    }

    @Override
    public List<DailyWeatherRespDto> searchDailyWeather(DailyWeatherReqDto reqDto) {

        Region region = customRegionRepository.findRegionByAddressName(reqDto.getAddressName())
                .orElseThrow(() -> new AppointmentException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 지역입니다."));

        return weatherNewsClient.searchWeatherByRegionCode(region.getCode()).getDaily().stream()
                .map(regionMapper::to)
                .toList();
    }

    @Transactional
    public void saveFromCSV(List<CSVRowDto> rows) {
        List<Region> entities = new ArrayList<>();
        for (CSVRowDto row : rows) {
            Region entity = Region.builder()
                    .code(row.getCode())
                    .address(row.getAddress())
                    .build();
            entities.add(entity);
        }

        regionRepository.saveAll(entities);
    }
}
