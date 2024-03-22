package com.example.gatherplan.appointment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CSVRowDto {
    private String regionName;

    public static CSVRowDto fromString(String row) {
        String[] values = row.split(",");
        CSVRowDto csvRowDto = new CSVRowDto();
        csvRowDto.setRegionName(values[0]);
        return csvRowDto;
    }
}