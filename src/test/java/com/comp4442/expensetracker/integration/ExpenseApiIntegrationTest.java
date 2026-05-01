package com.comp4442.expensetracker.integration;

import com.comp4442.expensetracker.entity.Expense;
import com.comp4442.expensetracker.entity.ExpenseCategory;
import com.comp4442.expensetracker.entity.ExpenseType;
import com.comp4442.expensetracker.repository.ExpenseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Expense API Integration Tests")
class ExpenseApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ExpenseRepository expenseRepository;

    // Clears database records before each integration test so tests do not affect each other.
    @BeforeEach
    void clearDatabase() {
        expenseRepository.deleteAll();
    }

    // Verifies that the create-expense endpoint stores data and returns the created record.
    @Test
    @DisplayName("POST /api/expenses creates a new expense")
    void createExpense_ReturnsCreatedExpense() throws Exception {
        Map<String, Object> request = Map.of(
                "title", "Lunch",
                "description", "Team lunch",
                "amount", new BigDecimal("120.50"),
                "type", "EXPENSE",
                "category", "FOOD",
                "transactionDate", LocalDate.now().toString()
        );

        mockMvc.perform(post("/api/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Expense created successfully"))
                .andExpect(jsonPath("$.data.id").isNumber())
                .andExpect(jsonPath("$.data.title").value("Lunch"))
                .andExpect(jsonPath("$.data.amount").value(120.50))
                .andExpect(jsonPath("$.data.type").value("EXPENSE"))
                .andExpect(jsonPath("$.data.category").value("FOOD"));
    }

    // Verifies that the list endpoint returns all records saved in the test database.
    @Test
    @DisplayName("GET /api/expenses returns all saved expenses")
    void getAllExpenses_ReturnsSavedRecords() throws Exception {
        saveExpense("Lunch", "25.00", ExpenseType.EXPENSE, ExpenseCategory.FOOD, LocalDate.now());
        saveExpense("Salary", "3000.00", ExpenseType.INCOME, ExpenseCategory.SALARY, LocalDate.now());

        mockMvc.perform(get("/api/expenses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[*].title", hasItem("Lunch")))
                .andExpect(jsonPath("$.data[*].title", hasItem("Salary")));
    }

    // Verifies that the get-by-ID endpoint returns the exact saved record.
    @Test
    @DisplayName("GET /api/expenses/{id} returns one expense")
    void getExpenseById_ReturnsMatchingRecord() throws Exception {
        Expense savedExpense = saveExpense(
                "Transport",
                "15.00",
                ExpenseType.EXPENSE,
                ExpenseCategory.TRANSPORT,
                LocalDate.now()
        );

        mockMvc.perform(get("/api/expenses/{id}", savedExpense.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(savedExpense.getId()))
                .andExpect(jsonPath("$.data.title").value("Transport"))
                .andExpect(jsonPath("$.data.category").value("TRANSPORT"));
    }

    // Verifies that the paged endpoint returns sorted content plus page metadata.
    @Test
    @DisplayName("GET /api/expenses/paged returns paginated data")
    void getExpensesPaged_ReturnsPageMetadataAndContent() throws Exception {
        LocalDate today = LocalDate.now();
        saveExpense("Coffee", "8.00", ExpenseType.EXPENSE, ExpenseCategory.FOOD, today.minusDays(1));
        saveExpense("Book", "40.00", ExpenseType.EXPENSE, ExpenseCategory.EDUCATION, today);

        mockMvc.perform(get("/api/expenses/paged")
                        .param("page", "0")
                        .param("size", "1")
                        .param("sortBy", "transactionDate")
                        .param("sortDir", "desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.page").value(0))
                .andExpect(jsonPath("$.data.size").value(1))
                .andExpect(jsonPath("$.data.totalElements").value(2))
                .andExpect(jsonPath("$.data.totalPages").value(2))
                .andExpect(jsonPath("$.data.content", hasSize(1)))
                .andExpect(jsonPath("$.data.content[0].title").value("Book"));
    }

    // Verifies that the current-month summary endpoint calculates income, expenses, net amount, and category totals.
    @Test
    @DisplayName("GET /api/summary/monthly/current returns current-month totals")
    void getCurrentMonthSummary_ReturnsCalculatedTotals() throws Exception {
        LocalDate today = LocalDate.now();
        saveExpense("Salary", "5000.00", ExpenseType.INCOME, ExpenseCategory.SALARY, today.withDayOfMonth(1));
        saveExpense("Groceries", "120.00", ExpenseType.EXPENSE, ExpenseCategory.FOOD, today.withDayOfMonth(2));

        mockMvc.perform(get("/api/summary/monthly/current"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.year").value(today.getYear()))
                .andExpect(jsonPath("$.data.month").value(today.getMonthValue()))
                .andExpect(jsonPath("$.data.totalIncome").value(5000.00))
                .andExpect(jsonPath("$.data.totalExpense").value(120.00))
                .andExpect(jsonPath("$.data.netAmount").value(4880.00))
                .andExpect(jsonPath("$.data.expenseByCategory.FOOD").value(120.00))
                .andExpect(jsonPath("$.data.incomeByCategory.SALARY").value(5000.00));
    }

    // Verifies that the custom API health endpoint reports the service as running.
    @Test
    @DisplayName("GET /api/health returns application health details")
    void getApiHealth_ReturnsUpStatus() throws Exception {
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.service").value("expense-tracker"))
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());
    }

    // Verifies that Spring Boot Actuator reports the application health as UP.
    @Test
    @DisplayName("GET /actuator/health returns actuator health status")
    void getActuatorHealth_ReturnsUpStatus() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }

    // Creates and saves a test expense record with the supplied values.
    private Expense saveExpense(
            String title,
            String amount,
            ExpenseType type,
            ExpenseCategory category,
            LocalDate transactionDate) {

        Expense expense = new Expense();
        expense.setTitle(title);
        expense.setDescription(title + " description");
        expense.setAmount(new BigDecimal(amount));
        expense.setType(type);
        expense.setCategory(category);
        expense.setTransactionDate(transactionDate);
        return expenseRepository.save(expense);
    }
}
