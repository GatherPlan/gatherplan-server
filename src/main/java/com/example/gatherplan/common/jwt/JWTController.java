package com.example.gatherplan.common.jwt;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JWTController {

    @PostMapping("/admin")
    public String abc(){

        return "okok";
    }
}
