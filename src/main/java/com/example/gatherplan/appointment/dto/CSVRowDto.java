package com.example.gatherplan.appointment.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 로컬 DB에 법정동, 법정동 코드 관련 csv 파일을 불러오기 위한 임시 Dto입니다.
 */

@Getter
@Setter
public class CSVRowDto {
    private String code;
    private String address;

    public static CSVRowDto fromString(String row) {
        String[] values = row.split(",");
        CSVRowDto csvRowDto = new CSVRowDto();
        csvRowDto.setCode(values[0]);
        csvRowDto.setAddress(values[1]);
        return csvRowDto;
    }
}