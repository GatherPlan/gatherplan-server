package com.example.gatherplan.appointment.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class AddressUtils {

    public static final Map<String, String> fullAddressDoMappingTable =
            Map.of(
                    "경기", "경기도",
                    "강원특별자치도", "강원특별자치도",
                    "충북", "충청북도",
                    "충남", "충청남도",
                    "전북특별자치도", "전북특별자치도",
                    "전남", "전라남도",
                    "경북", "경상북도",
                    "경남", "경상남도"
            );

    public static final Map<String, String> fullAddressSiMappingTable =
            Map.of(
                    "서울", "서울특별시",
                    "인천", "인천광역시",
                    "대전", "대전광역시",
                    "대구", "대구광역시",
                    "울산", "울산광역시",
                    "부산", "부산광역시",
                    "광주", "광주광역시",
                    "세종", "세종특별자치시"
            );

    public String processAddressName(String addressName) {
        Map<String, String> fullAddressMappingTable = new HashMap<>(fullAddressDoMappingTable);
        fullAddressMappingTable.putAll(fullAddressSiMappingTable);

        String[] words = Arrays.stream(addressName.split("\\s+"))
                .limit(3)
                .toArray(String[]::new);

        String beforeProcessWord = words[0];
        words[0] = fullAddressMappingTable.getOrDefault(beforeProcessWord, beforeProcessWord);

        return StringUtils.join(words, " ");
    }
}
