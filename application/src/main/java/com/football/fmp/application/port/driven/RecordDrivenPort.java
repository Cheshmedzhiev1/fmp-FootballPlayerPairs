package com.football.fmp.application.port.driven;

import com.football.fmp.domain.model.Record;

import java.util.List;
import java.util.Optional;

public interface RecordDrivenPort {
    Record save(Record record);
    List<Record> findAll();
    Optional<Record> findById(Long id);
    boolean existsById(Long id);
    void deleteById(Long id);
}
