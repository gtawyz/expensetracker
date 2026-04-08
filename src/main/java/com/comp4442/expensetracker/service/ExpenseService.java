package com.comp4442.expensetracker.service;

import com.comp4442.expensetracker.dto.CreateExpenseRequest;
import com.comp4442.expensetracker.dto.ExpenseResponse;

public interface ExpenseService {
    ExpenseResponse createExpense(CreateExpenseRequest request);
}