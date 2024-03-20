package com.example.gatherplan.controller.vo.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class AddressReq {
    private String level0;
    private String level1;
    private String level2;
    private String level3;
    private String level4;
    private String level5;
}