package com.football.fmp.application.service;

import com.football.fmp.application.port.driven.RecordDrivenPort;
import com.football.fmp.application.port.driving.ForRecord;
import com.football.fmp.domain.model.Record;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecordApplicationService implements ForRecord {

    private final RecordDrivenPort recordDrivenPort;

    public RecordApplicationService(RecordDrivenPort recordDrivenPort) {
        this.recordDrivenPort = recordDrivenPort;
    }

    @Override
    public Record create(Record record) {
        return recordDrivenPort.save(record);
    }

    @Override
    public List<Record> findAll() {
        return recordDrivenPort.findAll();
    }

    @Override
    public Optional<Record> findById(Long id) {
        return recordDrivenPort.findById(id);
    }

    @Override
    public Record update(Long id, Record record) {
        if (!recordDrivenPort.existsById(id)) {
            return null;
        }
        Record updated = new Record(id, record.playerId(), record.matchId(), record.fromMinutes(), record.toMinutes());
        return recordDrivenPort.save(updated);
    }

    @Override
    public boolean deleteById(Long id) {
        if (!recordDrivenPort.existsById(id)) {
            return false;
        }
        recordDrivenPort.deleteById(id);
        return true;
    }
}
