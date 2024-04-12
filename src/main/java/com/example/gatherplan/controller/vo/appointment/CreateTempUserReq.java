package com.example.gatherplan.controller.vo.appointment;

import com.example.gatherplan.controller.validation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "비회원 정보")
public class CreateTempUserReq {
    @Schema(description = "비회원 이름", example = "홍길동")
    @NotBlank(message = "이름은 공백이 될 수 없습니다.", groups = NotBlankNickName.class)
    @Size(min = 2, max = 6, message = "이름은 2자 이상 6자 이하여야 합니다.", groups = SizeCheckNickName.class)
    private String nickname;

    @Schema(description = "비회원 비밀번호", example = "abc1234")
    @NotBlank(message = "비밀번호는 공백이 될 수 없습니다.", groups = NotBlankPassword.class)
    @Size(min = 4, max = 12, message = "비밀번호는 4자 이상 12자 이하여야 합니다.", groups = SizeCheckPassword.class)
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).+$", message = "비밀번호는 영문자, 숫자를 적어도 하나씩 포함해야 합니다.",
            groups = PatternCheckPassword.class)
    private String password;

    @Schema(description = "약속 코드", example = "abcd1234efgh")
    @NotBlank(message = "약속 코드는 공백이 될 수 없습니다.")
    private String appointmentCode;
}
