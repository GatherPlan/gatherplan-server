package com.example.gatherplan.common.unit;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "확정 날짜 정보")
public class ConfirmedDateTime {

    @NotNull
    @Schema(description = "확정 날짜", example = "2024-04-20")
    private LocalDate confirmedDate;

    @NotNull
    @Schema(description = "확정 시작 시간", example = "18:00")
    private LocalTime confirmedStartTime;

    @NotNull
    @Schema(description = "확정 종료 시간", example = "20:00")
    private LocalTime confirmedEndTime;
}
