# Expense Tracker API

## Project Overview

This repository contains a COMP4442 semester project implemented as a Spring Boot REST API for managing personal income and expense records. The project focuses on clean CRUD operations, filtering, pagination, summary reporting, health monitoring, containerized execution, and a straightforward AWS deployment path.

## System Architecture

```text
Client -> EC2/Docker -> Spring Boot -> MySQL/RDS
```

- Clients access the API over HTTP.
- The application can run locally, inside Docker, or on AWS EC2.
- Spring Boot handles business logic, validation, persistence, and monitoring endpoints.
- Data is stored in MySQL locally or in Amazon RDS for deployment.

## Tech Stack

| Technology | Version | Purpose |
|---|---|---|
| Java | 17 | Programming language |
| Spring Boot | 3.5.13 | REST API framework |
| Spring Data JPA | - | Data access layer |
| MySQL | 8.4 | Primary database |
| H2 | - | In-memory test database |
| Maven | - | Build and dependency management |
| Docker Compose | v2 | Local container orchestration |
| AWS EC2 | - | Application hosting |
| AWS RDS | MySQL 8.4 | Managed production database |
| Spring Boot Actuator | - | Operational monitoring |
| springdoc OpenAPI | 2.8.6 | Swagger UI documentation |
| GitHub Actions | - | Continuous integration |

## Key Features

- CRUD operations for expense and income records
- Filtering by type, category, and date range
- Pagination and sorting for expense queries
- Monthly and yearly summary statistics
- CSV export for stored records
- Custom health endpoints for quick service checks
- Spring Boot Actuator endpoints for operational monitoring
- Multiple runtime profiles for local, Docker, and production environments

## API Overview

Main endpoint groups:

- `POST /api/expenses`, `GET /api/expenses`, `GET /api/expenses/{id}`, `PUT /api/expenses/{id}`, `DELETE /api/expenses/{id}`
- `GET /api/expenses/filter`
- `GET /api/expenses/paged`
- `GET /api/summary/monthly`, `GET /api/summary/monthly/current`, `GET /api/summary/yearly`
- `GET /api/export/csv`
- `GET /api/health`, `GET /api/health/detail`
- `GET /actuator/health`, `GET /actuator/info`, `GET /actuator/metrics`

Sample `curl` commands:

```bash
curl -X POST http://localhost:8080/api/expenses \
  -H "Content-Type: application/json" \
  -d '{"title":"Lunch","description":"Team lunch","amount":50.00,"type":"EXPENSE","category":"FOOD","transactionDate":"2026-04-09"}'

curl http://localhost:8080/api/expenses

curl http://localhost:8080/api/expenses/1

curl "http://localhost:8080/api/expenses/paged?page=0&size=5&sortBy=amount&sortDir=desc"

curl http://localhost:8080/api/summary/monthly/current

curl http://localhost:8080/api/health

curl http://localhost:8080/actuator/health

curl -OJ http://localhost:8080/api/export/csv
```

Swagger UI is available at `http://localhost:8080/swagger-ui/index.html` when the application is running.

## Profiles and Configuration

The project uses three Spring profiles:

- `dev`: default local profile, typically using MySQL on `localhost`
- `docker`: container profile, using the Docker service name `mysql`
- `prod`: deployment profile for environments such as AWS EC2 with RDS

Common environment variables:

```env
DB_HOST=localhost
DB_PORT=3306
DB_NAME=expense_tracker
DB_USERNAME=root
DB_PASSWORD=
SERVER_PORT=8080
```

## Local Run

Prerequisites:

- Java 17
- MySQL 8.x

Steps:

```bash
cd expensetracker
```

If you are starting from a fresh checkout, clone the repository first and then enter the project directory.

Create the database:

```sql
CREATE DATABASE expense_tracker;
```

Run the application:

```bash
./mvnw spring-boot:run
```

The default `dev` profile starts the API on `http://localhost:8080`.

## Docker Run

The Docker setup runs the Spring Boot app and MySQL together. The app is published on `8080`, while MySQL stays available to the app through the internal Docker network using the hostname `mysql`.

Start the stack:

```bash
docker compose up --build
```

Stop the stack:

```bash
docker compose down
```

Remove containers and the MySQL volume:

```bash
docker compose down -v
```

## AWS Deployment Summary

For deployment, the application is packaged as a Spring Boot JAR and runs on AWS EC2, while MySQL is hosted on Amazon RDS.

Typical deployment flow:

1. Provision an EC2 instance with Java 17 installed.
2. Provision an RDS MySQL database and create the `expense_tracker` schema.
3. Set `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USERNAME`, `DB_PASSWORD`, and `SERVER_PORT`.
4. Build the project with Maven and start the JAR using the `prod` profile.

Example commands:

```bash
chmod +x mvnw
./mvnw clean package -DskipTests
java -jar -Dspring.profiles.active=prod target/expensetracker-0.0.1-SNAPSHOT.jar
```

## Testing

The project includes both unit tests and Spring Boot integration tests.

- Unit tests cover service-layer behavior.
- Integration tests use `MockMvc` with an isolated H2 in-memory database.
- Covered integration endpoints include `POST /api/expenses`, `GET /api/expenses`, `GET /api/expenses/{id}`, `GET /api/expenses/paged`, `GET /api/summary/monthly/current`, `GET /api/health`, and `GET /actuator/health`.

Run all tests with:

```bash
./mvnw test
```

## CI

GitHub Actions runs a simple CI workflow for:

- pushes to `main`
- pull requests targeting `main`

The workflow uses Java 17, enables Maven dependency caching, and runs:

```bash
chmod +x mvnw
./mvnw clean test
```

If any test fails, the CI workflow fails.

## Demo Checklist

- Start the application locally or with Docker
- Verify `GET /api/health`
- Verify `GET /actuator/health`
- Create a record with `POST /api/expenses`
- List records with `GET /api/expenses`
- Show pagination with `GET /api/expenses/paged`
- Show current-month summary with `GET /api/summary/monthly/current`
- Export records with `GET /api/export/csv`
- Open Swagger UI at `http://localhost:8080/swagger-ui/index.html`
