# Debt Manager API

![Java](https://img.shields.io/badge/Java-25-red)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.2-green)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16.13-blue)

REST API for managing personal debts with JWT authentication.

## Technology

- Java 25
- Spring Boot 4.0.2
- Spring Security + JWT
- PostgreSQL 16.13
- Maven

## Requirements

- Java 25+
- PostgreSQL 16+
- Maven

## Setup

1. Create a PostgreSQL database
2. Configure environment variables or create an `application-local.properties` file (see [Environment Variables](#environment-variables))
3. Run `mvn spring-boot:run`

## Main Endpoints

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/api/auth/register` | Create a new account | No |
| POST | `/api/auth/login` | Authenticate and receive JWT | No |
| GET | `/api/public/debt/types` | Get all possible debt categories | No |
| GET | `/api/debt/entries` | List all debt entries | JWT |
| POST | `/api/debt/entries` | Create a new debt entry | JWT |
| GET | `/api/debt/entry/{id}` | Get a specific debt entry | JWT |
| PUT | `/api/debt/entry/{id}` | Update a specific debt entry | JWT |
| DELETE | `/api/debt/entry/{id}` | Delete a specific debt entry | JWT |
| GET | `/api/debt/total-remaining` | Get total outstanding debt | JWT |

## Environment Variables

| Variable | Description |
|----------|-------------|
| `DB_USER` | Database username |
| `DB_PASS` | Database password |
| `JWT_SECRET` | Secret key for JWT signing |

Or use `application-local.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/your_database
spring.datasource.username=your_user
spring.datasource.password=your_password
jwt.secret=your_secret_key
```

## Request Examples

### Register a User

```http
POST /api/auth/register
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "password": "SecurePass123",
  "confirmPassword": "SecurePass123"
}
```

**Validation rules:**
- `firstName`: 2-50 characters
- `lastName`: 2-50 characters
- `email`: 5-100 characters, valid email format
- `password`: 8-100 characters, must contain at least one uppercase, one lowercase, one number, and no spaces

### Create a Debt Entry

```http
POST /api/debt/entries
Authorization: Bearer <your-jwt-token>
Content-Type: application/json

{
  "description": "Medical consultation",
  "moneyAmount": 75000,
  "creditor": "Clínica Los Andes",
  "type": {
    "id": 1
  },
  "dateLimit": "2026-05-15T10:00:00"
}
```

**Validation rules:**
- `description`: max 150 characters
- `moneyAmount`: positive number, cannot be null
- `creditor`: max 100 characters
- `type`: must be a valid debt type ID
- `dateLimit`: must be a future date
- `isPaid`: defaults to `false`
- `isActive`: defaults to `true`

## Roadmap

- [ ] Unit and integration tests (JUnit 5, Mockito)
- [ ] Frontend client (React or Angular)
