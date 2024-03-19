package com.example.gatherplan.appointment.repository.entity.embedded;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class Address {
    private String level0;
    private String level1;
    private String level2;
    private String level3;
    private String level4;
    private String level5;
}
