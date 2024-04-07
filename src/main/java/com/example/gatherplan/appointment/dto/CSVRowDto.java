package com.example.gatherplan.appointment.dto;

import lombok.*;

/**
 * 로컬 DB에 법정동, 법정동 코드 관련 csv 파일을 불러오기 위한 임시 Dto입니다.
 */

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CSVRowDto {
    private String code;
    private String address;

    public static CSVRowDto fromString(String row) {
        String[] values = row.split(",");
        return CSVRowDto.builder()
                .code(values[0])
                .address(values[1])
                .build();
    }
}