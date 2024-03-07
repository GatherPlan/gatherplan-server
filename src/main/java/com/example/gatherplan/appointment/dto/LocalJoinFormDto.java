package com.example.gatherplan.appointment.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LocalJoinFormDto {

    private String email;

    private String authCode;

    private String name;

    private String password;
}
