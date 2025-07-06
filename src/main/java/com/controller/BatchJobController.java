package com.controller;

import com.service.BatchJobService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/batch")
public class BatchJobController {

  private final BatchJobService batchJobService;

  public BatchJobController(BatchJobService batchJobService) {
    this.batchJobService = batchJobService;
  }

  @PostMapping("/import")
  public ResponseEntity<String> importAll() {
    String result = batchJobService.runImportAllJob();
    if (result.startsWith("Job started")) {
      return ResponseEntity.ok(result);
    } else if (result.contains("already running")) {
      return ResponseEntity.badRequest().body(result);
    } else {
      return ResponseEntity.internalServerError().body(result);
    }
  }
}
