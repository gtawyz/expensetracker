package com.comp4442.expensetracker.controller;

import com.comp4442.expensetracker.service.ExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/export")
@Tag(name = "Export", description = "Export expense data to CSV file")
public class ExportController {

    @Autowired
    private ExportService exportService;

    // Generates a CSV file from all expense records and sends it as a download response.
    @GetMapping("/csv")
    @Operation(summary = "Export all expenses to CSV", description = "Downloads all expense records as a CSV file")
    public ResponseEntity<byte[]> exportCsv() {
        ByteArrayOutputStream csvData = exportService.exportExpensesToCsv();

        String filename = "expenses_" + LocalDate.now() + ".csv";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csvData.toByteArray());
    }
}
