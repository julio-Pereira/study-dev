# Food Service Management System - Technology Stack Specification

## Overview

This document details the finalized technology stack for the Food Service Management System (FSMS), a microservices-based application for restaurant operations management. This specification ensures compatibility and consistency across all services in the system.

## Backend Technologies

### Core

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 21 | Primary programming language |
| Spring Boot | 3.4.4 | Application framework |
| Spring Cloud | 2024.0.0 | Microservices support |
| Spring Security | (Spring Boot managed) | Authentication and authorization |
| Spring Data JPA | (Spring Boot managed) | Database access |
| Spring Data MongoDB | (Spring Boot managed) | NoSQL database access |
| Maven | 3.8.0+ | Dependency management and build |

### Dependencies & Libraries

| Technology | Version | Purpose |
|------------|---------|---------|
| Lombok | 1.18.28 | Reduce boilerplate code |
| MapStruct | 1.5.3.Final | Object mapping |
| Hibernate | 6.5.0.Final | ORM implementation |
| Flyway | (Spring Boot managed) | Database migrations |
| JWT (jjwt) | 0.12.3 | JWT token handling |
| SpringDoc OpenAPI | 2.1.0 | API documentation |
| Micrometer | 1.11.1 | Application metrics |

### Testing

| Technology | Version | Purpose |
|------------|---------|---------|
| JUnit 5 | (Spring Boot managed) | Unit testing |
| Mockito | (Spring Boot managed) | Mocking for tests |
| Testcontainers | 1.19.8 | Integration testing with containers |
| JaCoCo | 0.8.10 | Code coverage reporting |

## Frontend Technologies

### Core

| Technology | Version | Purpose |
|------------|---------|---------|
| Node.js | 18.0.0+ | JavaScript runtime |
| npm | 9.0.0+ | Package management |
| React | 18.x | UI library |
| Redux | 4.x | State management |
| React Router | 6.x | Routing |

### UI Components & Styling

| Technology | Version | Purpose |
|------------|---------|---------|
| Material-UI | 5.x | UI component library |
| CSS Modules | - | Component-level styling |
| Sass | 1.x | CSS preprocessing |

### Testing

| Technology | Version | Purpose |
|------------|---------|---------|
| Jest | 29.x | JavaScript testing |
| React Testing Library | 14.x | Component testing |
| Cypress | 12.x | End-to-end testing |
| MSW (Mock Service Worker) | 1.x | API mocking |

## Data Persistence

### Databases

| Technology | Version | Purpose |
|------------|---------|---------|
| PostgreSQL | 15 | Primary relational database |
| MongoDB | 6.0 | NoSQL database for unstructured data |
| Redis | 7.0 | Caching and session management |

### Connection Pools & Drivers

| Technology | Version | Purpose |
|------------|---------|---------|
| HikariCP | (Spring Boot managed) | JDBC connection pooling |
| PostgreSQL JDBC Driver | 42.6.0 | PostgreSQL Java driver |
| MongoDB Java Driver | 4.9.1 | MongoDB Java driver |

## Messaging & Integration

| Technology | Version | Purpose |
|------------|---------|---------|
| RabbitMQ | 3.x | Message broker |
| Spring AMQP | (Spring Boot managed) | RabbitMQ integration |
| Spring Cloud Gateway | (Spring Cloud managed) | API Gateway |

## DevOps & Infrastructure

### Containerization & Orchestration

| Technology | Version | Purpose |
|------------|---------|---------|
| Docker | 20.10.0+ | Containerization |
| Docker Compose | 2.5.0+ | Container orchestration (development) |
| Kubernetes | 1.26+ | Container orchestration (production) |

### CI/CD

| Technology | Version | Purpose |
|------------|---------|---------|
| GitHub Actions | Latest | CI/CD pipeline |
| SonarQube | 9.x | Code quality |

### Monitoring & Logging

| Technology | Version | Purpose |
|------------|---------|---------|
| Prometheus | 2.x | Metrics collection |
| Grafana | 9.x | Metrics visualization |
| Elasticsearch | 8.x | Log aggregation |
| Logstash | 8.x | Log processing |
| Kibana | 8.x | Log visualization |

## Security

| Technology | Version | Purpose |
|------------|---------|---------|
| OAuth 2.0 | - | Authorization framework |
| JWT | - | Token-based authentication |
| HTTPS/TLS | 1.3 | Transport layer security |
| Snyk | Latest | Dependency vulnerability scanning |
| OWASP ZAP | Latest | Security testing |

## Development Environment

| Technology | Version | Purpose |
|------------|---------|---------|
| Git | 2.30.0+ | Version control |
| IntelliJ IDEA | Latest | Backend IDE |
| Visual Studio Code | Latest | Frontend IDE |
| Postman | Latest | API testing |

## Compatibility Matrix

The following combinations have been verified for compatibility:

| Java | Spring Boot | Node.js | npm | PostgreSQL | MongoDB | Redis | RabbitMQ |
|------|-------------|---------|-----|------------|---------|-------|----------|
| 21   | 3.4.4       | 18.x    | 9.x | 15         | 6.0     | 7.0   | 3.x      |

## Service-Specific Technologies

### Authentication Service
- Spring Security
- JWT
- OAuth 2.0

### Order Service
- Spring State Machine for order workflow

### Inventory Service
- Scheduled tasks for stock alerts

### Payment Service
- Integration with payment gateways (Stripe, PayPal)

### Analytics Service
- Spring Batch for ETL processes
- Apache POI for report generation

### Notification Service
- WebSockets for real-time notifications
- Email integration (JavaMail)
- SMS integration (Twilio)

## Dependency Management

### Maven Configuration
All backend services inherit from a parent POM that defines:
- Common dependencies
- Plugin management
- Property definitions
- Dependency versions

### npm Configuration
Frontend applications use a shared package.json approach for:
- Common dependencies
- Development tools
- Build scripts

## Version Compatibility Notes

1. **Spring Boot 3.4.4 Compatibility**: Requires Java 17+, we're using Java 21 for advanced language features.
2. **Node.js/npm Compatibility**: Node.js 18.x is LTS and fully compatible with the React ecosystem.
3. **Database Compatibility**: PostgreSQL 15, MongoDB 6.0, and Redis 7.0 are all compatible with their respective Spring Data modules.
4. **Container Compatibility**: All selected databases have official Docker images that are regularly maintained.

## Updates and Maintenance

This technology stack specification will be reviewed and updated:
- Quarterly for minor version updates
- As needed for security patches
- Following major releases of core technologies (Spring Boot, React, etc.)

## Starter Dependencies & Templates

Starter templates with the configured dependencies are available in:
- `/templates/backend-service-template` - Spring Boot service template
- `/templates/frontend-app-template` - React application template

---

*This document was approved on March 31, 2025, as part of FSMS-002: Technology Stack Finalization.*
