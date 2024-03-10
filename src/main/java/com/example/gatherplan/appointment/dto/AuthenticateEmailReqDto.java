package com.example.gatherplan.appointment.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticateEmailReqDto {
    private String email;
}
