package com.comp4442.expensetracker.dto;

import com.comp4442.expensetracker.entity.ExpenseCategory;
import com.comp4442.expensetracker.entity.ExpenseType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CreateExpenseRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    @NotNull(message = "Type is required")
    private ExpenseType type;

    @NotNull(message = "Category is required")
    private ExpenseCategory category;

    @NotNull(message = "Transaction date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate transactionDate;

    // Creates an empty request object so JSON request fields can be mapped into it.
    public CreateExpenseRequest() {
    }

    // Returns the title supplied for the new transaction.
    public String getTitle() {
        return title;
    }

    // Updates the title supplied for the new transaction.
    public void setTitle(String title) {
        this.title = title;
    }

    // Returns the optional description supplied for the new transaction.
    public String getDescription() {
        return description;
    }

    // Updates the optional description supplied for the new transaction.
    public void setDescription(String description) {
        this.description = description;
    }

    // Returns the money amount supplied for the new transaction.
    public BigDecimal getAmount() {
        return amount;
    }

    // Updates the money amount supplied for the new transaction.
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    // Returns whether the new transaction is income or an expense.
    public ExpenseType getType() {
        return type;
    }

    // Updates whether the new transaction is income or an expense.
    public void setType(ExpenseType type) {
        this.type = type;
    }

    // Returns the category selected for the new transaction.
    public ExpenseCategory getCategory() {
        return category;
    }

    // Updates the category selected for the new transaction.
    public void setCategory(ExpenseCategory category) {
        this.category = category;
    }

    // Returns the date supplied for when the transaction happened.
    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    // Updates the date supplied for when the transaction happened.
    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }
}
