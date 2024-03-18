package com.example.gatherplan.appointment.repository.entity.embedded;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class Address {
    @Schema(description = "국가")
    private String level0;
    @Schema(description = "시도명")
    private String level1;
    @Schema(description = "시군구명")
    private String level2;
    @Schema(description = "읍면동명")
    private String level3;
    @Schema(description = "법정리명")
    private String level4;
    @Schema(description = "건물번호")
    private String level5;
}
