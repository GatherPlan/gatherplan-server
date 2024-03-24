package com.example.gatherplan.appointment.service.impl;

import com.example.gatherplan.api.service.KakaoLocalClient;
import com.example.gatherplan.appointment.dto.*;
import com.example.gatherplan.appointment.enums.AppointmentState;
import com.example.gatherplan.appointment.enums.UserRole;
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
import java.util.stream.Collectors;

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
    private final KakaoLocalClient kakaoLocalClient;

    @Override
    public List<searchDistrictRespDto> searchDisctrict(SearchDistrictReqDto searchDistrictReqDto) {
        List<Region> regionList = regionRepository.findByAddressContaining(searchDistrictReqDto.getKeyword());

        return regionList.stream()
                .map(region -> searchDistrictRespDto.builder()
                        .address(region.getAddress())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<SearchPlaceRespDto> searchPlace(SearchPlaceReqDto searchPlaceReqDto) {
        return kakaoLocalClient.callExternalAPI(searchPlaceReqDto.getKeyword());
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
