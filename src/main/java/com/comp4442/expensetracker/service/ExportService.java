package com.comp4442.expensetracker.service;

import java.io.ByteArrayOutputStream;

public interface ExportService {
    // Defines the service operation that exports all stored records as CSV bytes.
    ByteArrayOutputStream exportExpensesToCsv();
}
