package com.example.gatherplan.region.repository;

import com.example.gatherplan.region.repository.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegionRepository extends JpaRepository<Region, Long> {

    List<Region> findByAddressContaining(String keyword);

}
