# Development Environment Configuration

This document outlines the standardized development environment for the Food Service Management System (FSMS) project.

## Prerequisites

All developers need to have the following tools installed:

- **Git** (v2.30.0 or higher)
- **Docker** (v20.10.0 or higher)
- **Docker Compose** (v2.5.0 or higher)
- **Java Development Kit (JDK)** (v17 or higher)
- **Node.js** (v18.0.0 or higher)
- **npm** (v9.0.0 or higher)
- **Maven** (v3.8.0 or higher)
- **IDE** (Recommended: IntelliJ IDEA for backend, VS Code for frontend)

## Local Development Setup

### 1. Clone the Repository

```bash
git clone https://github.com/your-organization/food-service-app.git
cd food-service-app
```

### 2. Backend Setup (fs-services)

```bash
cd fs-services
./mvnw clean install
```

#### Running Backend Services

Using Docker:
```bash
docker-compose up -d
```

Or directly:
```bash
./mvnw spring-boot:run
```

### 3. Frontend Setup (fs-user-interface)

```bash
cd ../fs-user-interface
npm install
```

#### Running Frontend

```bash
npm start
```

This will start the development server at http://localhost:3000

### 4. Database Setup

The project uses PostgreSQL as the primary database.

Using Docker (recommended):
```bash
docker-compose up -d postgres
```

Manual setup:
- Install PostgreSQL 14 or higher
- Create a database named `fsms_db`
- Run the initialization scripts in `./database/init.sql`

### 5. API Documentation

Swagger UI is available at http://localhost:8080/swagger-ui.html when the backend is running.

## Containerized Development Environment

We provide a complete containerized development environment for consistency across all team members.

```bash
docker-compose -f docker-compose.dev.yml up -d
```

This will set up:
- Backend services
- Frontend development server
- PostgreSQL database
- MongoDB instance
- Redis cache
- Mock payment service
- Adminer (database management tool)

## Environment Variables

Create a `.env` file in the project root with the following variables:

```
# Database Configuration
DB_HOST=localhost
DB_PORT=5432
DB_NAME=fsms_db
DB_USER=postgres
DB_PASSWORD=postgres

# JWT Configuration
JWT_SECRET=your-jwt-secret
JWT_EXPIRATION=86400000

# API Configuration
API_PORT=8080
```

## Testing

### Backend Testing

```bash
cd fs-services
./mvnw test
```

### Frontend Testing

```bash
cd fs-user-interface
npm test
```

### Integration Testing

```bash
./scripts/run-integration-tests.sh
```

## Troubleshooting

### Common Issues

1. **Port conflicts**: Ensure ports 8080, 3000, 5432, 27017, and 6379 are available
2. **Docker memory issues**: Increase Docker memory allocation to at least 4GB
3. **Database connection errors**: Verify credentials in `.env` file

### Support

If you encounter any issues, please:
1. Check the project wiki troubleshooting section
2. Reach out in the #dev-support Slack channel
