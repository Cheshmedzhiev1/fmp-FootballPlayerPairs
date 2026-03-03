package com.football.fmp.outbound.adapters.persistence.repository;

import com.football.fmp.outbound.adapters.persistence.entity.MatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchJpaRepository extends JpaRepository<MatchEntity, Long> {
}
