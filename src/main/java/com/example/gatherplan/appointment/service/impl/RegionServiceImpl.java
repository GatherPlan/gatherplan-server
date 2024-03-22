package com.example.gatherplan.appointment.service.impl;

import com.example.gatherplan.appointment.dto.CSVRowDto;
import com.example.gatherplan.appointment.repository.RegionRepository;
import com.example.gatherplan.appointment.repository.entity.Region;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegionServiceImpl {

    @Autowired
    private RegionRepository regionRepository;

    @Transactional
    public void saveFromCSV(List<CSVRowDto> rows) {
        List<Region> entities = new ArrayList<>();
        for (CSVRowDto row : rows) {
            Region entity = Region.builder()
                    .regionName(row.getRegionName())
                    .build();
            entities.add(entity);
        }

        regionRepository.saveAll(entities);
    }
}
