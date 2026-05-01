package com.comp4442.expensetracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "Health Check", description = "System health and status monitoring")
public class HealthController {

    @Autowired
    private DataSource dataSource;

    // Returns a small health payload showing that the API process is running.
    @GetMapping("/health")
    @Operation(summary = "Basic health check", description = "Returns basic service status")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> health = new LinkedHashMap<>();
        health.put("service", "expense-tracker");
        health.put("status", "UP");
        health.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.ok(health);
    }

    // Checks database connectivity and JVM/system details to describe the service health more fully.
    @GetMapping("/health/detail")
    @Operation(summary = "Detailed health check", description = "Returns detailed system status including database connection and system info")
    public ResponseEntity<Map<String, Object>> detailedHealthCheck() {
        Map<String, Object> health = new LinkedHashMap<>();
        health.put("service", "expense-tracker");
        health.put("timestamp", LocalDateTime.now().toString());

        // Database status
        Map<String, Object> database = new LinkedHashMap<>();
        try (Connection connection = dataSource.getConnection()) {
            database.put("status", "UP");
            database.put("database", connection.getMetaData().getDatabaseProductName());
            database.put("version", connection.getMetaData().getDatabaseProductVersion());
            database.put("url", connection.getMetaData().getURL());
        } catch (Exception e) {
            database.put("status", "DOWN");
            database.put("error", e.getMessage());
        }
        health.put("database", database);

        // System info
        Map<String, Object> system = new LinkedHashMap<>();
        Runtime runtime = Runtime.getRuntime();
        system.put("javaVersion", System.getProperty("java.version"));
        system.put("javaVendor", System.getProperty("java.vendor"));
        system.put("osName", System.getProperty("os.name"));
        system.put("osVersion", System.getProperty("os.version"));
        system.put("osArch", System.getProperty("os.arch"));
        system.put("availableProcessors", runtime.availableProcessors());
        system.put("totalMemoryMB", runtime.totalMemory() / (1024 * 1024));
        system.put("freeMemoryMB", runtime.freeMemory() / (1024 * 1024));
        system.put("usedMemoryMB", (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024));
        system.put("maxMemoryMB", runtime.maxMemory() / (1024 * 1024));
        health.put("system", system);

        // Overall status
        String dbStatus = (String) database.get("status");
        health.put("status", "UP".equals(dbStatus) ? "UP" : "DEGRADED");

        return ResponseEntity.ok(health);
    }
}
