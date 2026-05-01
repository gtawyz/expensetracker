package com.comp4442.expensetracker.dto;

import com.comp4442.expensetracker.entity.ExpenseCategory;
import com.comp4442.expensetracker.entity.ExpenseType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public class UpdateExpenseRequest {

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

    // Creates an empty request object so JSON update fields can be mapped into it.
    public UpdateExpenseRequest() {
    }

    // Returns the replacement title for the existing transaction.
    public String getTitle() {
        return title;
    }

    // Updates the replacement title for the existing transaction.
    public void setTitle(String title) {
        this.title = title;
    }

    // Returns the replacement description for the existing transaction.
    public String getDescription() {
        return description;
    }

    // Updates the replacement description for the existing transaction.
    public void setDescription(String description) {
        this.description = description;
    }

    // Returns the replacement money amount for the existing transaction.
    public BigDecimal getAmount() {
        return amount;
    }

    // Updates the replacement money amount for the existing transaction.
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    // Returns the replacement income/expense type for the existing transaction.
    public ExpenseType getType() {
        return type;
    }

    // Updates the replacement income/expense type for the existing transaction.
    public void setType(ExpenseType type) {
        this.type = type;
    }

    // Returns the replacement category for the existing transaction.
    public ExpenseCategory getCategory() {
        return category;
    }

    // Updates the replacement category for the existing transaction.
    public void setCategory(ExpenseCategory category) {
        this.category = category;
    }

    // Returns the replacement date for when the transaction happened.
    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    // Updates the replacement date for when the transaction happened.
    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }
}
