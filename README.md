# Expense Tracker API

COMP4442 Semester Project - A Spring Boot REST API for managing personal income and expenses, deployed on AWS EC2 with RDS MySQL.

## Live Demo

- **API Base URL:** http://3.107.0.116:8080/api
- **Swagger UI:** http://3.107.0.116:8080/swagger-ui/index.html
- **Health Check:** http://3.107.0.116:8080/api/health
- **Detailed Health Check:** http://3.107.0.116:8080/api/health/detail
- **Actuator Health:** http://3.107.0.116:8080/actuator/health
- **Actuator Info:** http://3.107.0.116:8080/actuator/info
- **Actuator Metrics:** http://3.107.0.116:8080/actuator/metrics

## Tech Stack

| Technology | Version | Purpose |
|---|---|---|
| Java | 17 | Programming Language |
| Spring Boot | 3.5.13 | Backend Framework |
| Spring Data JPA | - | Database ORM |
| MySQL | 8.4 | Relational Database |
| Swagger/OpenAPI | 2.8.6 | API Documentation |
| Maven | - | Build Tool |
| AWS EC2 | t3.micro | Application Server |
| AWS RDS | MySQL 8.4 | Cloud Database |

## Features

- CRUD Operations - Create, read, update, delete expense and income records
- Filtering - Filter records by type, category, and date range
- Pagination and Sorting - Paginated results with customizable sorting
- Monthly and Yearly Summary - Statistics with category breakdown
- Health Check - Basic and detailed system health monitoring
- Spring Boot Actuator - Safe operational endpoints for health, info, and metrics
- Swagger UI - Interactive API documentation for easy testing
- CORS Support - Cross-origin requests enabled for frontend integration
- Global Exception Handling - Standardized error responses
- Input Validation - Request body validation with meaningful error messages
- Environment-based Profiles - Separate `dev`, `docker`, and `prod` configuration without hard-coded secrets

## API Endpoints

### Expense Management

| Method | Endpoint | Description |
|---|---|---|
| POST | /api/expenses | Create a new record |
| GET | /api/expenses | Get all records |
| GET | /api/expenses/{id} | Get a single record by ID |
| PUT | /api/expenses/{id} | Update an existing record |
| DELETE | /api/expenses/{id} | Delete a record |
| GET | /api/expenses/filter | Filter by type, category, date range |
| GET | /api/expenses/paged | Paginated sorted filtered results |

### Summary Statistics

| Method | Endpoint | Description |
|---|---|---|
| GET | /api/summary/monthly?year=2026&month=4 | Monthly summary |
| GET | /api/summary/yearly?year=2026 | Yearly summary |
| GET | /api/summary/monthly/current | Current month summary |

### Health Check

| Method | Endpoint | Description |
|---|---|---|
| GET | /api/health | Basic health check |
| GET | /api/health/detail | Detailed health check with DB and system info |

### Actuator

| Method | Endpoint | Description |
|---|---|---|
| GET | /actuator/health | Spring Boot actuator health status |
| GET | /actuator/info | Project metadata such as name, version, and active environment |
| GET | /actuator/metrics | Available application metrics |

## Data Model

### Expense Entity

| Field | Type | Constraints | Description |
|---|---|---|---|
| id | Long | Auto-generated | Primary key |
| title | String | Required | Title of the record |
| description | String | Optional, max 500 chars | Additional details |
| amount | BigDecimal | Required, greater than 0 | Transaction amount |
| type | Enum | Required | INCOME or EXPENSE |
| category | Enum | Required | FOOD, TRANSPORT, etc. |
| transactionDate | LocalDate | Required | Date of transaction |
| createdAt | LocalDateTime | Auto-generated | Creation timestamp |

### Expense Types

INCOME - Money received (salary, investment returns)  
EXPENSE - Money spent (food, transport, bills)

### Expense Categories

FOOD, TRANSPORT, ENTERTAINMENT, SHOPPING, BILLS, HEALTH, EDUCATION, SALARY, INVESTMENT, OTHER

## Project Structure

```text
expensetracker/
|-- pom.xml
|-- README.md
|-- .env.example
`-- src/main/
    |-- java/com/comp4442/expensetracker/
    |   |-- ExpensetrackerApplication.java
    |   |-- config/
    |   |   |-- OpenApiConfig.java
    |   |   `-- CorsConfig.java
    |   |-- controller/
    |   |   |-- ExpenseController.java
    |   |   |-- SummaryController.java
    |   |   `-- HealthController.java
    |   |-- dto/
    |   |   |-- ApiResponse.java
    |   |   |-- ErrorResponse.java
    |   |   |-- PagedResponse.java
    |   |   `-- SummaryResponse.java
    |   |-- entity/
    |   |   |-- Expense.java
    |   |   |-- ExpenseCategory.java
    |   |   `-- ExpenseType.java
    |   |-- exception/
    |   |   `-- GlobalExceptionHandler.java
    |   |-- repository/
    |   |   `-- ExpenseRepository.java
    |   `-- service/
    |       |-- ExpenseService.java
    |       |-- ExpenseServiceImpl.java
    |       |-- SummaryService.java
    |       `-- SummaryServiceImpl.java
    `-- resources/
        |-- application.properties
        |-- application-dev.properties
        |-- application-docker.properties
        `-- application-prod.properties
```

## Configuration Profiles

- `dev` is the default profile and is intended for local development with MySQL on `localhost`.
- `docker` is intended for containerized environments and uses the MySQL service name `mysql`.
- `prod` is intended for production deployments such as AWS EC2 with RDS.

All profiles read database settings from environment variables. No secrets need to be committed to the repository.

## Environment Variables

Copy `.env.example` for reference and set values in your shell, IDE, or deployment platform:

```env
DB_HOST=localhost
DB_PORT=3306
DB_NAME=expense_tracker
DB_USERNAME=root
DB_PASSWORD=
SERVER_PORT=8080
```

## How to Run Locally

### Prerequisites

- Java 17
- MySQL 8.x
- Maven

### Steps

1. Clone the repository

```bash
git clone https://github.com/YOUR_USERNAME/expensetracker.git
cd expensetracker
```

2. Create the MySQL database

```sql
CREATE DATABASE expense_tracker;
```

3. Optionally set environment variables if you want to override the local defaults

Windows PowerShell:

```powershell
$env:DB_HOST="localhost"
$env:DB_PORT="3306"
$env:DB_NAME="expense_tracker"
$env:DB_USERNAME="root"
$env:DB_PASSWORD=""
$env:SERVER_PORT="8080"
```

macOS / Linux:

```bash
export DB_HOST=localhost
export DB_PORT=3306
export DB_NAME=expense_tracker
export DB_USERNAME=root
export DB_PASSWORD=
export SERVER_PORT=8080
```

4. Run the application

```bash
./mvnw spring-boot:run
```

Since `dev` is the default profile, the app will connect to `localhost:3306` unless you override the environment variables.

5. Open Swagger UI at `http://localhost:8080/swagger-ui/index.html`

6. Check actuator endpoints

```bash
curl http://localhost:8080/actuator/health
curl http://localhost:8080/actuator/info
curl http://localhost:8080/actuator/metrics
```

## Running Specific Profiles

Run the Docker profile:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=docker
```

Run the production profile:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

Run the packaged jar with an explicit profile:

```bash
java -jar -Dspring.profiles.active=prod target/expensetracker-0.0.1-SNAPSHOT.jar
```

## Docker Profile Notes

The `docker` profile expects MySQL to be reachable at host `mysql`.

Example Docker-oriented values:

```env
DB_HOST=mysql
DB_PORT=3306
DB_NAME=expense_tracker
DB_USERNAME=root
DB_PASSWORD=
SERVER_PORT=8080
```

## Run With Docker

The Docker setup keeps the normal Maven and JAR workflow unchanged and adds a demo-friendly container stack for the Spring Boot app plus MySQL.

### Prerequisites

- Docker
- Docker Compose v2

### Build the app image

```bash
docker build -t expensetracker-app .
```

### Start the full stack

```bash
docker compose up --build
```

This starts:

- the Spring Boot app on `http://localhost:8080`
- MySQL 8 on `localhost:3306`

The compose setup uses the `docker` Spring profile automatically and passes database settings through environment variables.

### Stop the stack

```bash
docker compose down
```

To stop the containers and also remove the MySQL named volume:

```bash
docker compose down -v
```

### Verify the Docker demo

After `docker compose up --build`, check:

```bash
curl http://localhost:8080/actuator/health
curl http://localhost:8080/actuator/info
curl http://localhost:8080/api/health
curl http://localhost:8080/swagger-ui/index.html
```

You can also inspect container health:

```bash
docker compose ps
docker compose logs -f app
docker compose logs -f mysql
```

## AWS Deployment

### Architecture

Client (Browser) --> AWS EC2 (Spring Boot :8080) --> AWS RDS (MySQL :3306)

### EC2 Setup

1. Launch EC2 instance (Amazon Linux 2023, t3.micro)
2. Configure Security Group: ports 22, 80, 8080
3. Install Java 17: `sudo yum install java-17-amazon-corretto -y`
4. Install Git: `sudo yum install git -y`

### RDS Setup

1. Create RDS MySQL instance (db.t4g.micro, Free Tier)
2. Enable Public Access
3. Configure Security Group: port 3306
4. Create database: `CREATE DATABASE expense_tracker;`

### Deploy

```bash
git clone https://github.com/YOUR_USERNAME/expensetracker.git
cd expensetracker
chmod +x mvnw
./mvnw clean package -DskipTests

export DB_HOST=your-rds-endpoint.rds.amazonaws.com
export DB_PORT=3306
export DB_NAME=expense_tracker
export DB_USERNAME=admin
export DB_PASSWORD=your_password
export SERVER_PORT=8080

nohup java -jar -Dspring.profiles.active=prod target/expensetracker-0.0.1-SNAPSHOT.jar > app.log 2>&1 &
```

### Useful Commands

```bash
tail -f app.log
ps aux | grep expensetracker
pkill -f expensetracker
```

## Sample API Requests

### Create an Expense

```bash
curl -X POST http://3.107.0.116:8080/api/expenses -H "Content-Type: application/json" -d '{"title":"Lunch","amount":50,"type":"EXPENSE","category":"FOOD","transactionDate":"2026-04-09"}'
```

### Create an Income

```bash
curl -X POST http://3.107.0.116:8080/api/expenses -H "Content-Type: application/json" -d '{"title":"Monthly Salary","amount":25000,"type":"INCOME","category":"SALARY","transactionDate":"2026-04-01"}'
```

### Get All Records

```bash
curl http://3.107.0.116:8080/api/expenses
```

### Update a Record

```bash
curl -X PUT http://3.107.0.116:8080/api/expenses/1 -H "Content-Type: application/json" -d '{"title":"Lunch updated","amount":200,"type":"EXPENSE","category":"FOOD","transactionDate":"2026-04-09"}'
```

### Delete a Record

```bash
curl -X DELETE http://3.107.0.116:8080/api/expenses/1
```

### Filter Records

```bash
curl "http://3.107.0.116:8080/api/expenses/filter?type=EXPENSE&category=FOOD&startDate=2026-01-01&endDate=2026-12-31"
```

### Paginated Results

```bash
curl "http://3.107.0.116:8080/api/expenses/paged?page=0&size=5&sortBy=amount&sortDir=desc"
```

### Monthly Summary

```bash
curl "http://3.107.0.116:8080/api/summary/monthly?year=2026&month=4"
```

### Current Month Summary

```bash
curl http://3.107.0.116:8080/api/summary/monthly/current
```

### Yearly Summary

```bash
curl "http://3.107.0.116:8080/api/summary/yearly?year=2026"
```

### Health Check

```bash
curl http://3.107.0.116:8080/api/health
curl http://3.107.0.116:8080/api/health/detail
```

### Actuator Endpoints

```bash
curl http://3.107.0.116:8080/actuator/health
curl http://3.107.0.116:8080/actuator/info
curl http://3.107.0.116:8080/actuator/metrics
```

## Development Trace

| Step | Commit | Description |
|---|---|---|
| 1 | refactor: add GlobalExceptionHandler and standardize API response | Unified exception handling |
| 2 | feat: add filtering by type, category, and date range | Dynamic JPQL filtering |
| 3 | feat: add pagination and sorting support | Paged results with sort |
| 4 | feat: add monthly and yearly summary statistics | Summary with category breakdown |
| 5 | chore: add Swagger/OpenAPI documentation and CORS | Interactive API docs |
| 6 | feat: add production profile for AWS deployment | EC2 and RDS configuration |
| 7 | feat: add detailed health check with database and system info | Enhanced monitoring |
| 8 | docs: add comprehensive README | Project documentation |

## Course Information

- Course: COMP4442 - Service Computing
- Institution: The Hong Kong Polytechnic University
