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
    private final TempUserAppointmentMappingRepository tempUserAppointmentMappingRepository;

    @Override
    @Transactional
    public String registerAppointment(CreateAppointmentReqDto reqDto, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_USER));

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
    public boolean isUserParticipated(String appointmentCode, String email) {
        return customUserAppointmentMappingRepository.existUserMappedToAppointment(
                email, appointmentCode, UserRole.GUEST);
    }

    @Override
    public List<AppointmentWithHostRespDto> retrieveAppointmentList(String email) {
        return customUserAppointmentMappingRepository.findAllAppointmentsWithHostByEmail(email);
    }

    @Override
    public List<AppointmentWithHostRespDto> retrieveAppointmentSearchList(String keyword, String email) {
        return customUserAppointmentMappingRepository.findAllAppointmentsWithHostByEmailAndKeyword(
                email, keyword);
    }

    @Override
    public AppointmentInfoRespDto retrieveAppointmentInfo(String appointmentCode, String email) {
        return customUserAppointmentMappingRepository
                .findAppointmentInfo(email, appointmentCode)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));
    }

    @Override
    public AppointmentParticipationInfoRespDto retrieveAppointmentParticipationInfo(String appointmentCode, String email) {
        return customUserAppointmentMappingRepository
                .findAppointmentParticipationInfo(email, appointmentCode)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));
    }

    @Override
    @Transactional
    public void deleteAppointment(String appointmentCode, String email) {
        if (!customUserAppointmentMappingRepository.existUserMappedToAppointment(
                email, appointmentCode, UserRole.HOST)) {
            throw new AppointmentException(ErrorCode.USER_NOT_HOST);
        }

        Appointment appointment = appointmentRepository
                .findByAppointmentCode(appointmentCode)
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        Long appointmentId = appointment.getId();

        customTempUserRepository.deleteAllByAppointmentId(appointmentId);
        tempUserAppointmentMappingRepository.deleteAllByAppointmentSeq(appointmentId);
        userAppointmentMappingRepository.deleteAllByAppointmentSeq(appointmentId);
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
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));

        appointment.update(reqDto.getAppointmentName(), reqDto.getCandidateTimeTypeList(),
                reqDto.getAddress(), reqDto.getCandidateDateList());
    }

}
