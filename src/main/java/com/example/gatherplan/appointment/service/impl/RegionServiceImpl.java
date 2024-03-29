package com.example.gatherplan.appointment.service.impl;

import com.example.gatherplan.appointment.dto.CSVRowDto;
import com.example.gatherplan.appointment.repository.RegionRepository;
import com.example.gatherplan.appointment.repository.entity.Region;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 로컬 DB에 법정동, 법정동 코드 관련 csv 파일을 불러오기 위한 임시 서비스 클래스입니다.
 */
@Service
@AllArgsConstructor
public class RegionServiceImpl {

    private RegionRepository regionRepository;

    @Transactional
    public void saveFromCSV(List<CSVRowDto> rows) {
        List<Region> entities = new ArrayList<>();
        for (CSVRowDto row : rows) {
            Region entity = Region.builder()
                    .code(row.getCode())
                    .address(row.getAddress())
                    .build();
            entities.add(entity);
        }

        regionRepository.saveAll(entities);
    }
}
