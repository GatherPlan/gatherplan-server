package com.example.gatherplan.controller.vo.appointment;

import com.example.gatherplan.common.validation.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginMemberReq {
    @NotBlank(message = "이메일은 공백이 될 수 없습니다.", groups = NotBlankEmail.class)
    @Email(message = "이메일 형식이 맞지 않습니다.", groups = PatternCheckEmail.class)
    private String email;

    @NotBlank(message = "비밀번호는 공백이 될 수 없습니다.", groups = NotBlankPassword.class)
    @Size(min = 4, max = 12, message = "비밀번호는 4자 이상 12자 이하여야 합니다.", groups = SizeCheckPassword.class)
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).+$", message = "비밀번호는 영문자, 숫자를 적어도 하나씩 포함해야 합니다.", groups = PatternCheckPassword.class)
    private String password;
}
