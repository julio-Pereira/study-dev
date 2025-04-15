# Financial Processing System

A microservice-based system for processing financial transactions, built with Spring Boot and deployed on Google Cloud Platform.

## Modules

- **common-library**: Shared models, DTOs, and utilities
- **transaction-ingestor-service**: API for receiving financial transactions
- **transaction-processor-service**: Service for processing and enriching transactions
- **transaction-storage-service**: Service for storing and querying transaction data
- **api-gateway-service**: API Gateway for routing and security

## Getting Started

### Prerequisites

- JDK 21 or higher
- Maven 3.8 or higher
- Docker and Docker Compose (for local development)
- Google Cloud SDK (for cloud deployment)

### Building the Project

```bash
mvn clean install