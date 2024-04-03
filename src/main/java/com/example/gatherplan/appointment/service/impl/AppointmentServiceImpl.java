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
                .orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_USER_BY_EMAIL));

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
    public void retrieveParticipationStatus(ParticipationStatusReqDto reqDto, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_USER_BY_EMAIL));

        Appointment appointment = appointmentRepository
                .findByAppointmentCode(reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT_BY_CODE));

        userAppointmentMappingRepository
                .findByUserSeqAndAppointmentSeqAndUserRole(user.getId(), appointment.getId(), UserRole.GUEST)
                .orElseThrow(() -> new AppointmentException(ErrorCode.USER_NOT_PARTICIPATE_APPOINTMENT));
    }

    @Override
    public List<AppointmentListRespDto> retrieveAppointmentList(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_USER_BY_EMAIL));

        List<UserAppointmentMapping> maps = userAppointmentMappingRepository.findByUserSeq(user.getId());

        return maps.stream()
                .map(mapping -> {
                    Appointment appointment = appointmentRepository
                            .findById(mapping.getAppointmentSeq())
                            .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT_BY_ID));

                    UserAppointmentMapping hostMapping = userAppointmentMappingRepository
                            .findByAppointmentSeqAndUserRole(appointment.getId(), UserRole.HOST)
                            .orElseThrow(() ->
                                    new AppointmentException(ErrorCode.NOT_FOUND_HOST));

                    User host = userRepository.findById(hostMapping.getUserSeq())
                            .orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_USER_BY_ID));

                    return appointmentMapper.toGetAppointmentListRespDto(appointment, host.getNickname());
                })
                .toList();
    }

    @Override
    public List<AppointmentSearchListRespDto> retrieveAppointmentSearchList(
            AppointmentSearchListReqDto reqDto, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_USER_BY_EMAIL));

        List<UserAppointmentMapping> maps = userAppointmentMappingRepository.findByUserSeq(user.getId());

        return maps.stream()
                .map(mapping -> {
                    Appointment appointment = appointmentRepository.findById(mapping.getAppointmentSeq())
                            .filter(filterAppointment -> filterAppointment.getAppointmentName()
                                    .contains(reqDto.getKeyword()))
                            .orElseThrow(() ->
                                    new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT_CONTAIN_KEYWORD));

                    UserAppointmentMapping hostMapping = userAppointmentMappingRepository
                            .findByAppointmentSeqAndUserRole(appointment.getId(), UserRole.HOST)
                            .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_HOST));

                    User host = userRepository.findById(hostMapping.getUserSeq())
                            .orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_USER_BY_ID));

                    return appointmentMapper.toGetAppointmentSearchListRespDto(appointment, host.getNickname());
                })
                .toList();
    }

    @Override
    public AppointmentInfoRespDto retrieveAppointmentInfo(AppointmentInfoReqDto reqDto, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_USER_BY_EMAIL));

        Appointment appointment = appointmentRepository
                .findByAppointmentCode(reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT_BY_CODE));

        userAppointmentMappingRepository
                .findByUserSeqAndAppointmentSeqAndUserRole(user.getId(), appointment.getId(), UserRole.GUEST)
                .orElseThrow(() -> new AppointmentException(ErrorCode.USER_NOT_PARTICIPATE_APPOINTMENT));

        return AppointmentInfoRespDto.builder()
                .address(appointment.getAddress())
                .confirmedDateTime(appointment.getConfirmedDateTime())
                .build();
    }

    @Override
    public AppointmentParticipationInfoRespDto retrieveAppointmentParticipationInfo(
            AppointmentParticipationInfoReqDto reqDto, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_USER_BY_EMAIL));

        Appointment appointment = appointmentRepository
                .findByAppointmentCode(reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT_BY_CODE));

        userAppointmentMappingRepository
                .findByUserSeqAndAppointmentSeqAndUserRole(user.getId(), appointment.getId(), UserRole.GUEST)
                .orElseThrow(() -> new AppointmentException(ErrorCode.USER_NOT_PARTICIPATE_APPOINTMENT));

        List<UserAppointmentMapping> maps = userAppointmentMappingRepository
                .findAllByAppointmentSeqAndUserRole(appointment.getId(), UserRole.GUEST);

        List<AppointmentParticipationInfoRespDto.UserParticipationInfo> userParticipationInfoList = maps.stream()
                .map(mapping -> {
                    User findUser = userRepository.findById(mapping.getUserSeq())
                            .orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_USER_BY_ID));

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
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_USER_BY_EMAIL));

        Appointment appointment = appointmentRepository
                .findByAppointmentCode(reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT_BY_CODE));

        checkHost(user.getId(), appointment.getId());

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

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_USER_BY_EMAIL));

        Appointment appointment = appointmentRepository
                .findByAppointmentCode(reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT_BY_CODE));

        checkHost(user.getId(), appointment.getId());

        appointment.update(reqDto.getAppointmentName(), reqDto.getCandidateTimeTypeList(),
                reqDto.getAddress(), reqDto.getCandidateDateList());
    }


    private void checkHost(Long userId, Long appointmentId) {
        UserAppointmentMapping maps = userAppointmentMappingRepository
                .findByAppointmentSeqAndUserRole(appointmentId, UserRole.HOST)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_HOST));

        User host = userRepository.findById(maps.getUserSeq())
                .orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_USER_BY_ID));

        if (!userId.equals(host.getId())) {
            throw new AppointmentException(ErrorCode.SERVICE_ONLY_SUPPORT_HOST);
        }

    }

}
