package com.comp4442.expensetracker.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        Server localServer = new Server();
        localServer.setUrl("http://localhost:8080");
        localServer.setDescription("Local Development Server");

        Server prodServer = new Server();
        prodServer.setUrl("http://3.107.0.116:8080");
        prodServer.setDescription("AWS EC2 Production Server");

        return new OpenAPI()
                .info(new Info()
                        .title("Expense Tracker API")
                        .description("COMP4442 Expense Tracker - Spring Boot REST API for managing personal income and expenses.\n\n"
                                + "## Features\n"
                                + "- Create, read, update, delete expenses\n"
                                + "- Filter by type, category, and date range\n"
                                + "- Pagination and sorting\n"
                                + "- Monthly and yearly summary statistics\n\n"
                                + "## Expense Types\n"
                                + "- **INCOME** - Money received\n"
                                + "- **EXPENSE** - Money spent\n\n"
                                + "## Categories\n"
                                + "FOOD, TRANSPORT, ENTERTAINMENT, SHOPPING, BILLS, HEALTH, EDUCATION, SALARY, INVESTMENT, OTHER")
                        .version("1.0.0")
                        .contact(new io.swagger.v3.oas.models.info.Contact()
                                .name("COMP4442 Group")
                                .email("your-email@connect.polyu.hk")))
                .servers(List.of(localServer, prodServer));
    }
}