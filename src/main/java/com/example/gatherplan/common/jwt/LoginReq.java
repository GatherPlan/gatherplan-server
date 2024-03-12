package com.example.gatherplan.common.jwt;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginReq {

    private String username;
    private String password;
}
