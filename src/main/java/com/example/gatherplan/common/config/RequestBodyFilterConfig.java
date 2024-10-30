package com.example.gatherplan.common.config;

import com.example.gatherplan.common.filter.RequestBodyWrapperFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RequestBodyFilterConfig {

    @Bean
    public RequestBodyWrapperFilter requestBodyWrapperFilter() {
        return new RequestBodyWrapperFilter();
    }
}
