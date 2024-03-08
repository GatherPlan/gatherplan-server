package com.example.gatherplan.appointment.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LocalLoginFormDto {
    private String email;
    private String password;
}
