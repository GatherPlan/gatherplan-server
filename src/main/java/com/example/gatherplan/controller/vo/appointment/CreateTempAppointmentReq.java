package com.example.gatherplan.controller.vo.appointment;

import com.example.gatherplan.appointment.enums.TimeType;
import com.example.gatherplan.common.unit.Address;
import com.example.gatherplan.controller.validation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "임시 회원 약속 만들기 요청 객체")
public class CreateTempAppointmentReq {

    @Schema(description = "약속 이름", example = "맨땅에 헤딩")
    @NotBlank(message = "약속 이름은 공백이 될 수 없습니다.", groups = NotBlankAppointmentName.class)
    @Size(min = 1, max = 12, message = "약속 이름은 1자 이상 12자 이하여야 합니다.", groups = SizeCheckAppointmentName.class)
    private String appointmentName;

    @Schema(description = "약속 후보 시간" +
            "<br> MORNING : 오전, AFTERNOON : 오후, EVENING : 저녁", example = "[\"MORNING\", \"EVENING\"]")
    private List<TimeType> candidateTimeTypeList;

    @Schema(description = "약속 장소", example = "{\"locationType\": \"DETAIL_ADDRESS\"," +
            "\"fullAddress\": \"서울 성동구 성수동2가 289-30\", " +
            "\"placeName\": \"성수역 2호선 2번출구\", \"placeUrl\": \"http://place.map.kakao.com/7942972\"}")
    private Address address;

    @Schema(description = "약속 후보 날짜", example = "[\"2024-03-18\",\"2024-03-20\"]")
    private List<LocalDate> candidateDateList;

    @Schema(description = "약속 후보 날짜", example = "{\"nickname\": \"홍길동\",\"password\": \"abc1234\"}")
    @Valid
    private TempUserInfo tempUserInfo;

    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Schema(description = "임시 회원 정보")
    public static class TempUserInfo {
        @Schema(description = "임시 회원 이름", example = "홍길동")
        @NotBlank(message = "이름은 공백이 될 수 없습니다.", groups = NotBlankNickName.class)
        @Size(min = 2, max = 6, message = "이름은 2자 이상 6자 이하여야 합니다.", groups = SizeCheckNickName.class)
        private String nickname;

        @Schema(description = "임시 회원 비밀번호", example = "abc1234")
        @NotBlank(message = "비밀번호는 공백이 될 수 없습니다.", groups = NotBlankPassword.class)
        @Size(min = 4, max = 12, message = "비밀번호는 4자 이상 12자 이하여야 합니다.", groups = SizeCheckPassword.class)
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).+$", message = "비밀번호는 영문자, 숫자를 적어도 하나씩 포함해야 합니다.",
                groups = PatternCheckPassword.class)
        private String password;
    }
}
