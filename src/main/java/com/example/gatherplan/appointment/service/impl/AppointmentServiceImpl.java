package com.example.gatherplan.appointment.service.impl;

import com.example.gatherplan.appointment.dto.AppointmentFormDto;
import com.example.gatherplan.appointment.enums.AppointmentState;
import com.example.gatherplan.appointment.enums.CandidateTimeType;
import com.example.gatherplan.appointment.repository.AppointmentRepository;
import com.example.gatherplan.appointment.repository.entity.Appointment;
import com.example.gatherplan.appointment.service.AppointmentService;
import com.example.gatherplan.common.exception.BusinessException;
import com.example.gatherplan.common.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

    @Override
    @Transactional
    public void setInformation(AppointmentFormDto appointmentFormDto, HttpServletRequest httpServletRequest) {
        HttpSession httpSession = httpServletRequest.getSession(false);

        if (httpSession == null) {
            throw new BusinessException(ErrorCode.AUTHENTICATION_FAIL, "약속 만들기는 로그인이 필요합니다.");
        }

        String name = appointmentFormDto.getName();
        String notice = appointmentFormDto.getNotice();
        String place = appointmentFormDto.getPlace();

        List<LocalTime> startTimes = new ArrayList<>();
        List<LocalTime> endTimes = new ArrayList<>();

        CandidateTimeType candidateTimeType = CandidateTimeType.SECTION;

        if (appointmentFormDto.getCustom().equals(true)) {
            startTimes.add(LocalTime.parse(appointmentFormDto.getCustomStartTime()));
            endTimes.add(LocalTime.parse(appointmentFormDto.getCustomEndTime()));
            candidateTimeType = CandidateTimeType.CUSTOM;
        } else {
            if (appointmentFormDto.getMorning().equals(true)) {
                startTimes.add(LocalTime.parse("08:00"));
                endTimes.add(LocalTime.parse("11:00"));
            }

            if (appointmentFormDto.getAfternoon().equals(true)) {
                startTimes.add(LocalTime.parse("11:00"));
                endTimes.add(LocalTime.parse("17:00"));
            }

            if (appointmentFormDto.getEvening().equals(true)) {
                startTimes.add(LocalTime.parse("17:00"));
                endTimes.add(LocalTime.parse("22:00"));
            }
        }

        Appointment appointment = Appointment.builder()
                .name(name)
                .notice(notice)
                .place(place)
                .appointmentState(AppointmentState.UNCONFIRMED)
                .candidateStartTimes(startTimes)
                .candidateEndTimes(endTimes)
                .candidateTimeType(candidateTimeType)
                .candidateDates(appointmentFormDto.getLocalDateList())
                .build();

        appointmentRepository.saveAppointment(appointment);
    }

}
