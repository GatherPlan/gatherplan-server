package com.example.gatherplan.controller.vo.tempappointment;

import com.example.gatherplan.common.unit.Address;
import com.example.gatherplan.common.unit.TempUserInfo;
import com.example.gatherplan.controller.validation.NotBlankAppointmentName;
import com.example.gatherplan.controller.validation.SizeCheckAppointmentName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "비회원의 약속 변경 요청 객체")
public class UpdateTempAppointmentReq {

    @Schema(description = "약속 코드", example = "985a61f6f636")
    private String appointmentCode;

    @Schema(description = "약속 이름", example = "맨땅에 헤딩")
    @NotBlank(message = "약속 이름은 공백이 될 수 없습니다.", groups = NotBlankAppointmentName.class)
    @Size(min = 2, max = 20, message = "약속 이름은 2자 이상 20자 이하여야 합니다.", groups = SizeCheckAppointmentName.class)
    private String appointmentName;

    @Schema(description = "공지사항", example = "점심약속입니다.")
    private String notice;

    @Schema(description = "약속 장소", example = "{\"locationType\": \"DETAIL_ADDRESS\"," +
            "\"fullAddress\": \"서울 성동구 성수동2가 289-30\", " +
            "\"placeName\": \"성수역 2호선 2번출구\", \"placeUrl\": \"http://place.map.kakao.com/7942972\"}")
    private Address address;

    @Schema(description = "약속 후보 날짜", example = "[\"2024-03-18\",\"2024-03-20\"]")
    private List<LocalDate> candidateDateList;

    @Schema(description = "비회원 정보", example = "{\"nickname\": \"홍길동\",\"password\": \"abc1234\"}")
    @Valid
    @NotNull
    private TempUserInfo tempUserInfo;
}
