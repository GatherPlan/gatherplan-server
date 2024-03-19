package com.example.gatherplan.controller.vo.appointment.req;

import com.example.gatherplan.appointment.enums.CandidateTimeType;
import com.example.gatherplan.appointment.repository.entity.embedded.Address;
import com.example.gatherplan.appointment.repository.entity.embedded.CandidateTime;
import com.example.gatherplan.common.validation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "임시 회원 약속 만들기 요청 객체")
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateTempAppointmentReq {

    @Schema(description = "약속 이름")
    @NotBlank(message = "약속 이름은 공백이 될 수 없습니다.", groups = NotBlankName.class)
    @Size(min = 1, max = 12, message = "약속 이름은 1자 이상 12자 이하여야 합니다.", groups = SizeCheckName.class)
    private String appointmentName;

    @Schema(description = "약속 후보 시간 타입")
    private CandidateTimeType candidateTimeType;

    @Schema(description = "약속 후보 시간")
    private List<CandidateTime> candidateTimeList;

    @Schema(description = "약속 장소")
    private Address address;

    @Schema(description = "약속 메모")
    private String notice;

    @Schema(description = "약속 후보 날짜")
    private List<LocalDate> candidateDateList;

    @Schema(description = "임시 회원 이름")
    @NotBlank(message = "이름은 공백이 될 수 없습니다.", groups = NotBlankName.class)
    @Size(min = 2, max = 6, message = "이름은 2자 이상 6자 이하여야 합니다.", groups = SizeCheckName.class)
    private String name;

    @Schema(description = "임시 회원 비밀번호")
    @NotBlank(message = "비밀번호는 공백이 될 수 없습니다.", groups = NotBlankPassword.class)
    @Size(min = 4, max = 12, message = "비밀번호는 4자 이상 12자 이하여야 합니다.", groups = SizeCheckPassword.class)
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).+$", message = "비밀번호는 영문자, 숫자를 적어도 하나씩 포함해야 합니다.",
            groups = PatternCheckPassword.class)
    private String password;
}