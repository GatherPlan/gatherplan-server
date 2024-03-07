package com.example.gatherplan.appointment.repository.entity.embedded;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class Address {
    private String city;
    private String district;
    private String street;
    private String buildingNumber;
}
