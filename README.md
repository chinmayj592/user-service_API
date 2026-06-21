# User Service API

A RESTful backend API for user management with complete CRUD operations, soft delete functionality, validation, and optimistic locking.

## 🚀 Features

- **User Management**: Create, Read, Update, and Delete (CRUD) operations
- **Soft Delete**: Users are marked as deleted without removing from database
- **Unique Constraints**: Email, Aadhaar, and PAN number are unique
- **Validation**: Bean validation (JSR-380) for all requests
- **Pagination**: Get users with pagination support
- **Optimistic Locking**: Version control to handle concurrent updates
- **CORS Enabled**: Allow requests from frontend (http://localhost:5173)
- **Global Exception Handling**: Centralized error handling with custom exceptions
- **Soft Deletes**: `@SQLDelete` with `@Where` clause for data integrity

## 📋 Tech Stack

- **Java 17**
- **Spring Boot 3.2.6**
- **Spring Data JPA** (ORM)
- **MySQL 8.0**
- **Lombok** (Reduces boilerplate)
- **Validation** (Bean Validation)
- **Maven** (Build tool)

## 📁 Project Structure

```
src/main/java/com/chinmay/user_service_api/
├── controller/     → REST endpoints
├── service/        → Business logic
├── repository/     → Database operations
├── entity/         → User entity with soft delete
├── dto/            → Request/Response DTOs
├── mapper/         → Entity-DTO mapping
├── exception/      → Custom exceptions & global handler
└── config/         → CORS configuration
```

## 🛠️ Prerequisites

- **Java 17+**
- **MySQL 8.0+**
- **Maven 3.6+**

## 🚀 How to Run

### 1. Database Setup
Create a MySQL database:
```sql
CREATE DATABASE user_service26;
CREATE USER 'user_service_user26'@'localhost' IDENTIFIED BY 'UserService1234';
GRANT ALL PRIVILEGES ON user_service26.* TO 'user_service_user26'@'localhost';
FLUSH PRIVILEGES;
```

### 2. Configure Application (Optional)
Edit `src/main/resources/application.properties` if needed:
- Database URL: `jdbc:mysql://localhost:3306/user_service26`
- Username: `user_service_user26`
- Password: `UserService1234`
- Server port: `8080`

### 3. Run Backend
```bash
# Using Maven
mvn clean install
mvn spring-boot:run

# Or directly using Maven wrapper (Windows)
.\mvnw.cmd spring-boot:run
```

Backend will be available at: `http://localhost:8080`

## 📡 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/users/test` | Test endpoint |
| POST | `/api/v1/users` | Create new user |
| GET | `/api/v1/users?page=0&size=10` | Get all users (paginated) |
| PUT | `/api/v1/users/{id}` | Update user |
| DELETE | `/api/v1/users/{id}` | Soft delete user |

## 📝 User Request DTO Fields

- `name` (required)
- `email` (required, unique)
- `primaryMobile` (required)
- `secondaryMobile` (optional)
- `aadhaar` (required, unique, 12 chars)
- `pan` (required, unique, 10 chars)
- `dateOfBirth` (required)
- `placeOfBirth` (optional)
- `currentAddress` (required)
- `permanentAddress` (required)

## Important Configuration

- **DDL Auto**: Set to `update` to persist data across restarts
- **CORS**: Enabled for `http://localhost:5173` (Vite frontend)
- **Soft Delete**: Automatically filters deleted records from queries
- **Timestamp Tracking**: `createdAt` and `updatedAt` auto-managed

##  Building for Production

```bash
mvn clean package
java -jar target/user-service-api-0.0.1-SNAPSHOT.jar
```

