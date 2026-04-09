package com.comp4442.expensetracker.dto;

import java.math.BigDecimal;
import java.util.Map;

public class SummaryResponse {

    private int year;
    private int month;
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal netAmount;
    private Map<String, BigDecimal> expenseByCategory;
    private Map<String, BigDecimal> incomeByCategory;

    public SummaryResponse() {}

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public int getMonth() { return month; }
    public void setMonth(int month) { this.month = month; }

    public BigDecimal getTotalIncome() { return totalIncome; }
    public void setTotalIncome(BigDecimal totalIncome) { this.totalIncome = totalIncome; }

    public BigDecimal getTotalExpense() { return totalExpense; }
    public void setTotalExpense(BigDecimal totalExpense) { this.totalExpense = totalExpense; }

    public BigDecimal getNetAmount() { return netAmount; }
    public void setNetAmount(BigDecimal netAmount) { this.netAmount = netAmount; }

    public Map<String, BigDecimal> getExpenseByCategory() { return expenseByCategory; }
    public void setExpenseByCategory(Map<String, BigDecimal> expenseByCategory) {
        this.expenseByCategory = expenseByCategory;
    }

    public Map<String, BigDecimal> getIncomeByCategory() { return incomeByCategory; }
    public void setIncomeByCategory(Map<String, BigDecimal> incomeByCategory) {
        this.incomeByCategory = incomeByCategory;
    }
}