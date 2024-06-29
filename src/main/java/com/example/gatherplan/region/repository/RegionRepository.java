package com.example.gatherplan.region.repository;

import com.example.gatherplan.region.repository.entity.Region;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepository extends JpaRepository<Region, Long> {

    Page<Region> findByAddressContaining(String keyword, Pageable pageable);

}
