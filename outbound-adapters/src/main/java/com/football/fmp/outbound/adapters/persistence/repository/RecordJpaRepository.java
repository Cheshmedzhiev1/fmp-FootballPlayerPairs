package com.football.fmp.outbound.adapters.persistence.repository;

import com.football.fmp.outbound.adapters.persistence.entity.RecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordJpaRepository extends JpaRepository<RecordEntity, Long> {
}
