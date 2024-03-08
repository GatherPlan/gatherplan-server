package com.example.gatherplan.controller.vo.appointment;

import com.example.gatherplan.common.validation.NotBlankName;
import com.example.gatherplan.common.validation.SizeCheckName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class AppointmentFormReq {
    @NotBlank(message = "약속 이름은 공백이 될 수 없습니다.", groups = NotBlankName.class)
    @Size(min = 1, max = 12, message = "약속 이름은 1자 이상 12자 이하여야 합니다.", groups = SizeCheckName.class)
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
