package com.example.gatherplan.region.repository;

import com.example.gatherplan.region.repository.entity.Region;

import java.util.Optional;

public interface CustomRegionRepository {

    Optional<Region> findRegionByAddressName(String addressName);

}
