# Food Service Management System (FSMS)

A comprehensive solution for restaurant operations management, including order processing, inventory tracking, staff management, and business analytics.

## Project Overview

The Food Service Management System is designed to streamline restaurant operations, enhance customer experience, and provide valuable insights for business optimization. This system handles everything from customer orders and inventory management to staff scheduling and financial reporting.

## Repository Structure

```
food-service-app/
├── fs-services/            # Backend microservices
│   ├── auth-service/       # Authentication and authorization
│   ├── order-service/      # Order processing and management
│   ├── inventory-service/  # Inventory tracking and management
│   ├── payment-service/    # Payment processing
│   ├── analytics-service/  # Business analytics and reporting
│   └── notification-service/ # Notifications and alerts
├── fs-user-interface/      # Frontend applications
│   ├── admin-portal/       # Management dashboard
│   ├── staff-app/          # Staff interface
│   └── customer-app/       # Customer-facing application
├── database/               # Database scripts and migrations
├── docs/                   # Project documentation
└── scripts/                # Utility scripts
```

## Getting Started

### Prerequisites

- Docker and Docker Compose
- Java 21+
- Node.js 18+
- npm 9+
- Maven 3.8+

### Development Setup

1. Clone the repository:
```bash
git clone https://github.com/julio-Pereira/study-dev.git
cd study-challenges/food-service-app
```

2. Start the development environment:
```bash
docker-compose -f docker-compose.dev.yml up -d
```

3. Access the applications:
   - Admin Portal: http://localhost:3000
   - API Documentation: http://localhost:8080/swagger-ui.html
   - Database Admin: http://localhost:8081

For detailed setup instructions, see [Development Environment Configuration](./docs/development-environment.md).

## Documentation

- [Architecture Documentation](./docs/architecture.md)
- [API Documentation](./docs/api.md)
- [Dev Guide](./docs/dev-guide.md)
- [Development Workflow](./docs/project-board.md)
- [Testing Strategy](./docs/testing.md)

## Contributing

Please read our [Contributing Guidelines](./docs/contributing.md) before submitting pull requests.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Support

For support, please contact the project maintainers or create an issue in the GitHub repository.
