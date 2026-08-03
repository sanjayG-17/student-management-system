# Student Management System

A production-ready RESTful web application built with **Spring Boot** to manage college student records. This project is configured with a multi-profile database system (using H2 for zero-setup evaluation, and MySQL for production), validation checks, unified JSON API responses, robust global exception handling, and full Swagger UI documentation.

---

## 🚀 Key Features

* **Complete CRUD**: Create, Retrieve, Update, and Delete student records.
* **Smart Search**: Search students by department (case-insensitive) and retrieve students with a CGPA exceeding a specified threshold.
* **Input Validation**: Strict request payload validation using JSR-380 annotations (valid emails, 10-digit phone numbers, study year bounded between 1-4, CGPA ranges between 0.0-10.0).
* **Duplicate Protection**: Unique validation check preventing multiple records with the same email.
* **Unified Responses**: Standardized JSON response envelope for successful calls and error states alike.
* **Interactive API Playground**: Full Swagger/OpenAPI documentation.
* **Comprehensive Integration Tests**: Test coverage for all validation boundaries and failure paths.

---

## 🛠️ Technology Stack

* **Java Version**: 21 (LTS)
* **Spring Boot Version**: 3.3.2
* **Persistence**: Spring Data JPA / Hibernate
* **Database Options**:
  * **Development (Default)**: H2 In-Memory Database (Requires zero local database installation)
  * **Production**: MySQL Database (Configurable external database)
* **Documentation**: Springdoc OpenAPI / Swagger UI
* **Build Tool**: Maven

---

## 📁 Project Structure

```text
student-management-system/
├── pom.xml                                   # Maven Build Configuration
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/example/studentmanagement/
    │   │       ├── StudentManagementApplication.java # Entry Point
    │   │       ├── config/
    │   │       │   └── OpenApiConfig.java            # Swagger/OpenAPI Config
    │   │       ├── controller/
    │   │       │   └── StudentController.java        # REST Controller API Endpoints
    │   │       ├── dto/
    │   │       │   ├── ApiResponse.java              # Success JSON Envelope
    │   │       │   ├── ErrorResponse.java            # Error JSON Envelope
    │   │       │   └── StudentRequest.java           # Create/Update Input DTO
    │   │       ├── exception/
    │   │       │   ├── DuplicateEmailException.java   # Custom Business Exception
    │   │       │   ├── ResourceNotFoundException.java# Custom Business Exception
    │   │       │   └── GlobalExceptionHandler.java   # REST Controller Exception Handler
    │   │       ├── model/
    │   │       │   └── Student.java                  # JPA Entity Mapping
    │   │       ├── repository/
    │   │       │   └── StudentRepository.java        # Database Query Interface
    │   │       └── service/
    │   │           └── StudentService.java           # Core Business Service
    │   └── resources/
    │       ├── application.properties                # Core properties
    │       ├── application-dev.properties            # Dev / H2 Profile configurations
    │       └── application-prod.properties           # Prod / MySQL configurations
    └── test/
        └── java/
            └── com/example/studentmanagement/
                └── controller/
                    └── StudentControllerTest.java    # Integration & Validation Tests
```

---

## 🚦 Getting Started

### Prerequisites

Ensure you have the following installed:
* [Java Development Kit (JDK) 21](https://www.oracle.com/java/technologies/downloads/) (or JDK 17+)
* [Apache Maven](https://maven.apache.org/download.cgi)

---

### Running the Application

This project uses Spring Profiles to dictate database integration:

#### 1. Evaluation & Development Mode (H2 In-Memory DB - Zero Setup)
Run this command from the project root. The application defaults to the `dev` profile using H2:
```bash
mvn spring-boot:run
```
Once started:
* **Base URL**: `http://localhost:8081`
* **Swagger UI Docs**: [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)
* **H2 Database Console**: [http://localhost:8081/h2-console](http://localhost:8081/h2-console)
  * **JDBC URL**: `jdbc:h2:mem:studentdb`
  * **Username**: `sa`
  * **Password**: *(leave blank)*

#### 2. Production Mode (MySQL Database Integration)
1. Ensure your local/remote MySQL service is running.
2. Create a database named `student_db`:
   ```sql
   CREATE DATABASE student_db;
   ```
3. Open [application-prod.properties](file:///C:/Users/sanja/.gemini/antigravity-ide/scratch/student-management-system/src/main/resources/application-prod.properties) and update the credentials:
   ```properties
   spring.datasource.username=your_mysql_username
   spring.datasource.password=your_mysql_password
   ```
4. Start the application with the `prod` profile:
   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=prod
   ```

---

## 🧪 Running Automated Tests

The application contains complete integration tests using Spring's `MockMvc` covering all target test situations. To run the tests, execute:
```bash
mvn test
```

---

## 📖 API Documentation Reference

All API responses follow a consistent JSON structure.

### Response JSON Formats

#### 🟢 Success Envelope Format
```json
{
  "success": true,
  "message": "Student created successfully",
  "data": {
    "id": 1,
    "name": "Jane Doe",
    "email": "jane.doe@example.com",
    "phoneNumber": "9876543210",
    "department": "Computer Science",
    "yearOfStudy": 3,
    "cgpa": 9.2
  },
  "timestamp": "2026-08-02 09:55:00"
}
```

#### 🔴 Error Envelope Format (e.g., 400 Bad Request)
```json
{
  "success": false,
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed for request fields",
  "errors": {
    "phoneNumber": "Phone number must be exactly 10 digits",
    "email": "Email should be valid"
  },
  "timestamp": "2026-08-02 09:55:10"
}
```

---

### Endpoints Details & curl Samples

#### 1. Add a New Student
* **Endpoint**: `POST /api/students`
* **Headers**: `Content-Type: application/json`
* **Request Body**:
  ```json
  {
    "name": "Jane Doe",
    "email": "jane.doe@example.com",
    "phoneNumber": "9876543210",
    "department": "Computer Science",
    "yearOfStudy": 3,
    "cgpa": 9.2
  }
  ```
* **Sample Request**:
  ```bash
  curl -X POST http://localhost:8081/api/students \
    -H "Content-Type: application/json" \
    -d '{"name": "Jane Doe", "email": "jane.doe@example.com", "phoneNumber": "9876543210", "department": "Computer Science", "yearOfStudy": 3, "cgpa": 9.2}'
  ```

#### 2. Retrieve All Students
* **Endpoint**: `GET /api/students`
* **Sample Request**:
  ```bash
  curl http://localhost:8081/api/students
  ```

#### 3. Retrieve Student by ID
* **Endpoint**: `GET /api/students/{id}`
* **Sample Request**:
  ```bash
  curl http://localhost:8081/api/students/1
  ```

#### 4. Update Student Details
* **Endpoint**: `PUT /api/students/{id}`
* **Headers**: `Content-Type: application/json`
* **Request Body**:
  ```json
  {
    "name": "Jane Doe Updated",
    "email": "jane.doe@example.com",
    "phoneNumber": "9876543210",
    "department": "Information Technology",
    "yearOfStudy": 4,
    "cgpa": 9.5
  }
  ```
* **Sample Request**:
  ```bash
  curl -X PUT http://localhost:8081/api/students/1 \
    -H "Content-Type: application/json" \
    -d '{"name": "Jane Doe Updated", "email": "jane.doe@example.com", "phoneNumber": "9876543210", "department": "Information Technology", "yearOfStudy": 4, "cgpa": 9.5}'
  ```

#### 5. Delete a Student
* **Endpoint**: `DELETE /api/students/{id}`
* **Sample Request**:
  ```bash
  curl -X DELETE http://localhost:8081/api/students/1
  ```

#### 6. Search Students by Department
* **Endpoint**: `GET /api/students/search/department?dept={departmentName}`
* **Sample Request**:
  ```bash
  curl "http://localhost:8081/api/students/search/department?dept=computer%20science"
  ```

#### 7. Retrieve Students by CGPA Threshold
* **Endpoint**: `GET /api/students/search/cgpa?minCgpa={cgpaValue}`
* **Sample Request**:
  ```bash
  curl "http://localhost:8081/api/students/search/cgpa?minCgpa=8.5"
  ```
