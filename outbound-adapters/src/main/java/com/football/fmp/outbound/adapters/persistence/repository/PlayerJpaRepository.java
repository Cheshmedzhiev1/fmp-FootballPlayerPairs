package com.football.fmp.outbound.adapters.persistence.repository;

import com.football.fmp.outbound.adapters.persistence.entity.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerJpaRepository extends JpaRepository<PlayerEntity, Long> {
}
