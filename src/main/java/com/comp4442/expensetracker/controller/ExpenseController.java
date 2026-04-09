package com.comp4442.expensetracker.controller;

import com.comp4442.expensetracker.dto.ApiResponse;
import com.comp4442.expensetracker.dto.CreateExpenseRequest;
import com.comp4442.expensetracker.dto.ExpenseResponse;
import com.comp4442.expensetracker.dto.UpdateExpenseRequest;
import com.comp4442.expensetracker.entity.ExpenseCategory;
import com.comp4442.expensetracker.entity.ExpenseType;
import com.comp4442.expensetracker.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ExpenseResponse>> createExpense(
            @Valid @RequestBody CreateExpenseRequest request) {
        ExpenseResponse created = expenseService.createExpense(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Expense created successfully", created));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ExpenseResponse>>> getAllExpenses() {
        List<ExpenseResponse> expenses = expenseService.getAllExpenses();
        return ResponseEntity.ok(ApiResponse.success(expenses));
    }

    // New: filter endpoint - all params are optional
    // Example: GET /api/expenses/filter?type=EXPENSE&category=FOOD&startDate=2026-01-01&endDate=2026-04-09
    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<List<ExpenseResponse>>> getExpensesByFilters(
            @RequestParam(required = false) ExpenseType type,
            @RequestParam(required = false) ExpenseCategory category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<ExpenseResponse> expenses = expenseService.getExpensesByFilters(type, category, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(expenses));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ExpenseResponse>> getExpenseById(@PathVariable Long id) {
        ExpenseResponse expense = expenseService.getExpenseById(id);
        return ResponseEntity.ok(ApiResponse.success(expense));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ExpenseResponse>> updateExpense(
            @PathVariable Long id,
            @Valid @RequestBody UpdateExpenseRequest request) {
        ExpenseResponse updated = expenseService.updateExpense(id, request);
        return ResponseEntity.ok(ApiResponse.success("Expense updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.ok(ApiResponse.success("Expense deleted successfully", null));
    }
}