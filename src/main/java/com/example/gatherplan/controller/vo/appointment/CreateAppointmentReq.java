package com.example.gatherplan.controller.vo.appointment;

import com.example.gatherplan.common.validation.NotBlankName;
import com.example.gatherplan.common.validation.SizeCheckName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@Schema(description = "약속 만들기 요청 객체")
public class CreateAppointmentReq {

    @Schema(description = "약속 이름", example = "맨땅에 헤딩")
    @NotBlank(message = "약속 이름은 공백이 될 수 없습니다.", groups = NotBlankName.class)
    @Size(min = 1, max = 12, message = "약속 이름은 1자 이상 12자 이하여야 합니다.", groups = SizeCheckName.class)
    private String name;

    private Boolean morning;
    private Boolean afternoon;
    private Boolean evening;
    private Boolean custom;
    private String customStartTime;
    private String customEndTime;

    @Schema(description = "약속 장소", example = "서울특별시 성동구")
    private String place;

    @Schema(description = "약속 메모", example = "점심약속이니깐 참고하세요.")
    private String notice;

    @Schema(description = "약속 후보 날짜", example = "24/06/07, 24/06/08, 24/06/10")
    private List<LocalDate> localDateList;
}
