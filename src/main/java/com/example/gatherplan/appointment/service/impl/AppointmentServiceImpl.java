package com.example.gatherplan.appointment.service.impl;

import com.example.gatherplan.appointment.dto.CreateAppointmentReqDto;
import com.example.gatherplan.appointment.enums.AppointmentState;
import com.example.gatherplan.appointment.enums.CandidateTimeType;
import com.example.gatherplan.appointment.repository.AppointmentRepository;
import com.example.gatherplan.appointment.repository.entity.Appointment;
import com.example.gatherplan.appointment.service.AppointmentService;
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
    public void registerAppointment(CreateAppointmentReqDto createAppointmentReqDto) {

        String name = createAppointmentReqDto.getName();
        String notice = createAppointmentReqDto.getNotice();
        String place = createAppointmentReqDto.getPlace();

        List<LocalTime> startTimes = new ArrayList<>();
        List<LocalTime> endTimes = new ArrayList<>();

        CandidateTimeType candidateTimeType = CandidateTimeType.SECTION;

        if (createAppointmentReqDto.getCustom().equals(true)) {
            startTimes.add(LocalTime.parse(createAppointmentReqDto.getCustomStartTime()));
            endTimes.add(LocalTime.parse(createAppointmentReqDto.getCustomEndTime()));
            candidateTimeType = CandidateTimeType.CUSTOM;
        } else {
            if (createAppointmentReqDto.getMorning().equals(true)) {
                startTimes.add(LocalTime.parse("08:00"));
                endTimes.add(LocalTime.parse("11:00"));
            }

            if (createAppointmentReqDto.getAfternoon().equals(true)) {
                startTimes.add(LocalTime.parse("11:00"));
                endTimes.add(LocalTime.parse("17:00"));
            }

            if (createAppointmentReqDto.getEvening().equals(true)) {
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
                .build();

        appointmentRepository.saveAppointment(appointment);
    }

}
