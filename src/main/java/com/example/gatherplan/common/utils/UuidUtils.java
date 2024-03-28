package com.example.gatherplan.common.utils;

import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class UuidUtils {
    public String generateRandomString(int length) {
        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString().replace("-", "");

        return uuidString.substring(0, length);
    }
}
