package com.example.gatherplan.appointment.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateTemporaryMemberReqDto {
    private String name;

    private String password;
}
