package com.comp4442.expensetracker.dto;

import com.comp4442.expensetracker.entity.ExpenseCategory;
import com.comp4442.expensetracker.entity.ExpenseType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ExpenseResponse {

    private Long id;
    private String title;
    private String description;
    private BigDecimal amount;
    private ExpenseType type;
    private ExpenseCategory category;
    private LocalDate transactionDate;
    private LocalDateTime createdAt;

    // Creates an empty response object so service code can fill in each returned field.
    public ExpenseResponse() {
    }

    // Returns the database ID of the transaction.
    public Long getId() {
        return id;
    }

    // Updates the database ID included in the response.
    public void setId(Long id) {
        this.id = id;
    }

    // Returns the transaction title included in the response.
    public String getTitle() {
        return title;
    }

    // Updates the transaction title included in the response.
    public void setTitle(String title) {
        this.title = title;
    }

    // Returns the transaction description included in the response.
    public String getDescription() {
        return description;
    }

    // Updates the transaction description included in the response.
    public void setDescription(String description) {
        this.description = description;
    }

    // Returns the money amount included in the response.
    public BigDecimal getAmount() {
        return amount;
    }

    // Updates the money amount included in the response.
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    // Returns whether the response record is income or an expense.
    public ExpenseType getType() {
        return type;
    }

    // Updates whether the response record is income or an expense.
    public void setType(ExpenseType type) {
        this.type = type;
    }

    // Returns the category included in the response.
    public ExpenseCategory getCategory() {
        return category;
    }

    // Updates the category included in the response.
    public void setCategory(ExpenseCategory category) {
        this.category = category;
    }

    // Returns the transaction date included in the response.
    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    // Updates the transaction date included in the response.
    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    // Returns the creation timestamp included in the response.
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Updates the creation timestamp included in the response.
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
