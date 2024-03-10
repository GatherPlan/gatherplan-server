package com.example.gatherplan.appointment.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class CreateAppointmentReqDto {
    private String name;

    private Boolean morning;
    private Boolean afternoon;
    private Boolean evening;
    private Boolean custom;
    private String customStartTime;
    private String customEndTime;

    private String place;
    private String notice;
    private List<LocalDate> localDateList;
}
