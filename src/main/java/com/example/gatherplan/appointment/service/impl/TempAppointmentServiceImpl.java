package com.example.gatherplan.appointment.service.impl;

import com.example.gatherplan.appointment.dto.CreateTempAppointmentReqDto;
import com.example.gatherplan.appointment.dto.TempAppointmentInfoReqDto;
import com.example.gatherplan.appointment.dto.TempAppointmentInfoRespDto;
import com.example.gatherplan.appointment.enums.AppointmentState;
import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.exception.AppointmentException;
import com.example.gatherplan.appointment.mapper.TempAppointmentMapper;
import com.example.gatherplan.appointment.repository.AppointmentRepository;
import com.example.gatherplan.appointment.repository.CustomTempUserAppointmentMappingRepository;
import com.example.gatherplan.appointment.repository.TempUserAppointmentMappingRepository;
import com.example.gatherplan.appointment.repository.TempUserRepository;
import com.example.gatherplan.appointment.repository.entity.Appointment;
import com.example.gatherplan.appointment.repository.entity.TempUser;
import com.example.gatherplan.appointment.repository.entity.TempUserAppointmentMapping;
import com.example.gatherplan.appointment.service.TempAppointmentService;
import com.example.gatherplan.common.exception.ErrorCode;
import com.example.gatherplan.common.utils.UuidUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TempAppointmentServiceImpl implements TempAppointmentService {

    private final TempAppointmentMapper tempAppointmentMapper;
    private final AppointmentRepository appointmentRepository;
    private final TempUserRepository tempUserRepository;
    private final TempUserAppointmentMappingRepository tempUserAppointmentMappingRepository;
    private final CustomTempUserAppointmentMappingRepository customTempUserAppointmentMappingRepository;

    @Override
    @Transactional
    public String registerTempAppointment(CreateTempAppointmentReqDto reqDto) {


        String appointmentCode = UuidUtils.generateRandomString(12);

        Appointment appointment = tempAppointmentMapper.to(reqDto, AppointmentState.UNCONFIRMED, appointmentCode);

        CreateTempAppointmentReqDto.TempUserInfo tempUserInfo = reqDto.getTempUserInfo();
        TempUser tempUser = TempUser.builder()
                .nickname(tempUserInfo.getNickname())
                .password(tempUserInfo.getPassword())
                .build();

        Long appointmentId = appointmentRepository.save(appointment).getId();
        Long tempUserId = tempUserRepository.save(tempUser).getId();

        TempUserAppointmentMapping tempUserAppointmentMapping = TempUserAppointmentMapping.builder()
                .appointmentSeq(appointmentId)
                .tempUserSeq(tempUserId)
                .userRole(UserRole.HOST)
                .build();

        tempUserAppointmentMappingRepository.save(tempUserAppointmentMapping);

        return appointmentCode;
    }

    @Override
    public TempAppointmentInfoRespDto retrieveAppointmentInfo(TempAppointmentInfoReqDto reqDto) {
        return customTempUserAppointmentMappingRepository
                .findAppointmentInfo(reqDto.getNickname(), reqDto.getPassword(), reqDto.getAppointmentCode())
                .orElseThrow(() -> new AppointmentException(ErrorCode.NOT_FOUND_APPOINTMENT));
    }

}
