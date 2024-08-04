package com.example.gatherplan.controller.vo.appointment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "약속 키워드 검색 요청 객체")
public class AppointmentSearchReq {

    // @NotBlank(message = "키워드는 공백일 수 없습니다")
    @Size(min = 2, message = "키워드는 2자 이상이어야합니다.")
    @Schema(description = "약속 이름 검색 키워드", example = "세 얼간이")
    private String keyword;

    @Min(value = 1, message = "페이지 수는 1 이상이어야 합니다.")
    @Schema(description = "약속 장소 검색 페이지 수", example = "1", type = "integer", requiredMode = REQUIRED)
    private int page;

    @Min(value = 1, message = "데이터 사이즈는 1 이상이어야 합니다.")
    @Max(value = 15, message = "데이터 사이즈는 15 이하이어야 합니다.")
    @Schema(description = "약속 장소 검색 페이지 당 데이터 수", example = "10", type = "integer", requiredMode = REQUIRED)
    private int size;
}
