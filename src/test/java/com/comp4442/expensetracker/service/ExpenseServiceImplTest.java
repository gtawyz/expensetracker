package com.comp4442.expensetracker.service;

import com.comp4442.expensetracker.dto.CreateExpenseRequest;
import com.comp4442.expensetracker.dto.ExpenseResponse;
import com.comp4442.expensetracker.dto.UpdateExpenseRequest;
import com.comp4442.expensetracker.entity.Expense;
import com.comp4442.expensetracker.entity.ExpenseCategory;
import com.comp4442.expensetracker.entity.ExpenseType;
import com.comp4442.expensetracker.repository.ExpenseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ExpenseService Unit Tests")
class ExpenseServiceImplTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @InjectMocks
    private ExpenseServiceImpl expenseService;

    private Expense sampleExpense;
    private Expense sampleIncome;
    private CreateExpenseRequest createRequest;
    private UpdateExpenseRequest updateRequest;

    @BeforeEach
    void setUp() {
        // Sample expense entity
        sampleExpense = new Expense();
        sampleExpense.setId(1L);
        sampleExpense.setTitle("Lunch");
        sampleExpense.setDescription("Team lunch");
        sampleExpense.setAmount(new BigDecimal("150.50"));
        sampleExpense.setType(ExpenseType.EXPENSE);
        sampleExpense.setCategory(ExpenseCategory.FOOD);
        sampleExpense.setTransactionDate(LocalDate.of(2026, 4, 9));
        sampleExpense.setCreatedAt(LocalDateTime.now());

        // Sample income entity
        sampleIncome = new Expense();
        sampleIncome.setId(2L);
        sampleIncome.setTitle("Monthly Salary");
        sampleIncome.setDescription("April salary");
        sampleIncome.setAmount(new BigDecimal("25000"));
        sampleIncome.setType(ExpenseType.INCOME);
        sampleIncome.setCategory(ExpenseCategory.SALARY);
        sampleIncome.setTransactionDate(LocalDate.of(2026, 4, 1));
        sampleIncome.setCreatedAt(LocalDateTime.now());

        // Create request
        createRequest = new CreateExpenseRequest();
        createRequest.setTitle("Lunch");
        createRequest.setDescription("Team lunch");
        createRequest.setAmount(new BigDecimal("150.50"));
        createRequest.setType(ExpenseType.EXPENSE);
        createRequest.setCategory(ExpenseCategory.FOOD);
        createRequest.setTransactionDate(LocalDate.of(2026, 4, 9));

        // Update request
        updateRequest = new UpdateExpenseRequest();
        updateRequest.setTitle("Dinner");
        updateRequest.setDescription("Updated to dinner");
        updateRequest.setAmount(new BigDecimal("200"));
        updateRequest.setType(ExpenseType.EXPENSE);
        updateRequest.setCategory(ExpenseCategory.FOOD);
        updateRequest.setTransactionDate(LocalDate.of(2026, 4, 9));
    }

    @Test
    @DisplayName("Should create expense successfully")
    void createExpense_Success() {
        when(expenseRepository.save(any(Expense.class))).thenReturn(sampleExpense);

        ExpenseResponse result = expenseService.createExpense(createRequest);

        assertNotNull(result);
        assertEquals("Lunch", result.getTitle());
        assertEquals(new BigDecimal("150.50"), result.getAmount());
        assertEquals(ExpenseType.EXPENSE, result.getType());
        assertEquals(ExpenseCategory.FOOD, result.getCategory());
        verify(expenseRepository, times(1)).save(any(Expense.class));
    }

    @Test
    @DisplayName("Should get all expenses")
    void getAllExpenses_Success() {
        List<Expense> expenses = Arrays.asList(sampleExpense, sampleIncome);
        when(expenseRepository.findAll()).thenReturn(expenses);

        List<ExpenseResponse> result = expenseService.getAllExpenses();

        assertEquals(2, result.size());
        assertEquals("Lunch", result.get(0).getTitle());
        assertEquals("Monthly Salary", result.get(1).getTitle());
        verify(expenseRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no expenses exist")
    void getAllExpenses_EmptyList() {
        when(expenseRepository.findAll()).thenReturn(Collections.emptyList());

        List<ExpenseResponse> result = expenseService.getAllExpenses();

        assertTrue(result.isEmpty());
        assertEquals(0, result.size());
        verify(expenseRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should get expense by ID")
    void getExpenseById_Success() {
        when(expenseRepository.findById(1L)).thenReturn(Optional.of(sampleExpense));

        ExpenseResponse result = expenseService.getExpenseById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Lunch", result.getTitle());
        assertEquals(new BigDecimal("150.50"), result.getAmount());
        verify(expenseRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when expense not found by ID")
    void getExpenseById_NotFound() {
        when(expenseRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            expenseService.getExpenseById(999L);
        });
        verify(expenseRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Should update expense successfully")
    void updateExpense_Success() {
        when(expenseRepository.findById(1L)).thenReturn(Optional.of(sampleExpense));
        when(expenseRepository.save(any(Expense.class))).thenReturn(sampleExpense);

        ExpenseResponse result = expenseService.updateExpense(1L, updateRequest);

        assertNotNull(result);
        verify(expenseRepository, times(1)).findById(1L);
        verify(expenseRepository, times(1)).save(any(Expense.class));
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent expense")
    void updateExpense_NotFound() {
        when(expenseRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            expenseService.updateExpense(999L, updateRequest);
        });
        verify(expenseRepository, times(1)).findById(999L);
        verify(expenseRepository, never()).save(any(Expense.class));
    }

    @Test
    @DisplayName("Should delete expense successfully")
    void deleteExpense_Success() {
        when(expenseRepository.findById(1L)).thenReturn(Optional.of(sampleExpense));
        doNothing().when(expenseRepository).delete(any(Expense.class));

        expenseService.deleteExpense(1L);

        verify(expenseRepository, times(1)).findById(1L);
        verify(expenseRepository, times(1)).delete(sampleExpense);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent expense")
    void deleteExpense_NotFound() {
        when(expenseRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            expenseService.deleteExpense(999L);
        });
        verify(expenseRepository, times(1)).findById(999L);
        verify(expenseRepository, never()).delete(any(Expense.class));
    }

    @Test
    @DisplayName("Should filter expenses by type")
    void getExpensesByFilters_ByType() {
        List<Expense> expenses = Arrays.asList(sampleExpense);
        when(expenseRepository.findByFilters(
                eq(ExpenseType.EXPENSE), isNull(), isNull(), isNull()))
                .thenReturn(expenses);

        List<ExpenseResponse> result = expenseService.getExpensesByFilters(
                ExpenseType.EXPENSE, null, null, null);

        assertEquals(1, result.size());
        assertEquals(ExpenseType.EXPENSE, result.get(0).getType());
    }

    @Test
    @DisplayName("Should filter expenses by category")
    void getExpensesByFilters_ByCategory() {
        List<Expense> expenses = Arrays.asList(sampleExpense);
        when(expenseRepository.findByFilters(
                isNull(), eq(ExpenseCategory.FOOD), isNull(), isNull()))
                .thenReturn(expenses);

        List<ExpenseResponse> result = expenseService.getExpensesByFilters(
                null, ExpenseCategory.FOOD, null, null);

        assertEquals(1, result.size());
        assertEquals(ExpenseCategory.FOOD, result.get(0).getCategory());
    }

    @Test
    @DisplayName("Should return empty list when no matching filters")
    void getExpensesByFilters_NoMatch() {
        when(expenseRepository.findByFilters(
                eq(ExpenseType.INCOME), eq(ExpenseCategory.FOOD), isNull(), isNull()))
                .thenReturn(Collections.emptyList());

        List<ExpenseResponse> result = expenseService.getExpensesByFilters(
                ExpenseType.INCOME, ExpenseCategory.FOOD, null, null);

        assertTrue(result.isEmpty());
    }
}