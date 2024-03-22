package com.example.gatherplan.controller.vo.appointment;

import com.example.gatherplan.appointment.repository.entity.Region;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "��� ��� �˻� ���� ��ü")
public class SearchPlaceResp {

    @Schema(description = "Ű���忡 �´� ���� ��� (������)", example = "[����Ư���� ������ ������1��, ����Ư���� ������ ������2��]")
    List<Region> regionList;
}
