package com.example.gatherplan.appointment.repository;

import com.example.gatherplan.appointment.repository.entity.Region;

import java.util.Optional;

public interface CustomRegionRepository {

    Optional<Region> findRegionByAddressName(String addressName);

}
