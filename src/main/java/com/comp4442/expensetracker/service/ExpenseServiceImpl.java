package com.comp4442.expensetracker.service;

import com.comp4442.expensetracker.dto.CreateExpenseRequest;
import com.comp4442.expensetracker.dto.ExpenseResponse;
import com.comp4442.expensetracker.entity.Expense;
import com.comp4442.expensetracker.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @Override
    public ExpenseResponse createExpense(CreateExpenseRequest request) {
        Expense expense = new Expense();
        expense.setTitle(request.getTitle());
        expense.setDescription(request.getDescription());
        expense.setAmount(request.getAmount());
        expense.setType(request.getType());
        expense.setCategory(request.getCategory());
        expense.setTransactionDate(request.getTransactionDate());

        Expense savedExpense = expenseRepository.save(expense);

        return mapToResponse(savedExpense);
    }

    private ExpenseResponse mapToResponse(Expense expense) {
        ExpenseResponse response = new ExpenseResponse();
        response.setId(expense.getId());
        response.setTitle(expense.getTitle());
        response.setDescription(expense.getDescription());
        response.setAmount(expense.getAmount());
        response.setType(expense.getType());
        response.setCategory(expense.getCategory());
        response.setTransactionDate(expense.getTransactionDate());
        response.setCreatedAt(expense.getCreatedAt());
        return response;
    }
}