package com.football.fmp.controller;

import com.football.fmp.data.entity.RecordEntity;
import com.football.fmp.data.repository.RecordRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/records")
public class RecordController {

    private final RecordRepository recordRepository;

    public RecordController(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    @PostMapping
    public ResponseEntity<RecordEntity> createRecord(@RequestBody RecordEntity record) {
        RecordEntity saved = recordRepository.save(record);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<RecordEntity>> getAllRecords() {
        return ResponseEntity.ok(recordRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecordEntity> getRecord(@PathVariable Long id) {
        return recordRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecordEntity> updateRecord(@PathVariable Long id, @RequestBody RecordEntity record) {
        if (!recordRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        record.setId(id);
        return ResponseEntity.ok(recordRepository.save(record));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        if (!recordRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        recordRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}