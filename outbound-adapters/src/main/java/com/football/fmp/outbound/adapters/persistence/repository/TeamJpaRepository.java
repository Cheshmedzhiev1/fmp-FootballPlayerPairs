package com.football.fmp.outbound.adapters.persistence.repository;

import com.football.fmp.outbound.adapters.persistence.entity.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamJpaRepository extends JpaRepository<TeamEntity, Long> {
}
