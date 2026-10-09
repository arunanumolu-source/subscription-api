# Subscription Management API

A REST API for managing telecom operators, plans, customers, and subscriptions.

The application supports customer registration and login, JWT authentication, role-based authorization, subscription management, and Redis-based JWT token blacklisting.

## Technologies

- Java 25
- Spring Boot 4
- Spring Data JPA / Hibernate
- Spring Security
- JWT
- MySQL 8
- Redis
- MapStruct
- Lombok
- Swagger / OpenAPI
- Maven
- Docker Compose

## Main Features

### Authentication

- Customer registration
- Login using email and password
- BCrypt password hashing
- JWT authentication
- Logout using Redis token blacklisting
- Roles:
    - `ADMIN`
    - `CUSTOMER`

### Operators

Administrators can:

- Create operators
- Update operators
- Delete operators
- View operators

### Plans

Administrators can:

- Create plans
- Update plans
- Delete plans
- View all plans

Customers can:

- View active plans
- View individual plans

Supported service types:

- `MOBILE`
- `INTERNET`

### Subscriptions

Customers can:

- Create subscriptions
- View their subscriptions
- View their active subscriptions
- Cancel subscriptions
- Change subscription plans

Business rules include:

- A customer can have a maximum of one active subscription per service type.
- Only active plans can be subscribed to.
- New subscriptions are created with `ACTIVE` status.
- Cancellation stores the cancellation date.
- A plan change must stay with the same operator.
- A plan change must stay within the same service type.
- Customers can only manage their own subscriptions.

## Database Model

```mermaid
erDiagram

    CUSTOMER ||--o{ SUBSCRIPTION : has
    PLAN ||--o{ SUBSCRIPTION : used_by
    OPERATOR ||--o{ PLAN : owns

    CUSTOMER {
        BIGINT id PK
        VARCHAR firstName
        VARCHAR lastName
        VARCHAR email
        VARCHAR password
        VARCHAR role
        DATETIME createdAt
        DATETIME updatedAt
    }

    OPERATOR {
        BIGINT id PK
        VARCHAR name
        VARCHAR description
        DATETIME createdAt
        DATETIME updatedAt
    }

    PLAN {
        BIGINT id PK
        VARCHAR name
        DECIMAL price
        VARCHAR serviceType
        INT dataLimitGb
        BOOLEAN active
        BIGINT operatorId FK
        DATETIME createdAt
        DATETIME updatedAt
    }

    SUBSCRIPTION {
        BIGINT id PK
        BIGINT customerId FK
        BIGINT planId FK
        VARCHAR status
        DATETIME startDate
        DATETIME cancellationDate
        DATETIME createdAt
        DATETIME updatedAt
    }