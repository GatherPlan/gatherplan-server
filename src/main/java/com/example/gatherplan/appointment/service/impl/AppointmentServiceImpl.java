package com.example.gatherplan.appointment.service.impl;

import com.example.gatherplan.api.kakaolocal.KakaoLocationClient;
import com.example.gatherplan.api.kakaolocal.KakaoLocationClientResp;
import com.example.gatherplan.api.weathernews.WeatherNewsClient;
import com.example.gatherplan.appointment.dto.*;
import com.example.gatherplan.appointment.enums.AppointmentState;
import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.exception.AppointmentException;
import com.example.gatherplan.appointment.exception.MemberException;
import com.example.gatherplan.appointment.mapper.AppointmentMapper;
import com.example.gatherplan.appointment.repository.*;
import com.example.gatherplan.appointment.repository.entity.*;
import com.example.gatherplan.appointment.service.AppointmentService;
import com.example.gatherplan.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentMapper appointmentMapper;
    private final AppointmentRepository appointmentRepository;
    private final MemberRepository memberRepository;
    private final MemberAppointmentMappingRepository memberAppointmentMappingRepository;
    private final TempMemberRepository tempMemberRepository;
    private final TempMemberAppointmentMappingRepository tempMemberAppointmentMappingRepository;
    private final RegionRepository regionRepository;
    private final KakaoLocationClient kakaoLocationClient;
    private final WeatherNewsClient weatherNewsClient;
    private final CustomRegionRepository customRegionRepository;

    @Override
    public List<SearchDistrictRespDto> searchDisctrict(SearchDistrictReqDto searchDistrictReqDto) {
        List<Region> regionList = regionRepository.findByAddressContaining(searchDistrictReqDto.getKeyword());

        return regionList.stream()
                .map(appointmentMapper::to)
                .toList();
    }

    @Override
    public List<SearchPlaceRespDto> searchPlace(SearchPlaceReqDto searchPlaceReqDto) {
        KakaoLocationClientResp kakaoLocationClientResp =
                kakaoLocationClient.searchLocationByKeyword(searchPlaceReqDto.getKeyword(), searchPlaceReqDto.getPage(),
                        searchPlaceReqDto.getSize());

        return kakaoLocationClientResp.getDocuments().stream()
                .map(appointmentMapper::to)
                .toList();
    }

    @Override
    public List<SearchWeatherRespDto> searchWhether(SearchWeatherReqDto searchWeatherReqDto) {

        Region region = customRegionRepository.findRegionByAddressName(searchWeatherReqDto.getAddressName())
                .orElseThrow(() -> new AppointmentException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 지역입니다."));

        return weatherNewsClient.searchWhetherByRegionCode(region.getCode()).getDaily().stream()
                .map(appointmentMapper::to)
                .toList();
    }

    @Override
    @Transactional
    public void registerAppointment(CreateAppointmentReqDto createAppointmentReqDto, String email) {
        Appointment appointment = appointmentMapper.to(createAppointmentReqDto, AppointmentState.UNCONFIRMED);
        Long appointmentId = appointmentRepository.save(appointment).getId();
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(ErrorCode.RESOURCE_NOT_FOUND, "해당 회원은 존재하지 않습니다."));

        MemberAppointmentMapping memberAppointmentMapping = MemberAppointmentMapping.builder()
                .appointmentSeq(appointmentId)
                .memberSeq(member.getId())
                .userRole(UserRole.HOST)
                .build();

        memberAppointmentMappingRepository.save(memberAppointmentMapping);
    }

    @Override
    @Transactional
    public void registerTempAppointment(CreateTempAppointmentReqDto createTempAppointmentReqDto) {
        Appointment appointment = appointmentMapper.to(createTempAppointmentReqDto, AppointmentState.UNCONFIRMED);

        Long appointmentId = appointmentRepository.save(appointment).getId();

        TempMember tempMember = TempMember.builder()
                .name(createTempAppointmentReqDto.getName())
                .password(createTempAppointmentReqDto.getPassword())
                .build();

        Long tempMemberId = tempMemberRepository.save(tempMember).getId();

        TempMemberAppointmentMapping tempMemberAppointmentMapping = TempMemberAppointmentMapping.builder()
                .appointmentSeq(appointmentId)
                .tempMemberSeq(tempMemberId)
                .userRole(UserRole.HOST)
                .build();

        tempMemberAppointmentMappingRepository.save(tempMemberAppointmentMapping);
    }

}
