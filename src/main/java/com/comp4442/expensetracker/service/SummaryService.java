package com.comp4442.expensetracker.service;

import com.comp4442.expensetracker.dto.SummaryResponse;

import java.util.List;

public interface SummaryService {

    // Defines the service operation that calculates totals for one specific month.
    SummaryResponse getMonthlySummary(int year, int month);

    // Defines the service operation that calculates totals for every month in one year.
    List<SummaryResponse> getYearlySummary(int year);
}
