package com.comp4442.expensetracker.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        Server localServer = new Server();
        localServer.setUrl("http://localhost:" + serverPort);
        localServer.setDescription("Local Development");

        Server prodServer = new Server();
        prodServer.setUrl("http://your-ec2-public-ip:" + serverPort);
        prodServer.setDescription("AWS EC2 Production");

        Contact contact = new Contact();
        contact.setName("COMP4442 Group");
        contact.setEmail("your-email@connect.polyu.edu.hk");

        Info info = new Info()
                .title("Expense Tracker API")
                .version("1.0.0")
                .description("""
                        A RESTful API for tracking personal income and expenses.
                        
                        ## Features
                        - Create, read, update, delete expenses
                        - Filter by type, category, and date range
                        - Pagination and sorting
                        - Monthly and yearly summary statistics
                        
                        ## Expense Types
                        - `INCOME` - Money received
                        - `EXPENSE` - Money spent
                        
                        ## Categories
                        FOOD, TRANSPORT, ENTERTAINMENT, SHOPPING, BILLS, HEALTH, EDUCATION, SALARY, INVESTMENT, OTHER
                        """)
                .contact(contact);

        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer, prodServer));
    }
}