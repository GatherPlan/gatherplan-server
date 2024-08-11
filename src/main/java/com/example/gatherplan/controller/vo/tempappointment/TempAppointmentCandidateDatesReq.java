package com.example.gatherplan.controller.vo.tempappointment;

import com.example.gatherplan.common.unit.TempUserInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "비회원의 약속 확정 후보 정보 요청")
public class TempAppointmentCandidateDatesReq {

    @NotBlank(message = "약속 코드는 공백이 될 수 없습니다.")
    @Schema(description = "약속 코드", example = "985a61f6f636")
    private String appointmentCode;

    @Valid
    @NotNull
    @Schema(description = "비회원 정보", example = "{\"nickname\": \"홍길동\",\"password\": \"abc1234\"}")
    private TempUserInfo tempUserInfo;

    @Min(value = 1, message = "페이지 수는 1 이상이어야 합니다.")
    @Schema(description = "약속 장소 검색 페이지 수", example = "1", type = "integer", requiredMode = REQUIRED)
    private int page;

    @Min(value = 1, message = "데이터 사이즈는 1 이상이어야 합니다.")
    @Schema(description = "약속 장소 검색 페이지 당 데이터 수", example = "10", type = "integer", requiredMode = REQUIRED)
    private int size;
}
