package com.comp4442.expensetracker.service;

import com.comp4442.expensetracker.dto.SummaryResponse;

import java.util.List;

public interface SummaryService {

    // Summary for a specific month
    SummaryResponse getMonthlySummary(int year, int month);

    // Summary for all months in a specific year
    List<SummaryResponse> getYearlySummary(int year);
}