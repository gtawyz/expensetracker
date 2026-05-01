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

    // Creates an empty summary object so service code can fill in calculated totals.
    public SummaryResponse() {}

    // Returns the year this summary describes.
    public int getYear() { return year; }

    // Updates the year this summary describes.
    public void setYear(int year) { this.year = year; }

    // Returns the month number this summary describes.
    public int getMonth() { return month; }

    // Updates the month number this summary describes.
    public void setMonth(int month) { this.month = month; }

    // Returns the total income for the summary period.
    public BigDecimal getTotalIncome() { return totalIncome; }

    // Updates the total income for the summary period.
    public void setTotalIncome(BigDecimal totalIncome) { this.totalIncome = totalIncome; }

    // Returns the total expenses for the summary period.
    public BigDecimal getTotalExpense() { return totalExpense; }

    // Updates the total expenses for the summary period.
    public void setTotalExpense(BigDecimal totalExpense) { this.totalExpense = totalExpense; }

    // Returns income minus expenses for the summary period.
    public BigDecimal getNetAmount() { return netAmount; }

    // Updates income minus expenses for the summary period.
    public void setNetAmount(BigDecimal netAmount) { this.netAmount = netAmount; }

    // Returns expense totals grouped by category name.
    public Map<String, BigDecimal> getExpenseByCategory() { return expenseByCategory; }

    // Updates expense totals grouped by category name.
    public void setExpenseByCategory(Map<String, BigDecimal> expenseByCategory) {
        this.expenseByCategory = expenseByCategory;
    }

    // Returns income totals grouped by category name.
    public Map<String, BigDecimal> getIncomeByCategory() { return incomeByCategory; }

    // Updates income totals grouped by category name.
    public void setIncomeByCategory(Map<String, BigDecimal> incomeByCategory) {
        this.incomeByCategory = incomeByCategory;
    }
}
