# Mini-API Assurance

REST API for insurance contract management developed with Spring Boot as a summer internship project.

## Features

- Client Management (Create, Read)
- Contract Management (Create, Read, Business rules for date validation)
- Claim Management (Create, Business rule: cannot file a claim on an inactive or expired contract)
- JWT Security (Token-based authentication, Registration, Login)
- Interactive Swagger/OpenAPI Documentation

## Prerequisites

- Java 17
- Maven 3.8+
- PostgreSQL

## Setup and Launch

1. **Clone the repository**
   ```bash
   git clone https://github.com/taheryhaia2/mini-api-assurance.git
   cd mini-api-assurance
   ```
2. **Configure the database**

   Create a PostgreSQL database named `assurance_db`.
   Update credentials in `src/main/resources/application.yml` if necessary (default: user=postgres, password=admin).

3. **Run the application**

   ```bash
   ./mvnw spring-boot:run
   ```

   Demo data (including an Admin account and sample contracts) is inserted automatically on startup.

4. **Access Swagger documentation**

   The interactive UI is available at:
   👉 http://localhost:8080/swagger-ui/index.html

## API Usage Workflow

- **Register**: Use `POST /api/auth/register` to create an account (e.g., ADMIN).
- **Login**: Use `POST /api/auth/login` to obtain a JWT Token.
- **Authorize**: Click the 🔒 Authorize button in Swagger, enter `Bearer <your_token>` to access protected routes.
- **Operations**: Create clients (`/api/clients`), contracts (`/api/contracts`), and file claims (`/api/contracts/{contractId}/claims`).

## Demo Accounts

- **Admin**: username: `admin` / password: `admin123`

## Architecture

- **Framework**: Spring Boot 3
- **Database**: PostgreSQL with Spring Data JPA (Hibernate)
- **Security**: Spring Security + JWT (JJWT)
- **Documentation**: Springdoc OpenAPI (Swagger)
