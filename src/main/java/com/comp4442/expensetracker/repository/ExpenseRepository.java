package com.comp4442.expensetracker.repository;

import com.comp4442.expensetracker.entity.Expense;
import com.comp4442.expensetracker.entity.ExpenseCategory;
import com.comp4442.expensetracker.entity.ExpenseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    // Filter by type
    List<Expense> findByType(ExpenseType type);

    // Filter by category
    List<Expense> findByCategory(ExpenseCategory category);

    // Filter by date range
    List<Expense> findByTransactionDateBetween(LocalDate startDate, LocalDate endDate);

    // Filter by type and category
    List<Expense> findByTypeAndCategory(ExpenseType type, ExpenseCategory category);

    // Filter by type and date range
    List<Expense> findByTypeAndTransactionDateBetween(
            ExpenseType type, LocalDate startDate, LocalDate endDate);

    // Filter by category and date range
    List<Expense> findByCategoryAndTransactionDateBetween(
            ExpenseCategory category, LocalDate startDate, LocalDate endDate);

    // Filter by all three: type, category, date range
    List<Expense> findByTypeAndCategoryAndTransactionDateBetween(
            ExpenseType type, ExpenseCategory category,
            LocalDate startDate, LocalDate endDate);

    // Combined dynamic filter using JPQL
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
}