# Expense Tracker API

A Spring Boot service computing application for managing personal income and expense records.  
This project provides RESTful APIs for transaction management, filtering, pagination, summary generation, CSV export, health monitoring, automated testing, Docker-based execution, and AWS EC2 deployment.

--

## 1. Project Overview

This project was developed for **COMP4442 Service and Cloud Computing**.  
It focuses on the backend service layer rather than a large frontend interface. The system is designed to manage financial records through a clean REST API and to demonstrate practical service deployment on both local and cloud environments.

The application supports:

- Creating, reading, updating, and deleting income and expense records
- Filtering records by type, category, and date
- Pagination and sorting for scalable queries
- Monthly and yearly summary generation
- CSV export
- Health monitoring with both custom endpoints and Spring Boot Actuator
- Docker Compose deployment
- AWS EC2 deployment
- Automated testing with GitHub Actions

---

## 2. System Architecture

```text
Client Browser / Swagger UI
        |
        v
Spring Boot REST API
(Controllers)
        |
        v
Service Layer
        |
        v
Spring Data JPA Repository
        |
        v
MySQL Database
```

For deployment, the application uses a two-container Docker Compose setup:

- **expensetracker-app**: Spring Boot backend
- **expensetracker-mysql**: MySQL 8.4 database

This makes local testing and EC2 deployment consistent.

---

## 3. Tech Stack

| Technology | Version | Purpose |
|---|---:|---|
| Java | 17 | Core programming language |
| Spring Boot | 3.5.13 | REST API framework |
| Spring Data JPA | - | Persistence layer |
| MySQL | 8.4 | Primary relational database |
| H2 | - | In-memory database for tests |
| Maven | - | Build and dependency management |
| springdoc-openapi | 2.8.6 | Swagger UI / OpenAPI documentation |
| Spring Boot Actuator | - | Monitoring and health endpoints |
| Docker Compose | v2 | Container orchestration |
| GitHub Actions | - | Continuous integration |
| AWS EC2 | - | Cloud deployment environment |

---

## 4. Key Features

### 4.1 Expense and Income Management
The system stores both expenses and incomes using a unified `Expense` entity with a transaction type field.

### 4.2 Filtering, Pagination, and Sorting
Users can retrieve only the records they need by applying:
- type filtering
- category filtering
- date range filtering
- pagination
- sorting

### 4.3 Monthly and Yearly Summaries
The service can generate:
- current monthly summary
- summary for a specific month
- yearly summary across all months

### 4.4 CSV Export
Stored records can be exported in CSV format for reporting and further analysis.

### 4.5 Health and Monitoring
The project provides:
- custom health endpoints
- Spring Boot Actuator monitoring endpoints

### 4.6 Automated Testing and CI
Unit tests and integration tests are included, and GitHub Actions runs the test suite automatically.

---

## 5. Data Model

The core entity is `Expense`.

### Main fields
- `id`
- `title`
- `description`
- `amount`
- `type`
- `category`
- `transactionDate`
- `createdAt`

### Transaction types
- `INCOME`
- `EXPENSE`

### Categories
- `FOOD`
- `TRANSPORT`
- `ENTERTAINMENT`
- `SHOPPING`
- `BILLS`
- `HEALTH`
- `EDUCATION`
- `SALARY`
- `INVESTMENT`
- `OTHER`

---

## 6. API Overview

### 6.1 Expense API
- `POST /api/expenses`
- `GET /api/expenses`
- `GET /api/expenses/{id}`
- `PUT /api/expenses/{id}`
- `DELETE /api/expenses/{id}`
- `GET /api/expenses/filter`
- `GET /api/expenses/paged`

### 6.2 Summary API
- `GET /api/summary/monthly/current`
- `GET /api/summary/monthly`
- `GET /api/summary/yearly`

### 6.3 Export API
- `GET /api/export/csv`

### 6.4 Health and Monitoring
- `GET /api/health`
- `GET /api/health/detail`
- `GET /actuator/health`
- `GET /actuator/info`
- `GET /actuator/metrics`

---

## 7. Example Request Bodies

### Create an expense
```json
{
  "title": "Lunch",
  "description": "Lunch at campus canteen",
  "amount": 55.5,
  "type": "EXPENSE",
  "category": "FOOD",
  "transactionDate": "2026-04-16"
}
```

### Create an income
```json
{
  "title": "Part-time salary",
  "description": "April part-time job payment",
  "amount": 8000,
  "type": "INCOME",
  "category": "SALARY",
  "transactionDate": "2026-04-15"
}
```

---

## 8. Profiles and Configuration

The project uses profile-based configuration.

### Profiles
- `dev` - local development
- `docker` - Docker Compose deployment
- `prod` - production-style externalized configuration

### Configuration files
- `application.properties`
- `application-dev.properties`
- `application-docker.properties`
- `application-prod.properties`

### Environment variables
The following variables are used for database and server configuration:

- `DB_HOST`
- `DB_PORT`
- `DB_NAME`
- `DB_USERNAME`
- `DB_PASSWORD`
- `SERVER_PORT`

### Example `.env`
```env
DB_HOST=localhost
DB_PORT=3306
DB_NAME=expense_tracker
DB_USERNAME=root
DB_PASSWORD=your_password
SERVER_PORT=8080
```

---

## 9. Local Run

### Option A: Recommended - Docker Compose
This is the most stable way to run the project locally.

```bash
docker compose up --build
```

To stop:
```bash
docker compose down
```

To stop and remove volumes:
```bash
docker compose down -v
```

### Local verification
Open in browser:
```text
http://localhost:8080/swagger-ui/index.html
http://localhost:8080/actuator/health
http://localhost:8080/api/health
```

### Option B: Run with Maven
If you want to run without Docker, make sure your local MySQL is available and your database environment variables are set correctly.

Example:

#### Windows PowerShell
```powershell
$env:DB_HOST="localhost"
$env:DB_PORT="3306"
$env:DB_NAME="expense_tracker"
$env:DB_USERNAME="root"
$env:DB_PASSWORD="your_password"
$env:SERVER_PORT="8080"
./mvnw spring-boot:run
```

#### macOS / Linux
```bash
export DB_HOST=localhost
export DB_PORT=3306
export DB_NAME=expense_tracker
export DB_USERNAME=root
export DB_PASSWORD=your_password
export SERVER_PORT=8080
./mvnw spring-boot:run
```

---

## 10. Docker Run

### Build and start in detached mode
```bash
docker compose up --build -d
```

### Check container status
```bash
docker compose ps
```

### View logs
```bash
docker compose logs --tail=100
```

### View app logs only
```bash
docker compose logs --tail=100 expensetracker-app
```

### View MySQL logs only
```bash
docker compose logs --tail=100 expensetracker-mysql
```

---

## 11. AWS EC2 Deployment Summary

The final cloud deployment uses **AWS EC2 + Docker Compose**.

### Deployment steps
1. Launch an Amazon Linux EC2 instance
2. Open:
   - port `22` for SSH
   - port `8080` for the web API
3. Install Docker
4. Install Docker Compose and Buildx
5. Clone the repository
6. Run:
   ```bash
   docker compose up --build -d
   ```
7. Verify deployment through the public EC2 address

### Useful EC2 commands
```bash
docker compose ps
docker compose logs --tail=100
curl http://localhost:8080/actuator/health
curl http://localhost:8080/api/health
```

### Public verification
```text
http://<EC2_PUBLIC_IP>:8080/swagger-ui/index.html
http://<EC2_PUBLIC_IP>:8080/actuator/health
http://<EC2_PUBLIC_IP>:8080/api/health
```

---

## 12. Testing

### Run all tests
```bash
./mvnw clean test
```

### Test coverage includes
- service-layer unit tests
- integration tests with MockMvc
- H2 in-memory database for isolated test execution

### Representative tested endpoints
- `POST /api/expenses`
- `GET /api/expenses`
- `GET /api/expenses/{id}`
- `GET /api/expenses/paged`
- `GET /api/summary/monthly/current`
- `GET /api/health`
- `GET /actuator/health`

---

## 13. CI

The project includes a GitHub Actions workflow for continuous integration.

### Workflow behavior
- triggered on push to `main`
- triggered on pull request to `main`
- uses Java 17
- runs:
  ```bash
  ./mvnw clean test
  ```

---

## 14. Demo Checklist

Before the live demo, confirm the following:

### Local / Docker
- Docker Desktop is running
- `docker compose up --build` succeeds
- `docker compose ps` shows both containers are up
- Swagger UI opens successfully
- `/actuator/health` returns `UP`
- `/api/health` returns a normal JSON response

### Cloud / EC2
- EC2 instance is running
- port `8080` is open
- Docker containers are up
- public URL is reachable
- Swagger UI and health endpoints work from an external browser

---

## 15. Sample Demo Flow

A stable demo sequence is:

1. Open `/actuator/health`
2. Open `/api/health`
3. Open Swagger UI
4. Create an expense
5. Create an income
6. Retrieve all records
7. Show paged query
8. Show monthly summary
9. Show CSV export
10. Mention Docker and AWS EC2 deployment

---

## 16. Project Structure

```text
expensetracker/
├── .github/
│   └── workflows/
├── .mvn/
├── src/
│   ├── main/
│   │   ├── java/com/comp4442/expensetracker/
│   │   │   ├── config/
│   │   │   ├── controller/
│   │   │   ├── dto/
│   │   │   ├── entity/
│   │   │   ├── exception/
│   │   │   ├── repository/
│   │   │   ├── service/
│   │   │   └── ExpensetrackerApplication.java
│   │   └── resources/
│   └── test/
├── Dockerfile
├── compose.yaml
├── pom.xml
├── mvnw
├── mvnw.cmd
└── README.md
```

---

## 17. Limitations

The current version is intended as a backend service prototype for coursework demonstration.

Not included in the current scope:
- authentication / login
- user-specific account isolation
- budget reminders
- graphical dashboard
- multi-user support

The Docker demo also uses a simple MySQL root account configuration, which is acceptable for coursework demonstration but not ideal for hardened production deployment.

---

## 18. Conclusion

This project demonstrates a complete Spring Boot service computing application with clean layering, practical deployment, and verifiable API behavior.  
It supports CRUD operations, filtering, pagination, monthly and yearly summaries, CSV export, health monitoring, automated testing, Docker-based execution, and AWS EC2 deployment.

The final result is not only a working local backend, but also a cloud-deployable service that can be demonstrated reliably through Swagger UI and monitoring endpoints.
