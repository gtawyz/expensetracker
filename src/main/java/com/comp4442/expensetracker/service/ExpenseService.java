package com.comp4442.expensetracker.service;

import com.comp4442.expensetracker.dto.CreateExpenseRequest;
import com.comp4442.expensetracker.dto.ExpenseResponse;
import com.comp4442.expensetracker.dto.UpdateExpenseRequest;
import com.comp4442.expensetracker.entity.ExpenseCategory;
import com.comp4442.expensetracker.entity.ExpenseType;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseService {
    ExpenseResponse createExpense(CreateExpenseRequest request);
    List<ExpenseResponse> getAllExpenses();
    ExpenseResponse getExpenseById(Long id);
    ExpenseResponse updateExpense(Long id, UpdateExpenseRequest request);
    void deleteExpense(Long id);

    // New: filter with optional parameters
    List<ExpenseResponse> getExpensesByFilters(
            ExpenseType type,
            ExpenseCategory category,
            LocalDate startDate,
            LocalDate endDate
    );
}