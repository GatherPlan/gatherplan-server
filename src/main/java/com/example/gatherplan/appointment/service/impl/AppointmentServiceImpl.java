package com.example.gatherplan.appointment.service.impl;

import com.example.gatherplan.appointment.dto.CreateAppointmentReqDto;
import com.example.gatherplan.appointment.dto.CreateTempAppointmentReqDto;
import com.example.gatherplan.appointment.dto.MemberInfoReqDto;
import com.example.gatherplan.appointment.enums.AppointmentState;
import com.example.gatherplan.appointment.enums.CandidateTimeType;
import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.exception.MemberException;
import com.example.gatherplan.appointment.repository.*;
import com.example.gatherplan.appointment.repository.entity.*;
import com.example.gatherplan.appointment.repository.entity.embedded.Address;
import com.example.gatherplan.appointment.repository.entity.embedded.CandidateTime;
import com.example.gatherplan.appointment.service.AppointmentService;
import com.example.gatherplan.common.exception.ErrorCode;
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
    public void registerAppointment(CreateAppointmentReqDto createAppointmentReqDto, MemberInfoReqDto memberInfoReqDto) {
        Long appointmentId = saveAppointment(
                createAppointmentReqDto.getAppointmentName(),
                createAppointmentReqDto.getNotice(),
                createAppointmentReqDto.getAddress(),
                createAppointmentReqDto.getCandidateTimeType(),
                createAppointmentReqDto.getCandidateTimeList(),
                createAppointmentReqDto.getCandidateDateList()
        );

        Optional<Member> findMember = memberRepository.findMemberByEmail(memberInfoReqDto.getEmail());
        Member member = findMember.orElseThrow(() -> new MemberException(ErrorCode.RESOURCE_NOT_FOUND, "해당 회원은 존재하지 않습니다."));

        MemberAppointmentMapping memberAppointmentMapping = MemberAppointmentMapping.builder()
                .appointmentSeq(appointmentId)
                .memberSeq(member.getId())
                .userRole(UserRole.HOST)
                .build();

        memberAppointmentMappingRepository.saveMemberAppointmentMapping(memberAppointmentMapping);

    }

    @Override
    @Transactional
    public void registerTempAppointment(CreateTempAppointmentReqDto createTempAppointmentReqDto) {
        Long appointmentId = saveAppointment(
                createTempAppointmentReqDto.getAppointmentName(),
                createTempAppointmentReqDto.getNotice(),
                createTempAppointmentReqDto.getAddress(),
                createTempAppointmentReqDto.getCandidateTimeType(),
                createTempAppointmentReqDto.getCandidateTimeList(),
                createTempAppointmentReqDto.getCandidateDateList()
        );

        TempMember tempMember = TempMember.builder()
                .name(createTempAppointmentReqDto.getName())
                .password(createTempAppointmentReqDto.getPassword())
                .build();

        Long tempMemberId = tempMemberRepository.saveTempMember(tempMember);

        TempMemberAppointmentMapping tempMemberAppointmentMapping = TempMemberAppointmentMapping.builder()
                .appointmentSeq(appointmentId)
                .tempMemberSeq(tempMemberId)
                .userRole(UserRole.HOST)
                .build();

        tempMemberAppointmentMappingRepository.saveTempMemberAppointmentMapping(tempMemberAppointmentMapping);

    }

    private Long saveAppointment(String appointmentName, String notice, Address address,
                                 CandidateTimeType candidateTimeType, List<CandidateTime> candidateTimeList,
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
