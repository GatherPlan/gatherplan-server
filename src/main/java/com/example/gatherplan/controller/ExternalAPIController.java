package com.example.gatherplan.controller;

import com.example.gatherplan.appointment.service.impl.LongWhetherService;
import com.example.gatherplan.appointment.service.impl.ShortWhetherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class ExternalAPIController {

    private final LongWhetherService longWhetherService;
    private final ShortWhetherService shortWhetherService;

    @PostMapping("/call/longwhether")
    public Mono<String> callLongWhetherAPI() {
        return longWhetherService.callExternalAPI();
    }

    @PostMapping("/call/shortwhether")
    public Mono<String> callShortWhetherAPI() {
        return shortWhetherService.callExternalAPI();
    }
}