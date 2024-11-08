package com.example.gatherplan.controller.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "회원 정보 수정 요청")
public class UpdateUsereReq {

    @NotBlank(message = "이름은 공백이 될 수 없습니다.")
    @Size(min = 2, max = 6, message = "이름은 2자 이상 6자 이하여야 합니다.")
    @Schema(description = "회원 닉네임", example = "홍길동")
    private String name;
}
