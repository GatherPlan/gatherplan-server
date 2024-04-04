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
    private final CustomUserAppointmentMappingRepository customUserAppointmentMappingRepository;

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
    public boolean isUserParticipated(ParticipationStatusReqDto reqDto, String email) {
        return customUserAppointmentMappingRepository.existUserMappedToAppointment(
                email, reqDto.getAppointmentCode(), UserRole.GUEST);
    }

    @Override
    public List<AppointmentWithHostRespDto> retrieveAppointmentList(String email) {
        return customUserAppointmentMappingRepository.findAllAppointmentsWithHostByEmail(email);
    }

    @Override
    public List<AppointmentSearchListRespDto> retrieveAppointmentSearchList(
            AppointmentSearchListReqDto reqDto, String email) {
        return customUserAppointmentMappingRepository.findAllAppointmentsWithHostByEmailAndKeyword(
                email, reqDto.getKeyword());
    }

    @Override
    public AppointmentInfoRespDto retrieveAppointmentInfo(AppointmentInfoReqDto reqDto, String email) {
        AppointmentInfoDto appointmentInfoDto = customUserAppointmentMappingRepository
                .findAppointmentInfoDto(email, reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT_BY_CODE));

        return appointmentMapper.to(appointmentInfoDto);
    }

    @Override
    public AppointmentParticipationInfoRespDto retrieveAppointmentParticipationInfo(
            AppointmentParticipationInfoReqDto reqDto, String email) {

        Appointment appointment = appointmentRepository
                .findByAppointmentCode(reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT_BY_CODE));

        List<AppointmentParticipationInfoDto.UserParticipationInfo> appointmentParticipationInfoList =
                customUserAppointmentMappingRepository.findAppointmentParticipationInfoList(email, reqDto.getAppointmentCode());

        List<AppointmentParticipationInfoRespDto.UserParticipationInfo> userParticipationInfoList = appointmentParticipationInfoList
                .stream().map(appointmentMapper::to).toList();

        return appointmentMapper.to(userParticipationInfoList, appointment.getCandidateTimeTypeList()
                , appointment.getCandidateDateList());
    }

    @Override
    @Transactional
    public void deleteAppointment(DeleteAppointmentReqDto reqDto, String email) {
        if (!customUserAppointmentMappingRepository.existUserMappedToAppointment(
                email, reqDto.getAppointmentCode(), UserRole.HOST)) {
            throw new AppointmentException(ErrorCode.USER_NOT_HOST);
        }

        Appointment appointment = appointmentRepository
                .findByAppointmentCode(reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT_BY_CODE));

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
        if (!customUserAppointmentMappingRepository.existUserMappedToAppointment(
                email, reqDto.getAppointmentCode(), UserRole.HOST)) {
            throw new AppointmentException(ErrorCode.USER_NOT_HOST);
        }

        Appointment appointment = appointmentRepository
                .findByAppointmentCode(reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT_BY_CODE));

        appointment.update(reqDto.getAppointmentName(), reqDto.getCandidateTimeTypeList(),
                reqDto.getAddress(), reqDto.getCandidateDateList());
    }

}
