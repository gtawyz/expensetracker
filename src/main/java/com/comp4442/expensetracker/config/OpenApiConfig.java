package com.comp4442.expensetracker.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    // Creates the Swagger/OpenAPI metadata shown in the generated API documentation.
    @Bean
    public OpenAPI customOpenAPI() {
        Contact contact = new Contact();
        contact.setName("COMP4442 Group Project Team");

        Info info = new Info()
                .title("Expense Tracker API")
                .description("COMP4442 Expense Tracker - Spring Boot REST API for managing personal income and expenses.")
                .version("1.0.0")
                .contact(contact);

        return new OpenAPI().info(info);
    }
}
