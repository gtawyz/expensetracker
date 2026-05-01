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

    // Injects the repository used to read and write expense records in the database.
    public ExpenseServiceImpl(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    // Copies request data into a new entity, saves it, and returns it as an API response DTO.
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

    // Reads all database records and converts each entity into an API response DTO.
    @Override
    public List<ExpenseResponse> getAllExpenses() {
        return expenseRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Finds a record by ID or throws a 404 error when the ID does not exist.
    @Override
    public ExpenseResponse getExpenseById(Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Expense not found with id: " + id
                ));
        return mapToResponse(expense);
    }

    // Finds an existing record, replaces its editable values, saves it, and returns the updated DTO.
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

    // Finds an existing record and removes it from the database.
    @Override
    public void deleteExpense(Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Expense not found with id: " + id
                ));
        expenseRepository.delete(expense);
    }

    // Runs the repository's dynamic filter query and converts matching entities to response DTOs.
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

    // Applies safe sorting and pagination to the dynamic filter query, then wraps page metadata for the API.
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

    // Converts the database entity shape into the response object shape exposed by the API.
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
