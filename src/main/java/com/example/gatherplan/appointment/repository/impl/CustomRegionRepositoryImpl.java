package com.example.gatherplan.appointment.repository.impl;

import com.example.gatherplan.appointment.repository.CustomRegionRepository;
import com.example.gatherplan.appointment.repository.entity.Region;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Optional;

import static com.example.gatherplan.appointment.repository.entity.QRegion.region;

@Repository
public class CustomRegionRepositoryImpl implements CustomRegionRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public CustomRegionRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    public Optional<Region> findRegionByAddressName(String addressName) {
        String[] levels = addressName.split("\\s+");

        BooleanExpression[] booleanExpressions = Arrays.stream(levels)
                .map(region.address::contains)
                .toArray(BooleanExpression[]::new);

        Region result = jpaQueryFactory
                .selectFrom(region)
                .where(booleanExpressions)
                .fetchFirst();

        return Optional.ofNullable(result);
    }
}
