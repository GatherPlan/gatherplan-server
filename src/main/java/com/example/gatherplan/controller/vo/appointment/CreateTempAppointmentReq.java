package com.example.gatherplan.controller.vo.appointment;

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

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "임시 회원 약속 만들기 요청 객체")
public class CreateTempAppointmentReq {

    @Schema(description = "약속 이름")
    @NotBlank(message = "약속 이름은 공백이 될 수 없습니다.", groups = NotBlankName.class)
    @Size(min = 1, max = 12, message = "약속 이름은 1자 이상 12자 이하여야 합니다.", groups = SizeCheckName.class)
    private String appointmentName;

    @Schema(description = "약속 후보 시간 타입", example = "SECTION")
    private CandidateTimeType candidateTimeType;

    @Schema(description = "약속 후보 시간", example = "[{MORNING, 06:00, 12:00},{AFTERNOON, 12:00, 18:00}]")
    private List<CandidateTime> candidateTimeList;

    @Schema(description = "약속 장소", example = "{대한민국, 성동구, 용답동, 13-22}")
    private Address address;

    @Schema(description = "약속 메모", example = "점심 약속입니다.")
    private String notice;

    @Schema(description = "약속 후보 날짜", example = "[2024-03-18,2024-03-20]")
    private List<LocalDate> candidateDateList;

    @Schema(description = "임시 회원 이름", example = "홍길동")
    @NotBlank(message = "이름은 공백이 될 수 없습니다.", groups = NotBlankName.class)
    @Size(min = 2, max = 6, message = "이름은 2자 이상 6자 이하여야 합니다.", groups = SizeCheckName.class)
    private String name;

    @Schema(description = "임시 회원 비밀번호", example = "abcd1234")
    @NotBlank(message = "비밀번호는 공백이 될 수 없습니다.", groups = NotBlankPassword.class)
    @Size(min = 4, max = 12, message = "비밀번호는 4자 이상 12자 이하여야 합니다.", groups = SizeCheckPassword.class)
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).+$", message = "비밀번호는 영문자, 숫자를 적어도 하나씩 포함해야 합니다.",
            groups = PatternCheckPassword.class)
    private String password;
}
