package com.comp4442.expensetracker.repository;

import com.comp4442.expensetracker.entity.Expense;
import com.comp4442.expensetracker.entity.ExpenseCategory;
import com.comp4442.expensetracker.entity.ExpenseType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    // Lets Spring Data generate a query that returns records with the selected income/expense type.
    List<Expense> findByType(ExpenseType type);

    // Lets Spring Data generate a query that returns records in the selected category.
    List<Expense> findByCategory(ExpenseCategory category);

    // Lets Spring Data generate a query that returns records whose transaction date is inside a range.
    List<Expense> findByTransactionDateBetween(LocalDate startDate, LocalDate endDate);

    // Lets Spring Data generate a query that returns records matching both type and category.
    List<Expense> findByTypeAndCategory(ExpenseType type, ExpenseCategory category);

    // Lets Spring Data generate a query that returns records matching type and transaction date range.
    List<Expense> findByTypeAndTransactionDateBetween(
            ExpenseType type, LocalDate startDate, LocalDate endDate);

    // Lets Spring Data generate a query that returns records matching category and date range.
    List<Expense> findByCategoryAndTransactionDateBetween(
            ExpenseCategory category, LocalDate startDate, LocalDate endDate);

    // Lets Spring Data generate a query that returns records matching type, category, and date range.
    List<Expense> findByTypeAndCategoryAndTransactionDateBetween(
            ExpenseType type, ExpenseCategory category,
            LocalDate startDate, LocalDate endDate);

    // Runs one JPQL query where null parameters mean "do not filter by this field".
    @Query("SELECT e FROM Expense e WHERE " +
            "(:type IS NULL OR e.type = :type) AND " +
            "(:category IS NULL OR e.category = :category) AND " +
            "(:startDate IS NULL OR e.transactionDate >= :startDate) AND " +
            "(:endDate IS NULL OR e.transactionDate <= :endDate) " +
            "ORDER BY e.transactionDate DESC")
    List<Expense> findByFilters(
            @Param("type") ExpenseType type,
            @Param("category") ExpenseCategory category,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // Runs the same optional filters as findByFilters, but returns one page using Pageable sorting.
    @Query("SELECT e FROM Expense e WHERE " +
            "(:type IS NULL OR e.type = :type) AND " +
            "(:category IS NULL OR e.category = :category) AND " +
            "(:startDate IS NULL OR e.transactionDate >= :startDate) AND " +
            "(:endDate IS NULL OR e.transactionDate <= :endDate)")
    Page<Expense> findByFiltersPaged(
            @Param("type") ExpenseType type,
            @Param("category") ExpenseCategory category,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );
}
