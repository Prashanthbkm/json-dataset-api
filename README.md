# JSON Dataset API

A Spring Boot REST API for storing JSON dataset records in a relational database and querying them using dynamic **Group By** and **Sort By** operations.

## Live Application

- **Live Application:** https://json-dataset-api-4895.onrender.com/
- **Swagger API Documentation:** https://json-dataset-api-4895.onrender.com/swagger-ui/index.html
- **OpenAPI Specification:** https://json-dataset-api-4895.onrender.com/v3/api-docs
- **GitHub Repository:** https://github.com/Prashanthbkm/json-dataset-api

The application is deployed on Render and uses a MySQL-compatible TiDB Cloud database.

## Features

- Insert JSON records into a named dataset.
- Persist records in a relational database.
- Dynamically group records using any JSON field.
- Dynamically sort records using any JSON field.
- Support ascending (`asc`) and descending (`desc`) sorting.
- Validate API requests and handle errors.
- Interactive API documentation using Swagger / OpenAPI.
- RESTful API design.

## Technology Stack

| Component | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 4.1.2 |
| Database | MySQL / TiDB Cloud |
| Persistence | Spring Data JPA / Hibernate |
| Build Tool | Maven |
| Testing | JUnit 5 and Mockito |
| API Documentation | Swagger / OpenAPI |
| Deployment | Render |
| Containerization | Docker |

## Prerequisites

Make sure the following are installed:

- Java 17 or later
- MySQL Server (for local development)
- Git (optional)
- Maven (optional, Maven Wrapper is included)

## Database Configuration

Create the database in MySQL:

```sql
CREATE DATABASE json_dataset_db;
```

Configure the following environment variables before starting the application:

| Variable | Description |
|---|---|
| `DB_URL` | Database connection URL |
| `DB_USERNAME` | Database username |
| `DB_PASSWORD` | Database password |

Default local database URL:

```text
jdbc:mysql://localhost:3306/json_dataset_db
```

The application uses environment variables for database credentials. Do not commit passwords or other sensitive information to the repository.

For the deployed application, TiDB Cloud provides the MySQL-compatible database connection.

## How to Run the Application Locally

### 1. Clone the repository

```bash
git clone https://github.com/Prashanthbkm/json-dataset-api.git
```

### 2. Navigate to the project directory

```bash
cd json-dataset-api
```

### 3. Configure database environment variables

Set `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD` in your environment.

### 4. Start the application

**Windows:**

```cmd
mvnw.cmd spring-boot:run
```

**Linux / macOS:**

```bash
./mvnw spring-boot:run
```

The application starts at:

```text
http://localhost:8080
```

### 5. Open Swagger Documentation

```text
http://localhost:8080/swagger-ui/index.html
```

## REST API Endpoints

### 1. Insert a JSON Record

**Method:** `POST`

**Endpoint:**

```text
/api/dataset/{datasetName}/record
```

**Example Request:**

```http
POST /api/dataset/employee_dataset/record
Content-Type: application/json
```

**Request Body:**

```json
{
  "id": 1,
  "name": "John Doe",
  "age": 30,
  "department": "Engineering"
}
```

**Successful Response — HTTP 201 Created:**

```json
{
  "message": "Record added successfully",
  "dataset": "employee_dataset",
  "recordId": 1
}
```

### 2. Query Records Using Group By

**Method:** `GET`

**Endpoint:**

```text
/api/dataset/{datasetName}/query?groupBy={field}
```

**Example Request:**

```http
GET /api/dataset/employee_dataset/query?groupBy=department
```

**Successful Response — HTTP 200 OK:**

```json
{
  "groupedRecords": {
    "Engineering": [
      {
        "id": 1,
        "name": "John Doe",
        "age": 30,
        "department": "Engineering"
      }
    ],
    "Marketing": [
      {
        "id": 3,
        "name": "Alice Brown",
        "age": 28,
        "department": "Marketing"
      }
    ]
  }
}
```

The `groupBy` parameter dynamically determines the JSON field used for grouping.

### 3. Query Records Using Sort By

**Method:** `GET`

**Endpoint:**

```text
/api/dataset/{datasetName}/query?sortBy={field}&order={asc|desc}
```

**Ascending Order:**

```http
GET /api/dataset/employee_dataset/query?sortBy=age&order=asc
```

**Descending Order:**

```http
GET /api/dataset/employee_dataset/query?sortBy=age&order=desc
```

**Successful Response — HTTP 200 OK:**

```json
{
  "sortedRecords": [
    {
      "id": 2,
      "name": "Jane Smith",
      "age": 25,
      "department": "Engineering"
    },
    {
      "id": 3,
      "name": "Alice Brown",
      "age": 28,
      "department": "Marketing"
    },
    {
      "id": 1,
      "name": "John Doe",
      "age": 30,
      "department": "Engineering"
    }
  ]
}
```

The `sortBy` parameter specifies the JSON field, while `order` specifies the sorting direction.

## Request Validation and Error Handling

The API validates incoming requests and returns appropriate HTTP error responses for invalid requests.

Examples of invalid requests include:

- Missing both `groupBy` and `sortBy` parameters.
- Supplying an invalid sorting order.
- Providing a JSON array instead of a JSON object when inserting a record.
- Providing invalid dataset or field parameters.

## Running Tests

Run the service unit tests using Maven.

**Windows:**

```cmd
mvnw.cmd -Dtest=DatasetRecordServiceTest test
```

**Linux / macOS:**

```bash
./mvnw -Dtest=DatasetRecordServiceTest test
```

The service tests cover record insertion, JSON validation, grouping, and ascending and descending sorting.

## Deployment

The application is deployed using Render with Docker.

- Source code is maintained in a public GitHub repository.
- Docker is used to build and run the Spring Boot application.
- TiDB Cloud is used as the deployed MySQL-compatible database.
- Database credentials are configured through Render environment variables.
- The application uses the `PORT` environment variable provided by Render.

## Project Structure

```text
json-dataset-api/
│
├── src/
│   ├── main/
│   │   ├── java/com/freightfox/jsondatasetapi/
│   │   │   ├── controller/
│   │   │   ├── dto/
│   │   │   ├── entity/
│   │   │   ├── exception/
│   │   │   ├── repository/
│   │   │   ├── service/
│   │   │   ├── HomeController.java
│   │   │   └── JsonDatasetApiApplication.java
│   │   │
│   │   └── resources/
│   │       └── application.properties
│   │
│   └── test/
│       └── java/com/freightfox/jsondatasetapi/
│
├── Dockerfile
├── pom.xml
├── mvnw
├── mvnw.cmd
└── README.md
```

## Repository

GitHub: https://github.com/Prashanthbkm/json-dataset-api

---

**Developed as a Backend Engineering Assignment using Java and Spring Boot.**
