package com.example.gatherplan.controller.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "회원가입 요청")
public class CreateUserReq {

    @NotBlank(message = "이메일은 공백이 될 수 없습니다.")
    @Email(message = "이메일 형식이 맞지 않습니다.")
    @Schema(description = "이메일", example = "email@example.com")
    private String email;

    @NotBlank(message = "인증번호는 공백이 될 수 없습니다.")
    @Size(min = 6, max = 6, message = "인증번호는 6자입니다.")
    @Schema(description = "인증코드(6자리)", example = "123456")
    private String authCode;

    @NotBlank(message = "이름은 공백이 될 수 없습니다.")
    @Size(min = 2, max = 6, message = "이름은 2자 이상 6자 이하여야 합니다.")
    @Schema(description = "회원 닉네임", example = "홍길동")
    private String name;

    @NotBlank(message = "비밀번호는 공백이 될 수 없습니다.")
    @Size(min = 4, max = 12, message = "비밀번호는 4자 이상 12자 이하여야 합니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).+$", message = "비밀번호는 영문자, 숫자를 적어도 하나씩 포함해야 합니다.")
    @Schema(description = "회원 비밀번호", example = "abcd1234")
    private String password;
}
