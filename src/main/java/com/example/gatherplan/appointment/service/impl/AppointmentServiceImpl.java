package com.example.gatherplan.appointment.service.impl;

import com.example.gatherplan.api.kakaolocal.KakaoLocationClient;
import com.example.gatherplan.api.kakaolocal.KeywordPlaceClientResp;
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
import com.example.gatherplan.common.utils.UuidUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
    public List<RegionDto> searchRegion(RegionReqDto reqDto) {
        List<Region> regionList = regionRepository.findByAddressContaining(reqDto.getKeyword());

        return regionList.stream()
                .map(appointmentMapper::to)
                .toList();
    }

    @Override
    public List<KeywordPlaceRespDto> searchKeywordPlace(KeywordPlaceReqDto reqDto) {
        KeywordPlaceClientResp keywordPlaceClientResp =
                kakaoLocationClient.searchLocationByKeyword(
                        reqDto.getKeyword(), reqDto.getPage(), reqDto.getSize());

        return keywordPlaceClientResp.getDocuments().stream()
                .map(appointmentMapper::to)
                .toList();
    }

    @Override
    public List<DailyWeatherRespDto> searchDailyWeather(DailyWeatherReqDto reqDto) {

        Region region = customRegionRepository.findRegionByAddressName(reqDto.getAddressName())
                .orElseThrow(() -> new AppointmentException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 지역입니다."));

        return weatherNewsClient.searchWeatherByRegionCode(region.getCode()).getDaily().stream()
                .map(appointmentMapper::to)
                .toList();
    }

    @Override
    @Transactional
    public String registerAppointment(CreateAppointmentReqDto reqDto, String email) {
        String appointmentCode = UuidUtils.generateRandomString(12);

        Appointment appointment = appointmentMapper.to(reqDto, AppointmentState.UNCONFIRMED, appointmentCode);

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(ErrorCode.RESOURCE_NOT_FOUND, "해당 회원은 존재하지 않습니다."));

        Long appointmentId = appointmentRepository.save(appointment).getId();

        MemberAppointmentMapping memberAppointmentMapping = MemberAppointmentMapping.builder()
                .appointmentSeq(appointmentId)
                .memberSeq(member.getId())
                .userRole(UserRole.HOST)
                .build();

        memberAppointmentMappingRepository.save(memberAppointmentMapping);

        return appointmentCode;
    }

    @Override
    @Transactional
    public String registerTempAppointment(CreateTempAppointmentReqDto reqDto) {
        String appointmentCode = UuidUtils.generateRandomString(12);

        Appointment appointment = appointmentMapper.to(reqDto, AppointmentState.UNCONFIRMED, appointmentCode);

        CreateTempAppointmentReqDto.TempMemberInfo tempMemberInfo = reqDto.getTempMemberInfo();
        TempMember tempMember = TempMember.builder()
                .nickname(tempMemberInfo.getNickname())
                .password(tempMemberInfo.getPassword())
                .build();

        Long appointmentId = appointmentRepository.save(appointment).getId();
        Long tempMemberId = tempMemberRepository.save(tempMember).getId();

        TempMemberAppointmentMapping tempMemberAppointmentMapping = TempMemberAppointmentMapping.builder()
                .appointmentSeq(appointmentId)
                .tempMemberSeq(tempMemberId)
                .userRole(UserRole.HOST)
                .build();

        tempMemberAppointmentMappingRepository.save(tempMemberAppointmentMapping);

        return appointmentCode;
    }

    @Override
    public CheckTempAppointmentRespDto checkTempAppointment(CheckTempAppointmentReqDto reqDto, HttpServletRequest request) {

        Appointment appointment = appointmentRepository.findByAppointmentCode(reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 약속입니다."));

        TempMemberAppointmentMapping table = tempMemberAppointmentMappingRepository
                .findTempMemberAppointmentMappingByAppointmentSeq(appointment.getId())
                .orElseThrow(() -> new MemberException(ErrorCode.RESOURCE_NOT_FOUND, "관련된 회원을 찾을 수 없습니다."));

        TempMember tempMember = tempMemberRepository.findById(table.getTempMemberSeq())
                .orElseThrow(() -> new MemberException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 회원입니다."));

        HttpSession httpSession = request.getSession(true);
        httpSession.setAttribute("nickname", tempMember.getNickname());
        httpSession.setAttribute("appointmentCode", reqDto.getAppointmentCode());

        return CheckTempAppointmentRespDto.builder()
                .appointmentName(appointment.getAppointmentName())
                .appointmentState(appointment.getAppointmentState())
                .candidateDateList(appointment.getCandidateDateList())
                .candidateTimeTypeList(appointment.getCandidateTimeTypeList())
                .confirmedDateTime(appointment.getConfirmedDateTime())
                .appointmentCode(appointment.getAppointmentCode())
                .address(appointment.getAddress())
                .build();

    }

}
