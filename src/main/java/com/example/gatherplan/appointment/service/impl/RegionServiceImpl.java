package com.example.gatherplan.appointment.service.impl;

import com.example.gatherplan.appointment.dto.CSVRowDto;
import com.example.gatherplan.appointment.repository.RegionRepository;
import com.example.gatherplan.appointment.repository.entity.Region;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class RegionServiceImpl {

    private RegionRepository regionRepository;

    @Transactional
    public void saveFromCSV(List<CSVRowDto> rows) {
        List<Region> entities = new ArrayList<>();
        for (CSVRowDto row : rows) {
            Region entity = Region.builder()
                    .regionCode(row.getRegionCode())
                    .regionName(row.getRegionName())
                    .build();
            entities.add(entity);
        }

        regionRepository.saveAll(entities);
    }
}
