package com.comp4442.expensetracker.service;

import com.comp4442.expensetracker.entity.Expense;
import com.comp4442.expensetracker.entity.ExpenseCategory;
import com.comp4442.expensetracker.entity.ExpenseType;
import com.comp4442.expensetracker.repository.ExpenseRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ExportService Unit Tests")
class ExportServiceImplTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @InjectMocks
    private ExportServiceImpl exportService;

    // Checks that exporting with records includes the CSV header and expected field values.
    @Test
    @DisplayName("Should export expenses to CSV with correct headers and data")
    void exportCsv_WithData() {
        Expense expense = new Expense();
        expense.setId(1L);
        expense.setTitle("Lunch");
        expense.setDescription("Team lunch");
        expense.setAmount(new BigDecimal("150.50"));
        expense.setType(ExpenseType.EXPENSE);
        expense.setCategory(ExpenseCategory.FOOD);
        expense.setTransactionDate(LocalDate.of(2026, 4, 9));
        expense.setCreatedAt(LocalDateTime.now());

        when(expenseRepository.findAll()).thenReturn(Arrays.asList(expense));

        ByteArrayOutputStream result = exportService.exportExpensesToCsv();
        String csv = result.toString();

        assertTrue(csv.contains("ID,Title,Description,Amount,Type,Category,Transaction Date,Created At"));
        assertTrue(csv.contains("Lunch"));
        assertTrue(csv.contains("150.50"));
        assertTrue(csv.contains("EXPENSE"));
        assertTrue(csv.contains("FOOD"));
    }

    // Checks that exporting with no records still produces a valid CSV header row.
    @Test
    @DisplayName("Should export empty CSV with only headers when no data")
    void exportCsv_Empty() {
        when(expenseRepository.findAll()).thenReturn(Collections.emptyList());

        ByteArrayOutputStream result = exportService.exportExpensesToCsv();
        String csv = result.toString();

        assertTrue(csv.contains("ID,Title,Description,Amount,Type,Category,Transaction Date,Created At"));
        String[] lines = csv.trim().split("\n");
        assertEquals(1, lines.length);
    }

    // Checks that multiple records are written as separate CSV data rows.
    @Test
    @DisplayName("Should handle multiple expenses in CSV")
    void exportCsv_MultipleRecords() {
        Expense expense1 = new Expense();
        expense1.setId(1L);
        expense1.setTitle("Lunch");
        expense1.setDescription("Team lunch");
        expense1.setAmount(new BigDecimal("150.50"));
        expense1.setType(ExpenseType.EXPENSE);
        expense1.setCategory(ExpenseCategory.FOOD);
        expense1.setTransactionDate(LocalDate.of(2026, 4, 9));
        expense1.setCreatedAt(LocalDateTime.now());

        Expense expense2 = new Expense();
        expense2.setId(2L);
        expense2.setTitle("Salary");
        expense2.setDescription("Monthly salary");
        expense2.setAmount(new BigDecimal("25000"));
        expense2.setType(ExpenseType.INCOME);
        expense2.setCategory(ExpenseCategory.SALARY);
        expense2.setTransactionDate(LocalDate.of(2026, 4, 1));
        expense2.setCreatedAt(LocalDateTime.now());

        when(expenseRepository.findAll()).thenReturn(Arrays.asList(expense1, expense2));

        ByteArrayOutputStream result = exportService.exportExpensesToCsv();
        String csv = result.toString();
        String[] lines = csv.trim().split("\n");

        assertEquals(3, lines.length);
        assertTrue(csv.contains("Lunch"));
        assertTrue(csv.contains("Salary"));
    }
}
