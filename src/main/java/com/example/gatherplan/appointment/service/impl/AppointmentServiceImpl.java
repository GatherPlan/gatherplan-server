package com.example.gatherplan.appointment.service.impl;

import com.example.gatherplan.appointment.dto.*;
import com.example.gatherplan.appointment.enums.AppointmentState;
import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.exception.AppointmentException;
import com.example.gatherplan.appointment.exception.UserException;
import com.example.gatherplan.appointment.mapper.AppointmentMapper;
import com.example.gatherplan.appointment.repository.AppointmentRepository;
import com.example.gatherplan.appointment.repository.UserAppointmentMappingRepository;
import com.example.gatherplan.appointment.repository.UserRepository;
import com.example.gatherplan.appointment.repository.entity.Appointment;
import com.example.gatherplan.appointment.repository.entity.User;
import com.example.gatherplan.appointment.repository.entity.UserAppointmentMapping;
import com.example.gatherplan.appointment.service.AppointmentService;
import com.example.gatherplan.common.exception.ErrorCode;
import com.example.gatherplan.common.utils.UuidUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentMapper appointmentMapper;
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final UserAppointmentMappingRepository userAppointmentMappingRepository;


    @Override
    @Transactional
    public String registerAppointment(CreateAppointmentReqDto reqDto, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ErrorCode.RESOURCE_NOT_FOUND, "해당 회원은 존재하지 않습니다."));

        String appointmentCode = UuidUtils.generateRandomString(12);

        Appointment appointment = appointmentMapper.to(reqDto, AppointmentState.UNCONFIRMED, appointmentCode);

        Long appointmentId = appointmentRepository.save(appointment).getId();

        UserAppointmentMapping userAppointmentMapping = UserAppointmentMapping.builder()
                .appointmentSeq(appointmentId)
                .userSeq(user.getId())
                .userRole(UserRole.HOST)
                .build();

        userAppointmentMappingRepository.save(userAppointmentMapping);

        return appointmentCode;
    }

    @Override
    public void checkParticipation(CheckAppointmentReqDto checkAppointmentReqDto, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 회원입니다."));

        Appointment appointment = appointmentRepository
                .findByAppointmentCode(checkAppointmentReqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 약속입니다."));

        List<UserAppointmentMapping> maps = userAppointmentMappingRepository.findByAppointmentSeq(appointment.getId());

        if (maps.stream().noneMatch(map -> map.getUserSeq().equals(user.getId()))) {
            throw new AppointmentException(ErrorCode.RESOURCE_NOT_FOUND, "참여하지 않은 약속입니다.");
        }
    }

    @Override
    public List<GetAppointmentListRespDto> getAppointmentList(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 회원입니다."));

        List<UserAppointmentMapping> maps = userAppointmentMappingRepository.findByUserSeq(user.getId());

        List<Appointment> appointments = maps.stream()
                .map(UserAppointmentMapping::getAppointmentSeq)
                .map(appointmentRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        List<String> hostNames = getHostNames(maps);

        return IntStream.range(0, appointments.size())
                .mapToObj(index -> appointmentMapper.to(appointments.get(index), hostNames.get(index)))
                .toList();

    }

    @Override
    public List<GetAppointmentSearchListRespDto> getAppointmentSearchList(
            GetAppointmentSearchListReqDto getAppointmentSearchListReqDto, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 회원입니다."));

        List<UserAppointmentMapping> maps = userAppointmentMappingRepository.findByUserSeq(user.getId());

        List<Appointment> appointments = maps.stream()
                .map(UserAppointmentMapping::getAppointmentSeq)
                .map(appointmentRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(appointment -> appointment.getAppointmentName()
                        .contains(getAppointmentSearchListReqDto.getKeyword()))
                .toList();


        List<String> hostNames = getHostNames(maps);

        return IntStream.range(0, appointments.size())
                .mapToObj(index -> appointmentMapper.toDto(appointments.get(index), hostNames.get(index)))
                .toList();
    }

    @Override
    public GetAppointmentInfoRespDto getAppointmentInfo
            (GetAppointmentInfoReqDto getAppointmentInfoReqDto, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 회원입니다."));

        Appointment appointment = appointmentRepository
                .findByAppointmentCode(getAppointmentInfoReqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 약속입니다."));

        userAppointmentMappingRepository
                .findByAppointmentSeqAndUserSeq(appointment.getId(), user.getId())
                .orElseThrow(() -> new AppointmentException(ErrorCode.RESOURCE_NOT_FOUND, "매핑되어 있지 않은 약속입니다."));

        return GetAppointmentInfoRespDto.builder()
                .address(appointment.getAddress())
                .confirmedDateTime(appointment.getConfirmedDateTime())
                .build();
    }

    private List<String> getHostNames(List<UserAppointmentMapping> maps) {
        return maps.stream()
                .map(UserAppointmentMapping::getAppointmentSeq)
                .flatMap(appointmentSeq -> appointmentRepository.findById(appointmentSeq).stream())
                .map(appointment -> userAppointmentMappingRepository
                        .findByAppointmentSeqAndUserRole(appointment.getId(), UserRole.HOST))
                .filter(Objects::nonNull)
                .map(hostMapping -> userRepository.findById(hostMapping.getUserSeq()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(User::getNickname)
                .toList();
    }

}
