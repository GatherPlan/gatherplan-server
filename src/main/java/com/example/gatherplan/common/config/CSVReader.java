package com.example.gatherplan.common.config;

import com.example.gatherplan.appointment.dto.CSVRowDto;
import com.example.gatherplan.appointment.service.impl.RegionServiceImpl;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 로컬 DB에 법정동, 법정동 코드 관련 csv 파일을 불러오기 위한 임시 컴포넌트입니다.
 */
@Component
public class CSVReader {

    @Value("${csv.file.path}")
    private String csvFilePath;

    @Autowired
    private RegionServiceImpl regionService;

    @PostConstruct
    public void readAndSaveCSV() throws IOException {
        List<CSVRowDto> rows = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(csvFilePath), "CP949"))) {
            String line;
            while ((line = br.readLine()) != null) {
                CSVRowDto row = CSVRowDto.fromString(line);
                rows.add(row);
            }
        }
        regionService.saveFromCSV(rows);
    }
}