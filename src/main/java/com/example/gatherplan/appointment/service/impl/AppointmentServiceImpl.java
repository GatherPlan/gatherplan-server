package com.example.gatherplan.appointment.service.impl;

import com.example.gatherplan.appointment.dto.CreateAppointmentReqDto;
import com.example.gatherplan.appointment.dto.CreateAppointmentRespDto;
import com.example.gatherplan.appointment.dto.CreateTempAppointmentReqDto;
import com.example.gatherplan.appointment.dto.CreateTempAppointmentRespDto;
import com.example.gatherplan.appointment.enums.AppointmentState;
import com.example.gatherplan.appointment.enums.CandidateTimeType;
import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.enums.UserType;
import com.example.gatherplan.appointment.exception.MemberException;
import com.example.gatherplan.appointment.repository.*;
import com.example.gatherplan.appointment.repository.entity.*;
import com.example.gatherplan.appointment.repository.entity.embedded.Address;
import com.example.gatherplan.appointment.repository.entity.embedded.CandidateTime;
import com.example.gatherplan.appointment.service.AppointmentService;
import com.example.gatherplan.common.exception.ErrorCode;
import com.example.gatherplan.common.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final MemberRepository memberRepository;
    private final MemberAppointmentMappingRepository memberAppointmentMappingRepository;
    private final TempMemberRepository tempMemberRepository;
    private final TempMemberAppointmentMappingRepository tempMemberAppointmentMappingRepository;

    @Override
    @Transactional
    public CreateAppointmentRespDto registerAppointment(CreateAppointmentReqDto createAppointmentReqDto) {
        String appointmentName = createAppointmentReqDto.getAppointmentName();
        CandidateTimeType candidateTimeType = createAppointmentReqDto.getCandidateTimeType();
        List<CandidateTime> candidateTimeList = createAppointmentReqDto
                .getCandidateTimeList();
        Address address = createAppointmentReqDto.getAddress();
        String notice = createAppointmentReqDto.getNotice();
        List<LocalDate> candidateDateList = createAppointmentReqDto.getCandidateDateList();

        Long appointmentId = saveAppointment(createAppointmentReqDto.getAppointmentName(), notice, address, candidateTimeType,
                candidateTimeList, candidateDateList);

        CustomUserDetails customUserDetails = createAppointmentReqDto.getCustomUserDetails();
        String email = customUserDetails.getEmail();

        Optional<Member> findMember = memberRepository.findMemberByEmail(email);
        if (findMember.isEmpty()) {
            throw new MemberException(ErrorCode.RESOURCE_NOT_FOUND, "해당 회원은 존재하지 않습니다.");
        }

        Member member = findMember.get();

        MemberAppointmentMapping memberAppointmentMapping = MemberAppointmentMapping.builder()
                .appointmentSeq(appointmentId)
                .memberSeq(member.getId())
                .userRole(UserRole.HOST)
                .build();

        memberAppointmentMappingRepository.saveMemberAppointmentMapping(memberAppointmentMapping);

        return CreateAppointmentRespDto.builder()
                .appointmentName(appointmentName)
                .address(address)
                .notice(notice)
                .hostName(member.getName())
                .userType(UserType.REGULAR)
                .candidateDateList(candidateDateList)
                .candidateTimeList(candidateTimeList)
                .build();
    }

    @Override
    @Transactional
    public CreateTempAppointmentRespDto registerTempAppointment(CreateTempAppointmentReqDto createTempAppointmentReqDto) {
        String appointmentName = createTempAppointmentReqDto.getAppointmentName();
        CandidateTimeType candidateTimeType = createTempAppointmentReqDto.getCandidateTimeType();
        List<CandidateTime> candidateTimeList = createTempAppointmentReqDto
                .getCandidateTimeList();
        Address address = createTempAppointmentReqDto.getAddress();
        String notice = createTempAppointmentReqDto.getNotice();
        List<LocalDate> candidateDateList = createTempAppointmentReqDto.getCandidateDateList();
        String name = createTempAppointmentReqDto.getName();
        String password = createTempAppointmentReqDto.getPassword();

        Long appointmentId = saveAppointment(appointmentName, notice, address, candidateTimeType,
                candidateTimeList, candidateDateList);

        TempMember tempMember = TempMember.builder()
                .name(name)
                .password(password)
                .build();

        Long tempMemberId = tempMemberRepository.saveTempMember(tempMember);

        TempMemberAppointmentMapping tempMemberAppointmentMapping = TempMemberAppointmentMapping.builder()
                .appointmentSeq(appointmentId)
                .tempMemberSeq(tempMemberId)
                .userRole(UserRole.HOST)
                .build();

        tempMemberAppointmentMappingRepository.saveTempMemberAppointmentMapping(tempMemberAppointmentMapping);

        return CreateTempAppointmentRespDto.builder()
                .appointmentName(appointmentName)
                .address(address)
                .notice(notice)
                .hostName(name)
                .userType(UserType.TEMPORARY)
                .candidateDateList(candidateDateList)
                .candidateTimeList(candidateTimeList)
                .build();
    }


    private Long saveAppointment(String appointmentName, String notice, Address address,
                                 CandidateTimeType candidateTimeType,
                                 List<CandidateTime> candidateTimeList,
                                 List<LocalDate> candidateDateList) {
        Appointment appointment = Appointment.builder()
                .name(appointmentName)
                .notice(notice)
                .address(address)
                .appointmentState(AppointmentState.UNCONFIRMED)
                .candidateTimeType(candidateTimeType)
                .candidateTimeList(candidateTimeList)
                .candidateDateList(candidateDateList)
                .build();

        return appointmentRepository.saveAppointment(appointment);
    }


}
