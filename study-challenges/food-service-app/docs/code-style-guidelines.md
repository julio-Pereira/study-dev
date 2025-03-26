# Code Style Guidelines

This document outlines the coding standards and best practices for the Food Service Management System (FSMS) project.

## General Guidelines

- Write clean, readable, and maintainable code
- Follow the SOLID principles
- Keep methods and classes focused on a single responsibility
- Use meaningful and descriptive names for variables, methods, and classes
- Include appropriate comments where necessary, but strive for self-documenting code
- Write unit tests for all non-trivial code
- Limit method length to 50 lines and class length to 500 lines
- Maintain 80% or higher test coverage

## Version Control

- Make small, frequent commits with descriptive messages
- Use the format: `[FSMS-XXX] Brief description of changes`
- Create feature branches from `develop` using the format: `feature/FSMS-XXX-brief-description`
- Create bugfix branches using the format: `bugfix/FSMS-XXX-brief-description`
- Submit pull requests for code review before merging
- Squash commits before merging to main branches

## Java Code Style (Backend)

### Formatting
- Use 4 spaces for indentation (no tabs)
- Line width: 120 characters maximum
- Use braces for all control structures, even single-line blocks
- Place opening braces on the same line as declarations
- Add a space after keywords and before opening parentheses

### Naming Conventions
- Classes: PascalCase (e.g., `OrderService`)
- Interfaces: PascalCase, often with an "I" prefix (e.g., `IOrderService`)
- Methods: camelCase (e.g., `processOrder()`)
- Variables: camelCase (e.g., `orderItems`)
- Constants: UPPER_SNAKE_CASE (e.g., `MAX_ORDER_ITEMS`)
- Packages: all lowercase, no underscores (e.g., `com.fsms.orderservice`)

### Code Organization
- Organize imports alphabetically
- Group similar methods together
- Order class members: static variables, instance variables, constructors, methods
- Place public methods before private methods
- Keep interfaces focused and cohesive

### Documentation
- Use JavaDoc for public APIs and interfaces
- Document all exceptions thrown by a method
- Include parameter and return value descriptions

### Maven
- Organize dependencies alphabetically
- Keep versions in properties section
- Use consistent version for Spring Boot starters

## JavaScript/TypeScript Code Style (Frontend)

### Formatting
- Use 2 spaces for indentation (no tabs)
- Line width: 100 characters maximum
- Use semicolons
- Use single quotes for strings
- Add trailing commas in objects and arrays
- Use template literals for string concatenation

### Naming Conventions
- Components: PascalCase (e.g., `OrderList`)
- Functions: camelCase (e.g., `processOrder()`)
- Variables: camelCase (e.g., `orderItems`)
- Constants: UPPER_SNAKE_CASE (e.g., `MAX_ORDER_ITEMS`)
- Files: same as the component they contain (e.g., `OrderList.js`)

### React Specific
- Use functional components with hooks instead of class components
- Keep components small and focused
- Follow the container/presentational pattern
- Use context for global state
- Use prop-types or TypeScript for type checking

### State Management
- Use Redux for complex global state
- Use component state for simple local state
- Follow the Redux duck pattern for organizing Redux code
- Use Redux Toolkit to reduce boilerplate

### Testing
- Write tests for all components
- Test behavior, not implementation
- Use meaningful test descriptions
- Mock external dependencies

## SQL Style Guidelines

- Use uppercase for SQL keywords (e.g., `SELECT`, `FROM`, `WHERE`)
- Use snake_case for table and column names
- Include comments for complex queries
- Use meaningful and descriptive names for tables and columns
- Prefix table names with a domain identifier (e.g., `order_items`, `inventory_products`)

## API Design Guidelines

- Follow RESTful principles
- Use nouns for resource endpoints (e.g., `/orders`, not `/getOrders`)
- Use proper HTTP methods (GET, POST, PUT, DELETE)
- Return appropriate status codes
- Version APIs in the URL (e.g., `/api/v1/orders`)
- Use consistent response formats
- Document all APIs with Swagger/OpenAPI

## Automated Code Quality Tools

The following tools are integrated into our CI/CD pipeline:

### Backend
- Checkstyle for Java style enforcement
- PMD for code analysis
- JaCoCo for test coverage
- SonarQube for comprehensive code quality

### Frontend
- ESLint for JavaScript/TypeScript linting
- Prettier for code formatting
- Jest for test coverage
- SonarQube for comprehensive code quality

## Code Review Checklist

When reviewing code, consider the following:

1. Does the code follow the style guidelines?
2. Is the code readable and maintainable?
3. Are there appropriate tests with good coverage?
4. Does the code solve the problem it was intended to solve?
5. Are there any security concerns?
6. Is the error handling comprehensive?
7. Is the code performant and optimized?
8. Is there any duplicate code that could be refactored?

## Compliance Requirements

- All code must comply with GDPR requirements
- PII data must be properly secured and encrypted
- Payment processing code must follow PCI DSS standards
