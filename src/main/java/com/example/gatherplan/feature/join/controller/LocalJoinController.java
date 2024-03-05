package com.example.gatherplan.feature.join.controller;

import com.example.gatherplan.common.dto.response.SuccessResponse;
import com.example.gatherplan.feature.join.dto.LocalJoinEmailFormDto;
import com.example.gatherplan.feature.join.dto.LocalJoinFormDto;
import com.example.gatherplan.feature.join.service.LocalJoinService;
import com.example.gatherplan.feature.join.validation.LocalJoinFormValidationSequence;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class LocalJoinController {

    private final LocalJoinService localJoinService;

    @PostMapping("/api/v1/join/send-authcode")
    public ResponseEntity<SuccessResponse> sendAuthCode(@Validated(value = LocalJoinFormValidationSequence.class) @RequestBody LocalJoinEmailFormDto localJoinEmailFormDto){
        localJoinService.sendAuthCodeProcess(localJoinEmailFormDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponse.of());
    }

    @PostMapping("/api/v1/join/validate-form")
    public ResponseEntity<SuccessResponse> validateLocalJoinForm(@Validated(value = LocalJoinFormValidationSequence.class) @RequestBody LocalJoinFormDto localJoinFormDto){
        localJoinService.validateLocalJoinFormProcess(localJoinFormDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponse.of());
    }
}
