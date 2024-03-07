package com.example.gatherplan.controller.vo.member;

import com.example.gatherplan.common.validation.NotBlankEmail;
import com.example.gatherplan.common.validation.PatternCheckEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
@Getter
public class LocalJoinEmailReq {
    @NotBlank(message = "이메일은 공백이 될 수 없습니다.", groups = NotBlankEmail.class)
    @Email(message = "이메일 형식이 맞지 않습니다.", groups = PatternCheckEmail.class)
    private String email;
}
