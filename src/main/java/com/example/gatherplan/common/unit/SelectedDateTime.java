package com.example.gatherplan.common.unit;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Embeddable
@Schema(description = "사용자 선택 날짜 및 시간 정보")
public class SelectedDateTime {

    @NotNull
    @Schema(description = "선택 날짜", example = "2024-04-20")
    private LocalDate selectedDate;

    @NotNull
    @Schema(description = "선택 시작 시간", example = "18:00")
    private LocalTime selectedStartTime;

    @NotNull
    @Schema(description = "선택 종료 시간", example = "20:00")
    private LocalTime selectedEndTime;
}
