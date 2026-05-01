package com.comp4442.expensetracker.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "expenses")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 500)
    private String description;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExpenseType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExpenseCategory category;

    @Column(nullable = false)
    private LocalDate transactionDate;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    // Creates an empty Expense object so JPA and Jackson can instantiate it before fields are filled.
    public Expense() {
    }

    // Creates an Expense object with all database fields already provided.
    public Expense(Long id, String title, String description, BigDecimal amount,
                   ExpenseType type, ExpenseCategory category,
                   LocalDate transactionDate, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.transactionDate = transactionDate;
        this.createdAt = createdAt;
    }

    // Automatically sets the creation timestamp right before the entity is first saved.
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // Returns the database ID of this record.
    public Long getId() {
        return id;
    }

    // Updates the database ID of this record.
    public void setId(Long id) {
        this.id = id;
    }

    // Returns the short name or title of the transaction.
    public String getTitle() {
        return title;
    }

    // Updates the short name or title of the transaction.
    public void setTitle(String title) {
        this.title = title;
    }

    // Returns the optional longer description of the transaction.
    public String getDescription() {
        return description;
    }

    // Updates the optional longer description of the transaction.
    public void setDescription(String description) {
        this.description = description;
    }

    // Returns the money amount for this income or expense.
    public BigDecimal getAmount() {
        return amount;
    }

    // Updates the money amount for this income or expense.
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    // Returns whether this record is income or an expense.
    public ExpenseType getType() {
        return type;
    }

    // Updates whether this record is income or an expense.
    public void setType(ExpenseType type) {
        this.type = type;
    }

    // Returns the category used to group this transaction.
    public ExpenseCategory getCategory() {
        return category;
    }

    // Updates the category used to group this transaction.
    public void setCategory(ExpenseCategory category) {
        this.category = category;
    }

    // Returns the date when the transaction happened.
    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    // Updates the date when the transaction happened.
    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    // Returns the timestamp when this record was created in the system.
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Updates the timestamp when this record was created in the system.
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
