package com.comp4442.expensetracker.controller;

import com.comp4442.expensetracker.dto.ApiResponse;
import com.comp4442.expensetracker.dto.SummaryResponse;
import com.comp4442.expensetracker.service.SummaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/summary")
public class SummaryController {

    private final SummaryService summaryService;

    public SummaryController(SummaryService summaryService) {
        this.summaryService = summaryService;
    }

    // GET /api/summary/monthly?year=2026&month=4
    @GetMapping("/monthly")
    public ResponseEntity<ApiResponse<SummaryResponse>> getMonthlySummary(
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now().getYear()}") int year,
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now().getMonthValue()}") int month) {

        // Validate month range
        if (month < 1 || month > 12) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Month must be between 1 and 12"));
        }

        SummaryResponse summary = summaryService.getMonthlySummary(year, month);
        return ResponseEntity.ok(ApiResponse.success(summary));
    }

    // GET /api/summary/yearly?year=2026
    @GetMapping("/yearly")
    public ResponseEntity<ApiResponse<List<SummaryResponse>>> getYearlySummary(
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now().getYear()}") int year) {

        List<SummaryResponse> summaries = summaryService.getYearlySummary(year);
        return ResponseEntity.ok(ApiResponse.success(summaries));
    }

    // GET /api/summary/monthly/current  (current month shortcut)
    @GetMapping("/monthly/current")
    public ResponseEntity<ApiResponse<SummaryResponse>> getCurrentMonthSummary() {
        LocalDate now = LocalDate.now();
        SummaryResponse summary = summaryService.getMonthlySummary(now.getYear(), now.getMonthValue());
        return ResponseEntity.ok(ApiResponse.success(summary));
    }
}