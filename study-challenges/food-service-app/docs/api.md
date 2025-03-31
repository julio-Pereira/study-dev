# Food Service Management System API Documentation

## Overview

This document provides details about the RESTful APIs exposed by the Food Service Management System (FSMS). The APIs are organized by microservice and follow REST principles.

## Base URL

All API endpoints are available at:

```
http://localhost:8080/api/v1
```

When deployed to production, replace `localhost:8080` with the appropriate domain.

## Authentication

Most API endpoints require authentication. Authentication is handled via JWT tokens.

### Authentication Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST   | `/auth/login` | Authenticate user and get JWT token |
| POST   | `/auth/register` | Register a new user |
| POST   | `/auth/refresh-token` | Refresh an expired JWT token |
| POST   | `/auth/forgot-password` | Initiate password reset |
| POST   | `/auth/reset-password` | Complete password reset |
| POST   | `/auth/logout` | Invalidate JWT token |

### Authentication Headers

For secured endpoints, include the JWT token in the Authorization header:

```
Authorization: Bearer <jwt_token>
```

## User Service

### Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET    | `/users` | Get all users (admin only) |
| GET    | `/users/{id}` | Get user by ID |
| GET    | `/users/me` | Get current user profile |
| PUT    | `/users/{id}` | Update user |
| PUT    | `/users/me` | Update current user profile |
| DELETE | `/users/{id}` | Delete user (admin only) |
| GET    | `/users/{id}/roles` | Get user roles |
| PUT    | `/users/{id}/roles` | Update user roles (admin only) |

### Sample Request/Response

#### Get Current User Profile

Request:
```
GET /api/v1/users/me
Authorization: Bearer <jwt_token>
```

Response:
```json
{
  "id": 1,
  "username": "john.doe",
  "email": "john.doe@example.com",
  "fullName": "John Doe",
  "phoneNumber": "+1234567890",
  "roles": ["STAFF", "MANAGER"],
  "createdAt": "2023-06-01T12:00:00Z",
  "updatedAt": "2023-06-15T10:30:00Z"
}
```

## Restaurant Service

### Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET    | `/restaurants` | Get all restaurants |
| GET    | `/restaurants/{id}` | Get restaurant by ID |
| POST   | `/restaurants` | Create a new restaurant (admin only) |
| PUT    | `/restaurants/{id}` | Update restaurant |
| DELETE | `/restaurants/{id}` | Delete restaurant (admin only) |
| GET    | `/restaurants/{id}/staff` | Get restaurant staff |
| POST   | `/restaurants/{id}/staff` | Add staff to restaurant |
| DELETE | `/restaurants/{id}/staff/{userId}` | Remove staff from restaurant |

## Menu Service

### Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET    | `/restaurants/{restaurantId}/categories` | Get all menu categories |
| POST   | `/restaurants/{restaurantId}/categories` | Create a new category |
| PUT    | `/restaurants/{restaurantId}/categories/{id}` | Update category |
| DELETE | `/restaurants/{restaurantId}/categories/{id}` | Delete category |
| GET    | `/restaurants/{restaurantId}/items` | Get all menu items |
| GET    | `/restaurants/{restaurantId}/items/{id}` | Get menu item by ID |
| POST   | `/restaurants/{restaurantId}/items` | Create a new menu item |
| PUT    | `/restaurants/{restaurantId}/items/{id}` | Update menu item |
| DELETE | `/restaurants/{restaurantId}/items/{id}` | Delete menu item |

## Order Service

### Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET    | `/orders` | Get all orders (with filtering) |
| GET    | `/orders/{id}` | Get order by ID |
| POST   | `/orders` | Create a new order |
| PUT    | `/orders/{id}` | Update order |
| PUT    | `/orders/{id}/status` | Update order status |
| DELETE | `/orders/{id}` | Cancel order |
| GET    | `/orders/{id}/items` | Get order items |
| POST   | `/orders/{id}/items` | Add item to order |
| PUT    | `/orders/{id}/items/{itemId}` | Update order item |
| DELETE | `/orders/{id}/items/{itemId}` | Remove item from order |

### Sample Request/Response

#### Create a New Order

Request:
```
POST /api/v1/orders
Authorization: Bearer <jwt_token>
Content-Type: application/json

{
  "restaurantId": 1,
  "tableId": 5,
  "customerName": "Jane Smith",
  "notes": "Allergic to nuts",
  "items": [
    {
      "itemId": 101,
      "quantity": 2,
      "specialInstructions": "Extra spicy"
    },
    {
      "itemId": 205,
      "quantity": 1,
      "specialInstructions": ""
    }
  ]
}
```

Response:
```json
{
  "id": 1001,
  "orderNumber": "ORD-20230701-1001",
  "restaurantId": 1,
  "tableId": 5,
  "customerName": "Jane Smith",
  "status": "PENDING",
  "totalAmount": 34.97,
  "notes": "Allergic to nuts",
  "createdAt": "2023-07-01T18:30:00Z",
  "items": [
    {
      "id": 1,
      "itemId": 101,
      "name": "Spicy Chicken Wings",
      "quantity": 2,
      "unitPrice": 12.99,
      "subtotal": 25.98,
      "specialInstructions": "Extra spicy"
    },
    {
      "id": 2,
      "itemId": 205,
      "name": "Caesar Salad",
      "quantity": 1,
      "unitPrice": 8.99,
      "subtotal": 8.99,
      "specialInstructions": ""
    }
  ]
}
```

## Inventory Service

### Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET    | `/inventory` | Get all inventory items |
| GET    | `/inventory/{id}` | Get inventory item by ID |
| POST   | `/inventory` | Create a new inventory item |
| PUT    | `/inventory/{id}` | Update inventory item |
| DELETE | `/inventory/{id}` | Delete inventory item |
| GET    | `/inventory/low-stock` | Get low stock inventory items |
| POST   | `/inventory/{id}/adjust` | Adjust inventory quantity |
| GET    | `/suppliers` | Get all suppliers |
| POST   | `/suppliers` | Create a new supplier |
| GET    | `/suppliers/{id}` | Get supplier by ID |
| PUT    | `/suppliers/{id}` | Update supplier |
| DELETE | `/suppliers/{id}` | Delete supplier |

## Payment Service

### Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET    | `/payments` | Get all payments |
| GET    | `/payments/{id}` | Get payment by ID |
| POST   | `/payments` | Process a new payment |
| POST   | `/payments/{id}/refund` | Process a refund |
| GET    | `/orders/{orderId}/payments` | Get payments for an order |

### Sample Request/Response

#### Process a Payment

Request:
```
POST /api/v1/payments
Authorization: Bearer <jwt_token>
Content-Type: application/json

{
  "orderId": 1001,
  "amount": 34.97,
  "paymentMethod": "CREDIT_CARD",
  "cardDetails": {
    "cardNumber": "XXXX-XXXX-XXXX-4242",
    "expiryMonth": 12,
    "expiryYear": 2025,
    "cvv": "***"
  },
  "billingAddress": {
    "line1": "123 Main St",
    "city": "Anytown",
    "state": "CA",
    "postalCode": "12345",
    "country": "USA"
  }
}
```

Response:
```json
{
  "id": 501,
  "orderId": 1001,
  "transactionId": "txn_123456789",
  "amount": 34.97,
  "status": "COMPLETED",
  "paymentMethod": "CREDIT_CARD",
  "paymentDate": "2023-07-01T19:15:30Z"
}
```

## Analytics Service

### Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET    | `/analytics/sales` | Get sales analytics |
| GET    | `/analytics/orders` | Get order analytics |
| GET    | `/analytics/inventory` | Get inventory analytics |
| GET    | `/analytics/popular-items` | Get popular menu items |
| GET    | `/analytics/staff-performance` | Get staff performance metrics |

### Query Parameters

Analytics endpoints support various query parameters for filtering:

- `startDate`: Start date for analytics period (ISO format)
- `endDate`: End date for analytics period (ISO format)
- `restaurantId`: Filter by restaurant ID
- `groupBy`: Group results by day, week, month, or year
- `limit`: Limit the number of results

## Notification Service

### Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET    | `/notifications` | Get all notifications for the current user |
| GET    | `/notifications/{id}` | Get notification by ID |
| PUT    | `/notifications/{id}/read` | Mark notification as read |
| DELETE | `/notifications/{id}` | Delete notification |
| PUT    | `/notifications/read-all` | Mark all notifications as read |
| POST   | `/notifications/settings` | Update notification preferences |

## Error Handling

All APIs follow a consistent error response format:

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid input data",
  "timestamp": "2023-07-01T12:34:56Z",
  "path": "/api/v1/orders",
  "details": [
    "Order items cannot be empty",
    "Restaurant ID is required"
  ]
}
```

### Common Error Codes

- `400 Bad Request`: Invalid input data
- `401 Unauthorized`: Authentication required
- `403 Forbidden`: Insufficient permissions
- `404 Not Found`: Resource not found
- `409 Conflict`: Resource conflict (e.g., duplicate entry)
- `422 Unprocessable Entity`: Valid request but unable to process
- `500 Internal Server Error`: Server-side error

## Rate Limiting

To prevent abuse, API endpoints are rate-limited. The following headers are included in responses:

- `X-Rate-Limit-Limit`: The maximum number of requests allowed per time window
- `X-Rate-Limit-Remaining`: The number of requests remaining in the current time window
- `X-Rate-Limit-Reset`: The time when the current rate limit window resets (Unix timestamp)

## Pagination

List endpoints support pagination with the following query parameters:

- `page`: Page number (0-based, default: 0)
- `size`: Page size (default: 20)
- `sort`: Sort field and direction (e.g., `sort=createdAt,desc`)

Response includes pagination metadata:

```json
{
  "content": [
    // Array of items
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20,
    "sort": {
      "sorted": true,
      "unsorted": false
    }
  },
  "totalElements": 50,
  "totalPages": 3,
  "last": false,
  "first": true,
  "size": 20,
  "number": 0,
  "sort": {
    "sorted": true,
    "unsorted": false
  },
  "numberOfElements": 20,
  "empty": false
}
```

## Versioning

The API is versioned using URL path versioning (e.g., `/api/v1/resource`). When breaking changes are introduced, a new version will be released (e.g., `/api/v2/resource`).

## API Documentation

Interactive API documentation is available via Swagger UI at:

```
http://localhost:8080/swagger-ui.html
```

## Postman Collection

A Postman collection for testing the APIs is available in the repository:

```
/docs/postman/FSMS-API-Collection.json
```