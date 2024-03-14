package com.lofominhili.farmflow.repository;

import com.lofominhili.farmflow.entities.HarvestRateEntity;
import com.lofominhili.farmflow.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HarvestRateRepository extends JpaRepository<HarvestRateEntity, Long> {
    Optional<HarvestRateEntity> findByProduct(ProductEntity product);
}
