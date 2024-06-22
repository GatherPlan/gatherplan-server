package com.example.gatherplan.common.unit;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.Comment;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TempUserInfo {

    @Comment("비회원 닉네임")
    @NotBlank(message = "이름은 공백이 될 수 없습니다.")
    @Size(min = 2, max = 6, message = "이름은 2자 이상 6자 이하여야 합니다.")
    private String nickname;

    @Comment("비회원 비밀번호")
    @NotBlank(message = "비밀번호는 공백이 될 수 없습니다.")
    @Size(min = 4, max = 12, message = "비밀번호는 4자 이상 12자 이하여야 합니다.")
    private String password;
}