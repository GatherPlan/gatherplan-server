package com.example.gatherplan.region.mapper;

import com.example.gatherplan.common.enums.LocationType;
import com.example.gatherplan.external.vo.DailyWeatherClientResp;
import com.example.gatherplan.external.vo.FestivalClientResp;
import com.example.gatherplan.external.vo.KeywordPlaceClientResp;
import com.example.gatherplan.region.dto.DailyWeatherRespDto;
import com.example.gatherplan.region.dto.DistrictSearchRespDto;
import com.example.gatherplan.region.dto.FestivalRespDto;
import com.example.gatherplan.region.dto.PlaceSearchRespDto;
import com.example.gatherplan.region.repository.entity.Region;
import org.mapstruct.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL
)
public interface RegionMapper {
    @Mapping(target = "addressName", source = "entity.address")
    DistrictSearchRespDto to(Region entity, LocationType locationType);

    PlaceSearchRespDto to(KeywordPlaceClientResp.PlaceSearchInfo clientResp, LocationType locationType);

    DailyWeatherRespDto to(DailyWeatherClientResp.DailyWeatherInfo clientResp, String weatherImagePath);

    @Mapping(target = "startDate", source = "eventstartdate", qualifiedByName = "stringToLocalDate")
    @Mapping(target = "endDate", source = "eventenddate", qualifiedByName = "stringToLocalDate")
    @Mapping(target = "addressName", source = "addr1")
    @Mapping(target = "placeName", source = "addr2")
    @Mapping(target = "imagePath", source = "firstimage")
    FestivalRespDto to(FestivalClientResp.ItemInfo.Item clientResp);

    @Named("stringToLocalDate")
    default LocalDate stringToLocalDate(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyyMMdd"));
    }
}
