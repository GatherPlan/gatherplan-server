package com.example.gatherplan.appointment.service.impl;

import com.example.gatherplan.appointment.dto.*;
import com.example.gatherplan.appointment.enums.AppointmentState;
import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.exception.AppointmentException;
import com.example.gatherplan.appointment.exception.UserException;
import com.example.gatherplan.appointment.mapper.AppointmentMapper;
import com.example.gatherplan.appointment.repository.*;
import com.example.gatherplan.appointment.repository.entity.Appointment;
import com.example.gatherplan.appointment.repository.entity.TempUserAppointmentMapping;
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

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentMapper appointmentMapper;
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final UserAppointmentMappingRepository userAppointmentMappingRepository;
    private final TempUserAppointmentMappingRepository tempUserAppointmentMappingRepository;
    private final TempUserRepository tempUserRepository;


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
    public void checkParticipation(ParticipationStatusReqDto reqDto, String email) {
        checkUserParticipation(email, reqDto.getAppointmentCode());
    }

    @Override
    public List<AppointmentListRespDto> getAppointmentList(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 회원입니다."));

        List<UserAppointmentMapping> maps = userAppointmentMappingRepository.findByUserSeq(user.getId());

        return maps.stream()
                .map(mapping -> {
                    Appointment appointment = appointmentRepository.findById(mapping.getAppointmentSeq())
                            .orElseThrow(() ->
                                    new AppointmentException(ErrorCode.RESOURCE_NOT_FOUND, "약속을 찾을 수 없습니다."));

                    UserAppointmentMapping hostMapping = userAppointmentMappingRepository
                            .findByAppointmentSeqAndUserRole(appointment.getId(), UserRole.HOST)
                            .orElseThrow(() ->
                                    new AppointmentException(ErrorCode.RESOURCE_NOT_FOUND, "호스트를 찾을 수 없습니다."));

                    User host = userRepository.findById(hostMapping.getUserSeq())
                            .orElseThrow(() ->
                                    new AppointmentException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 회원입니다."));

                    return appointmentMapper.toGetAppointmentListRespDto(appointment, host.getNickname());
                })
                .toList();
    }

    @Override
    public List<AppointmentSearchListRespDto> getAppointmentSearchList(
            AppointmentSearchListReqDto reqDto, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 회원입니다."));

        List<UserAppointmentMapping> maps = userAppointmentMappingRepository.findByUserSeq(user.getId());

        return maps.stream()
                .map(mapping -> {
                    Appointment appointment = appointmentRepository.findById(mapping.getAppointmentSeq())
                            .filter(filterAppointment -> filterAppointment.getAppointmentName()
                                    .contains(reqDto.getKeyword()))
                            .orElseThrow(() ->
                                    new AppointmentException(ErrorCode.RESOURCE_NOT_FOUND, "약속을 찾을 수 없습니다."));

                    UserAppointmentMapping hostMapping = userAppointmentMappingRepository
                            .findByAppointmentSeqAndUserRole(appointment.getId(), UserRole.HOST)
                            .orElseThrow(() ->
                                    new AppointmentException(ErrorCode.RESOURCE_NOT_FOUND, "호스트를 찾을 수 없습니다."));

                    User host = userRepository.findById(hostMapping.getUserSeq())
                            .orElseThrow(() ->
                                    new AppointmentException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 회원입니다."));

                    return appointmentMapper.toGetAppointmentSearchListRespDto(appointment, host.getNickname());
                })
                .toList();
    }

    @Override
    public AppointmentInfoRespDto getAppointmentInfo(AppointmentInfoReqDto reqDto, String email) {
        checkUserParticipation(email, reqDto.getAppointmentCode());
        Appointment appointment = findAppointmentByCode(reqDto.getAppointmentCode());

        return AppointmentInfoRespDto.builder()
                .address(appointment.getAddress())
                .confirmedDateTime(appointment.getConfirmedDateTime())
                .build();
    }

    @Override
    public AppointmentParticipationInfoRespDto getAppointmentParticipationInfo(
            AppointmentParticipationInfoReqDto reqDto, String email) {

        checkUserParticipation(email, reqDto.getAppointmentCode());
        Appointment appointment = findAppointmentByCode(reqDto.getAppointmentCode());

        List<UserAppointmentMapping> maps = userAppointmentMappingRepository
                .findAllByAppointmentSeqAndUserRole(appointment.getId(), UserRole.GUEST);

        List<AppointmentParticipationInfoRespDto.UserParticipationInfo> userParticipationInfoList = maps.stream()
                .map(mapping -> {
                    User findUser = userRepository.findById(mapping.getUserSeq())
                            .orElseThrow(() -> new UserException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 회원입니다."));

                    return AppointmentParticipationInfoRespDto.UserParticipationInfo.builder()
                            .nickname(findUser.getNickname())
                            .selectedDateTime(mapping.getSelectedDateTimeList())
                            .build();
                })
                .toList();

        return AppointmentParticipationInfoRespDto.builder()
                .userParticipationInfo(userParticipationInfoList)
                .candidateTimeTypeList(appointment.getCandidateTimeTypeList())
                .candidateDateList(appointment.getCandidateDateList())
                .build();
    }

    @Override
    @Transactional
    public void deleteAppointment(DeleteAppointmentReqDto reqDto, String email) {
        Appointment appointment = checkHost(email, reqDto.getAppointmentCode());

        List<TempUserAppointmentMapping> tempUserMaps = tempUserAppointmentMappingRepository
                .findAllByAppointmentSeq(appointment.getId());

        List<Long> tempUserDeleteList = tempUserMaps.stream()
                .map(TempUserAppointmentMapping::getTempUserSeq)
                .toList();

        tempUserRepository.deleteAllById(tempUserDeleteList);

        tempUserAppointmentMappingRepository.deleteAll(tempUserMaps);

        List<UserAppointmentMapping> userMaps = userAppointmentMappingRepository
                .findAllByAppointmentSeq(appointment.getId());

        userAppointmentMappingRepository.deleteAll(userMaps);

        appointmentRepository.deleteById(appointment.getId());
    }

    @Override
    @Transactional
    public void updateAppointment(UpdateAppointmentReqDto reqDto, String email) {
        Appointment appointment = checkHost(email, reqDto.getAppointmentCode());

        appointment.update(reqDto.getAppointmentName(), reqDto.getCandidateTimeTypeList(),
                reqDto.getAddress(), reqDto.getCandidateDateList());
    }

    private Appointment checkHost(String email, String appointmentCode) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 회원입니다."));

        Appointment appointment = findAppointmentByCode(appointmentCode);

        UserAppointmentMapping maps = userAppointmentMappingRepository
                .findByAppointmentSeqAndUserRole(appointment.getId(), UserRole.HOST)
                .orElseThrow(() -> new AppointmentException(ErrorCode.RESOURCE_NOT_FOUND, "호스트를 찾을 수 없습니다."));

        User host = userRepository.findById(maps.getUserSeq())
                .orElseThrow(() -> new UserException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 회원입니다."));

        if (!user.getId().equals(host.getId())) {
            throw new AppointmentException(ErrorCode.RESOURCE_NOT_FOUND, "호스트만 접근할 수 있습니다.");
        }

        return appointment;
    }

    private void checkUserParticipation(String email, String appointmentCode) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 회원입니다."));

        Appointment appointment = findAppointmentByCode(appointmentCode);

        userAppointmentMappingRepository
                .findByAppointmentSeqAndUserSeqAndUserRole(appointment.getId(), user.getId(), UserRole.GUEST)
                .orElseThrow(() -> new AppointmentException(ErrorCode.RESOURCE_NOT_FOUND, "참여하지 않은 약속입니다."));
    }


    private Appointment findAppointmentByCode(String appointmentCode) {
        return appointmentRepository
                .findByAppointmentCode(appointmentCode)
                .orElseThrow(() -> new AppointmentException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 약속입니다."));
    }

}
