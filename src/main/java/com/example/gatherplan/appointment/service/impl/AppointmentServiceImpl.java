package com.example.gatherplan.appointment.service.impl;

import com.example.gatherplan.appointment.dto.CreateAppointmentReqDto;
import com.example.gatherplan.appointment.dto.CreateTempAppointmentReqDto;
import com.example.gatherplan.appointment.enums.AppointmentState;
import com.example.gatherplan.appointment.enums.CandidateTimeType;
import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.exception.MemberException;
import com.example.gatherplan.appointment.mapper.AppointmentMapper;
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

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentMapper appointmentMapper;

    private final AppointmentRepository appointmentRepository;
    private final MemberRepository memberRepository;
    private final MemberAppointmentMappingRepository memberAppointmentMappingRepository;
    private final TempMemberRepository tempMemberRepository;
    private final TempMemberAppointmentMappingRepository tempMemberAppointmentMappingRepository;

    @Override
    @Transactional
    public void registerAppointment(CreateAppointmentReqDto createAppointmentReqDto, String email) {

        Appointment appointment = appointmentMapper.to(createAppointmentReqDto);
        Long appointmentId = appointmentRepository.save(appointment);

        Member member = memberRepository.findMemberByEmail(email)
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

        Long tempMemberId = tempMemberRepository.save(tempMember);

        TempMemberAppointmentMapping tempMemberAppointmentMapping = TempMemberAppointmentMapping.builder()
                .appointmentSeq(appointmentId)
                .tempMemberSeq(tempMemberId)
                .userRole(UserRole.HOST)
                .build();

        tempMemberAppointmentMappingRepository.save(tempMemberAppointmentMapping);

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

        return appointmentRepository.save(appointment);
    }


}
