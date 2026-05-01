package com.comp4442.expensetracker.service;

import com.comp4442.expensetracker.dto.CreateExpenseRequest;
import com.comp4442.expensetracker.dto.ExpenseResponse;
import com.comp4442.expensetracker.dto.PagedResponse;
import com.comp4442.expensetracker.dto.UpdateExpenseRequest;
import com.comp4442.expensetracker.entity.ExpenseCategory;
import com.comp4442.expensetracker.entity.ExpenseType;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseService {
    // Defines the service operation that creates and returns a new expense or income record.
    ExpenseResponse createExpense(CreateExpenseRequest request);

    // Defines the service operation that returns all stored expense and income records.
    List<ExpenseResponse> getAllExpenses();

    // Defines the service operation that finds one record by its ID.
    ExpenseResponse getExpenseById(Long id);

    // Defines the service operation that updates one existing record by its ID.
    ExpenseResponse updateExpense(Long id, UpdateExpenseRequest request);

    // Defines the service operation that deletes one existing record by its ID.
    void deleteExpense(Long id);

    // Defines the service operation that filters records by optional type, category, and dates.
    List<ExpenseResponse> getExpensesByFilters(
            ExpenseType type,
            ExpenseCategory category,
            LocalDate startDate,
            LocalDate endDate
    );

    // Defines the service operation that filters records and returns them with pagination and sorting.
    PagedResponse<ExpenseResponse> getExpensesPaged(
            ExpenseType type,
            ExpenseCategory category,
            LocalDate startDate,
            LocalDate endDate,
            int page,
            int size,
            String sortBy,
            String sortDir
    );
}
