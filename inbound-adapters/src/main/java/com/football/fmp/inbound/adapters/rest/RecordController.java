package com.football.fmp.inbound.adapters.rest;

import com.football.fmp.application.port.driving.ForRecord;
import com.football.fmp.domain.model.Record;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/records")
public class RecordController {

    private final ForRecord forRecord;

    public RecordController(ForRecord forRecord) {
        this.forRecord = forRecord;
    }

    @PostMapping
    public ResponseEntity<Record> createRecord(@RequestBody Record record) {
        return ResponseEntity.status(HttpStatus.CREATED).body(forRecord.create(record));
    }

    @GetMapping
    public ResponseEntity<List<Record>> getAllRecords() {
        return ResponseEntity.ok(forRecord.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Record> getRecord(@PathVariable Long id) {
        return forRecord.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Record> updateRecord(@PathVariable Long id, @RequestBody Record record) {
        Record updated = forRecord.update(id, record);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        if (!forRecord.deleteById(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
