package com.football.fmp.application.port.driving;

import com.football.fmp.domain.model.Record;

import java.util.List;
import java.util.Optional;

public interface ForRecord {
    Record create(Record record);

    List<Record> findAll();

    Optional<Record> findById(Long id);

    Record update(Long id, Record record);

    boolean deleteById(Long id);
}

// driving port
// implemented by -> MatchAppService, TeamAppService, RecordAppService , called by -> their respective REST controller
