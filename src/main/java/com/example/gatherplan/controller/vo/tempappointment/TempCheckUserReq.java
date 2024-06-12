package com.example.gatherplan.controller.vo.tempappointment;

import com.example.gatherplan.common.unit.TempUserInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "비회원의 특정 약속 포함 여부 확인 요청 객체")
public class TempCheckUserReq {
    @Valid
    @Schema(description = "비회원 정보", example = "{\"nickname\": \"홍길동\",\"password\": \"abc1234\"}")
    private TempUserInfo tempUserInfo;

    @Schema(description = "약속 코드", example = "abcd1234efgh")
    @NotBlank(message = "약속 코드는 공백이 될 수 없습니다.")
    private String appointmentCode;
}
