package com.example.gatherplan.controller.vo.appointment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddressReq {
    private String level0;
    private String level1;
    private String level2;
    private String level3;
    private String level4;
    private String level5;
}