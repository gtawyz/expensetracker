package com.comp4442.expensetracker.service;

import java.io.ByteArrayOutputStream;

public interface ExportService {
    ByteArrayOutputStream exportExpensesToCsv();
}