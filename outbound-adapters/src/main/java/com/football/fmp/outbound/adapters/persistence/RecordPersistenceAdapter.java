package com.football.fmp.outbound.adapters.persistence;

import com.football.fmp.application.port.driven.RecordDrivenPort;
import com.football.fmp.domain.model.Record;
import com.football.fmp.outbound.adapters.persistence.entity.RecordEntity;
import com.football.fmp.outbound.adapters.persistence.repository.RecordJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class RecordPersistenceAdapter implements RecordDrivenPort {

    private final RecordJpaRepository recordJpaRepository;

    public RecordPersistenceAdapter(RecordJpaRepository recordJpaRepository) {
        this.recordJpaRepository = recordJpaRepository;
    }

    @Override
    public Record save(Record record) {
        RecordEntity entity = toEntity(record);
        RecordEntity saved = recordJpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public List<Record> findAll() {
        return recordJpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public Optional<Record> findById(Long id) {
        return recordJpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return recordJpaRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        recordJpaRepository.deleteById(id);
    }

    private RecordEntity toEntity(Record record) {
        RecordEntity entity = new RecordEntity();
        entity.setId(record.id());
        entity.setPlayerId(record.playerId());
        entity.setMatchId(record.matchId());
        entity.setFromMinutes(record.fromMinutes());
        entity.setToMinutes(record.toMinutes());
        return entity;
    }

    private Record toDomain(RecordEntity entity) {
        return new Record(
                entity.getId(),
                entity.getPlayerId(),
                entity.getMatchId(),
                entity.getFromMinutes(),
                entity.getToMinutes()
        );
    }
}
