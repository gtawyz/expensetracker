package com.comp4442.expensetracker.service;

import com.comp4442.expensetracker.entity.Expense;
import com.comp4442.expensetracker.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.List;

@Service
public class ExportServiceImpl implements ExportService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Override
    public ByteArrayOutputStream exportExpensesToCsv() {
        List<Expense> expenses = expenseRepository.findAll();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(out);

        // CSV Header
        writer.println("ID,Title,Description,Amount,Type,Category,Transaction Date,Created At");

        // CSV Data
        for (Expense expense : expenses) {
            writer.println(String.format("%d,\"%s\",\"%s\",%.2f,%s,%s,%s,%s",
                    expense.getId(),
                    escapeCsv(expense.getTitle()),
                    escapeCsv(expense.getDescription()),
                    expense.getAmount(),
                    expense.getType(),
                    expense.getCategory(),
                    expense.getTransactionDate(),
                    expense.getCreatedAt()));
        }

        writer.flush();
        return out;
    }

    private String escapeCsv(String value) {
        if (value == null) return "";
        return value.replace("\"", "\"\"");
    }
}