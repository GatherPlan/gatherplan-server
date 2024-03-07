package com.example.gatherplan.appointment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocalJoinFormDto {

    private String email;

    private String authCode;

    private String name;

    private String password;
}
