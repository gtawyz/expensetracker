package com.comp4442.expensetracker.controller;

import com.comp4442.expensetracker.dto.ApiResponse;
import com.comp4442.expensetracker.dto.SummaryResponse;
import com.comp4442.expensetracker.service.SummaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Summary", description = "Monthly and yearly expense/income statistics")
@RestController
@RequestMapping("/api/summary")
public class SummaryController {

    private final SummaryService summaryService;

    public SummaryController(SummaryService summaryService) {
        this.summaryService = summaryService;
    }

    @Operation(summary = "Get summary for a specific month")
    @GetMapping("/monthly")
    public ResponseEntity<ApiResponse<SummaryResponse>> getMonthlySummary(
            @Parameter(description = "Year, e.g. 2026")
            @RequestParam(required = false) Integer year,
            @Parameter(description = "Month (1-12)")
            @RequestParam(required = false) Integer month) {

        // Default to current year/month if not provided
        LocalDate now = LocalDate.now();
        int resolvedYear = (year != null) ? year : now.getYear();
        int resolvedMonth = (month != null) ? month : now.getMonthValue();

        if (resolvedMonth < 1 || resolvedMonth > 12) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Month must be between 1 and 12"));
        }

        SummaryResponse summary = summaryService.getMonthlySummary(resolvedYear, resolvedMonth);
        return ResponseEntity.ok(ApiResponse.success(summary));
    }

    @Operation(summary = "Get summary for all 12 months in a year")
    @GetMapping("/yearly")
    public ResponseEntity<ApiResponse<List<SummaryResponse>>> getYearlySummary(
            @Parameter(description = "Year, e.g. 2026")
            @RequestParam(required = false) Integer year) {

        int resolvedYear = (year != null) ? year : LocalDate.now().getYear();

        List<SummaryResponse> summaries = summaryService.getYearlySummary(resolvedYear);
        return ResponseEntity.ok(ApiResponse.success(summaries));
    }

    @Operation(summary = "Get summary for the current month (shortcut)")
    @GetMapping("/monthly/current")
    public ResponseEntity<ApiResponse<SummaryResponse>> getCurrentMonthSummary() {
        LocalDate now = LocalDate.now();
        SummaryResponse summary = summaryService.getMonthlySummary(now.getYear(), now.getMonthValue());
        return ResponseEntity.ok(ApiResponse.success(summary));
    }
}