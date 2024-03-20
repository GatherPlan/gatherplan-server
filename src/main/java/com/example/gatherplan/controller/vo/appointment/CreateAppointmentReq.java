package com.example.gatherplan.controller.vo.appointment;

import com.example.gatherplan.appointment.enums.CandidateTimeType;
import com.example.gatherplan.appointment.repository.entity.embedded.CandidateTime;
import com.example.gatherplan.common.validation.NotBlankName;
import com.example.gatherplan.common.validation.SizeCheckName;
import com.example.gatherplan.controller.vo.common.AddressReq;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
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
@Schema(description = "약속 만들기 요청 객체")
public class CreateAppointmentReq {

    @Schema(description = "약속 이름", example = "맨땅에 헤딩")
    @NotBlank(message = "약속 이름은 공백이 될 수 없습니다.", groups = NotBlankName.class)
    @Size(min = 1, max = 12, message = "약속 이름은 1자 이상 12자 이하여야 합니다.", groups = SizeCheckName.class)
    private String appointmentName;

    @Schema(description = "약속 후보 시간 타입", example = "SECTION")
    private CandidateTimeType candidateTimeType;

    @Schema(description = "약속 후보 시간", example = "[{\"appointmentCandidateTimeType\": \"CUSTOM\"," +
            " \"startTime\": \"16:53\", \"endTime\": \"17:53\"}]")
    private List<CandidateTime> candidateTimeList;

    @Schema(description = "약속 장소", example = "{\"level0\": \"대한민국\", \"level1\":" +
            " \"서울특별시\", \"level2\": \"성동구\", \"level3\": \"용답동\", \"level4\": \"\", \"level5\": \"13-22\"}")
    private AddressReq address;

    @Schema(description = "약속 메모", example = "점심 약속입니다.")
    private String notice;

    @Schema(description = "약속 후보 날짜", example = "[\"2024-03-18\",\"2024-03-20\"]")
    private List<LocalDate> candidateDateList;

}
