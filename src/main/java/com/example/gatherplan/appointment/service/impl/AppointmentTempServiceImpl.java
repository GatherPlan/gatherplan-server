package com.example.gatherplan.appointment.service.impl;

import com.example.gatherplan.appointment.dto.CreateTempAppointmentReqDto;
import com.example.gatherplan.appointment.enums.AppointmentState;
import com.example.gatherplan.appointment.enums.UserRole;
import com.example.gatherplan.appointment.mapper.AppointmentTempMapper;
import com.example.gatherplan.appointment.repository.AppointmentRepository;
import com.example.gatherplan.appointment.repository.TempUserAppointmentMappingRepository;
import com.example.gatherplan.appointment.repository.TempUserRepository;
import com.example.gatherplan.appointment.repository.entity.Appointment;
import com.example.gatherplan.appointment.repository.entity.TempUser;
import com.example.gatherplan.appointment.repository.entity.TempUserAppointmentMapping;
import com.example.gatherplan.appointment.service.AppointmentTempService;
import com.example.gatherplan.common.utils.UuidUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentTempServiceImpl implements AppointmentTempService {

    private final AppointmentTempMapper appointmentTempMapper;
    private final AppointmentRepository appointmentRepository;
    private final TempUserRepository tempUserRepository;
    private final TempUserAppointmentMappingRepository tempUserAppointmentMappingRepository;

    @Override
    @Transactional
    public String registerTempAppointment(CreateTempAppointmentReqDto reqDto) {


        String appointmentCode = UuidUtils.generateRandomString(12);

        Appointment appointment = appointmentTempMapper.to(reqDto, AppointmentState.UNCONFIRMED, appointmentCode);

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
}
