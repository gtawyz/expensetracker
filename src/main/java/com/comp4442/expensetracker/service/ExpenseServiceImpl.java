package com.comp4442.expensetracker.service;

import com.comp4442.expensetracker.dto.CreateExpenseRequest;
import com.comp4442.expensetracker.dto.ExpenseResponse;
import com.comp4442.expensetracker.dto.PagedResponse;
import com.comp4442.expensetracker.dto.UpdateExpenseRequest;
import com.comp4442.expensetracker.entity.Expense;
import com.comp4442.expensetracker.entity.ExpenseCategory;
import com.comp4442.expensetracker.entity.ExpenseType;
import com.comp4442.expensetracker.repository.ExpenseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

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

    @Override
    public List<ExpenseResponse> getAllExpenses() {
        return expenseRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public ExpenseResponse getExpenseById(Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Expense not found with id: " + id
                ));
        return mapToResponse(expense);
    }

    @Override
    public ExpenseResponse updateExpense(Long id, UpdateExpenseRequest request) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Expense not found with id: " + id
                ));

        expense.setTitle(request.getTitle());
        expense.setDescription(request.getDescription());
        expense.setAmount(request.getAmount());
        expense.setType(request.getType());
        expense.setCategory(request.getCategory());
        expense.setTransactionDate(request.getTransactionDate());

        Expense updatedExpense = expenseRepository.save(expense);
        return mapToResponse(updatedExpense);
    }

    @Override
    public void deleteExpense(Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Expense not found with id: " + id
                ));
        expenseRepository.delete(expense);
    }

    @Override
    public List<ExpenseResponse> getExpensesByFilters(
            ExpenseType type,
            ExpenseCategory category,
            LocalDate startDate,
            LocalDate endDate) {
        return expenseRepository.findByFilters(type, category, startDate, endDate)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public PagedResponse<ExpenseResponse> getExpensesPaged(
            ExpenseType type,
            ExpenseCategory category,
            LocalDate startDate,
            LocalDate endDate,
            int page,
            int size,
            String sortBy,
            String sortDir) {

        // Validate sortBy field to prevent injection
        List<String> allowedSortFields = List.of("transactionDate", "amount", "createdAt", "title");
        if (!allowedSortFields.contains(sortBy)) {
            sortBy = "transactionDate";
        }

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Expense> expensePage = expenseRepository.findByFiltersPaged(
                type, category, startDate, endDate, pageable);

        Page<ExpenseResponse> responsePage = expensePage.map(this::mapToResponse);

        return new PagedResponse<>(responsePage);
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