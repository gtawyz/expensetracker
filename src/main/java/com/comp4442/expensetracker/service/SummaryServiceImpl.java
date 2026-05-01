package com.comp4442.expensetracker.service;

import com.comp4442.expensetracker.dto.SummaryResponse;
import com.comp4442.expensetracker.entity.Expense;
import com.comp4442.expensetracker.entity.ExpenseType;
import com.comp4442.expensetracker.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SummaryServiceImpl implements SummaryService {

    private final ExpenseRepository expenseRepository;

    // Injects the repository used to load records for summary calculations.
    public SummaryServiceImpl(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    // Loads records inside one month and calculates totals, net amount, and category breakdowns.
    @Override
    public SummaryResponse getMonthlySummary(int year, int month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        List<Expense> expenses = expenseRepository.findByTransactionDateBetween(startDate, endDate);

        return buildSummary(year, month, expenses);
    }

    // Loads records for the full year and builds a separate summary for each month.
    @Override
    public List<SummaryResponse> getYearlySummary(int year) {
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);

        List<Expense> allExpenses = expenseRepository.findByTransactionDateBetween(startDate, endDate);

        List<SummaryResponse> summaries = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            final int m = month;
            List<Expense> monthlyExpenses = allExpenses.stream()
                    .filter(e -> e.getTransactionDate().getMonthValue() == m)
                    .collect(Collectors.toList());

            summaries.add(buildSummary(year, month, monthlyExpenses));
        }
        return summaries;
    }

    // Calculates income, expenses, net amount, and per-category totals from the provided records.
    private SummaryResponse buildSummary(int year, int month, List<Expense> expenses) {
        BigDecimal totalIncome = expenses.stream()
                .filter(e -> e.getType() == ExpenseType.INCOME)
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpense = expenses.stream()
                .filter(e -> e.getType() == ExpenseType.EXPENSE)
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal netAmount = totalIncome.subtract(totalExpense);

        // Group EXPENSE by category
        Map<String, BigDecimal> expenseByCategory = expenses.stream()
                .filter(e -> e.getType() == ExpenseType.EXPENSE)
                .collect(Collectors.groupingBy(
                        e -> e.getCategory().name(),
                        LinkedHashMap::new,
                        Collectors.reducing(BigDecimal.ZERO, Expense::getAmount, BigDecimal::add)
                ));

        // Group INCOME by category
        Map<String, BigDecimal> incomeByCategory = expenses.stream()
                .filter(e -> e.getType() == ExpenseType.INCOME)
                .collect(Collectors.groupingBy(
                        e -> e.getCategory().name(),
                        LinkedHashMap::new,
                        Collectors.reducing(BigDecimal.ZERO, Expense::getAmount, BigDecimal::add)
                ));

        SummaryResponse response = new SummaryResponse();
        response.setYear(year);
        response.setMonth(month);
        response.setTotalIncome(totalIncome);
        response.setTotalExpense(totalExpense);
        response.setNetAmount(netAmount);
        response.setExpenseByCategory(expenseByCategory);
        response.setIncomeByCategory(incomeByCategory);

        return response;
    }
}
