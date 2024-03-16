package com.example.gatherplan.common.jwt;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginReq {

    private String email;
    private String name;
    private String password;
}
