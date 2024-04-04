package com.example.gatherplan.appointment.service.impl;

import com.example.gatherplan.appointment.dto.*;
import com.example.gatherplan.appointment.enums.AppointmentState;
import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.exception.AppointmentException;
import com.example.gatherplan.appointment.exception.UserException;
import com.example.gatherplan.appointment.mapper.AppointmentMapper;
import com.example.gatherplan.appointment.repository.*;
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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {

    private final UserRepository userRepository;
    private final AppointmentMapper appointmentMapper;
    private final AppointmentRepository appointmentRepository;
    private final CustomTempUserRepository customTempUserRepository;
    private final UserAppointmentMappingRepository userAppointmentMappingRepository;
    private final CustomUserAppointmentMappingRepository customUserAppointmentMappingRepository;
    private final CustomTempUserAppointmentMappingRepository customTempUserAppointmentMappingRepository;

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
    public List<AppointmentWithHostRespDto> retrieveAppointmentSearchList(
            AppointmentSearchReqDto reqDto, String email) {
        return customUserAppointmentMappingRepository.findAllAppointmentsWithHostByEmailAndKeyword(
                email, reqDto.getKeyword());
    }

    @Override
    public AppointmentInfoRespDto retrieveAppointmentInfo(AppointmentInfoReqDto reqDto, String email) {
        return customUserAppointmentMappingRepository
                .findAppointmentInfo(email, reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT_BY_CODE));
    }

    @Override
    public AppointmentParticipationInfoRespDto retrieveAppointmentParticipationInfo(AppointmentParticipationInfoReqDto reqDto, String email) {
        return customUserAppointmentMappingRepository
                .findAppointmentParticipationInfo(email, reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT_BY_CODE));
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

        Long appointmentId = appointment.getId();

        customTempUserRepository.deleteAllByAppointmentId(appointmentId);
        customTempUserAppointmentMappingRepository.deleteAllByAppointmentId(appointmentId);
        customUserAppointmentMappingRepository.deleteAllByAppointmentId(appointmentId);
        appointmentRepository.deleteById(appointmentId);
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
