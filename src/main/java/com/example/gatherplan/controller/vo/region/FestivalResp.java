package com.example.gatherplan.controller.vo.region;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "축제 배너 응답")
public class FestivalResp {

    @Schema(description = "축제 이름", example = "가야문화축제")
    private String title;

    @Schema(description = "주소 명", example = "경상남도 김해시 분성로261번길 35 (봉황동)")
    private String addressName;

    @Schema(description = "장소 명", example = "수릉원")
    private String placeName;

    @Schema(description = "행사 시작 일", example = "2024-10-16")
    private LocalDate startDate;

    @Schema(description = "행사 종료 일", example = "2024-10-20")
    private LocalDate endDate;

    @Schema(description = "축제 대표 이미지 경로",
            example = "http://tong.visitkorea.or.kr/cms/resource/88/3357988_image2_1.jpg")
    private String imagePath;

    @Schema(description = "전화 번호", example = "055-330-6840")
    private String tel;
}
