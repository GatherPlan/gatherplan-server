package com.example.gatherplan.appointment.service.impl;

import com.example.gatherplan.api.service.KakaoLocalService;
import com.example.gatherplan.appointment.dto.*;
import com.example.gatherplan.appointment.enums.AppointmentState;
import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.exception.MemberException;
import com.example.gatherplan.appointment.mapper.AppointmentMapper;
import com.example.gatherplan.appointment.repository.*;
import com.example.gatherplan.appointment.repository.entity.*;
import com.example.gatherplan.appointment.service.AppointmentService;
import com.example.gatherplan.common.exception.ErrorCode;
import com.example.gatherplan.controller.vo.common.PlaceDetail;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
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
    private final KakaoLocalService kakaoLocalService;

    @Override
    public SearchPlaceRespDto searchPlace(SearchPlaceReqDto searchPlaceReqDto) {
        List<Region> regionList = regionRepository.findByRegionNameContaining(searchPlaceReqDto.getKeyword());

        return SearchPlaceRespDto.builder()
                .regionList(regionList)
                .build();
    }

    @Override
    public SearchPlaceDetailRespDto searchPlaceDetail(SearchPlaceDetailReqDto searchPlaceDetailReqDto) {
        Mono<String> stringMono = kakaoLocalService.callExternalAPI(searchPlaceDetailReqDto.getKeyword());
        String result = stringMono.block(); // 비동기 처리 결과를 동기적으로 가져옴

        List<PlaceDetail> placeDetails = new ArrayList<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(result);
            JsonNode documentsNode = jsonNode.get("documents");

            for (JsonNode documentNode : documentsNode) {
                String addressName = documentNode.get("address_name").asText();
                String placeName = documentNode.get("place_name").asText();
                PlaceDetail placeDetail = PlaceDetail.builder()
                        .placeName(placeName)
                        .addressName(addressName)
                        .build();
                placeDetails.add(placeDetail);
            }
        } catch (JsonProcessingException e) {
            log.info(e.getMessage());
        }

        return SearchPlaceDetailRespDto.builder()
                .placeDetails(placeDetails)
                .build();
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
