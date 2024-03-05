package com.example.gatherplan.feature.join.service;

import com.example.gatherplan.feature.join.dto.LocalJoinEmailFormDto;
import com.example.gatherplan.feature.join.dto.LocalJoinFormDto;

public interface LocalJoinService {
    void sendAuthCodeProcess(LocalJoinEmailFormDto localJoinEmailFormDto);

    void validateLocalJoinFormProcess(LocalJoinFormDto localJoinFormDto);
}
