package com.example.gatherplan.appointment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {
    private String level0;
    private String level1;
    private String level2;
    private String level3;
    private String level4;
    private String level5;
}
