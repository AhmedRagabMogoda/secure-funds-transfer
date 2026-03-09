# API Documentation
## Secure Funds Transfer Application

**Base URL (Local):** `http://localhost:8080`  
**Base URL (Docker):** `http://localhost:8080`  
**Content-Type:** `application/json`  
**Authentication:** `Bearer <accessToken>` (required on all endpoints except auth)

---

## Standard Response Format

Every endpoint returns an `ApiResponse<T>` envelope:

```json
{
  "success": true,
  "message": "Human-readable result message",
  "data": { },
  "timestamp": "2026-03-08T14:00:00"
}
```

On error:
```json
{
  "success": false,
  "message": "Descriptive error message",
  "data": null,
  "timestamp": "2026-03-08T14:00:00"
}
```

---

## Authentication Endpoints

### POST /api/auth/login

Authenticates a user and returns an access token and refresh token.  
**Access:** Public

**Request Body:**
```json
{
  "username": "ahmed",
  "password": "Ahmed@1234"
}
```

**Response 200 OK:**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "role": "USER"
  },
  "timestamp": "2026-03-08T14:00:00"
}
```

**Response 401 Unauthorized:**
```json
{
  "success": false,
  "message": "Invalid username or password",
  "data": null,
  "timestamp": "2026-03-08T14:00:00"
}
```

**Response 400 Bad Request (validation):**
```json
{
  "success": false,
  "message": "Validation failed",
  "data": null,
  "timestamp": "2026-03-08T14:00:00"
}
```

---

### POST /api/auth/refresh

Issues a new access token using a valid refresh token.  
Called automatically by the Angular interceptor — not exposed in the UI.  
**Access:** Public

**Request Body:**
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Response 200 OK:**
```json
{
  "success": true,
  "message": "Token refreshed successfully",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...(new)",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...(unchanged)",
    "role": "USER"
  },
  "timestamp": "2026-03-08T14:00:00"
}
```

**Response 400 Bad Request:**
```json
{
  "success": false,
  "message": "Invalid or expired refresh token",
  "data": null,
  "timestamp": "2026-03-08T14:00:00"
}
```

---

## Account Endpoints

### GET /api/accounts/me

Returns the authenticated user's account details.  
**Access:** Authenticated (USER, ADMIN)

**Headers:**
```
Authorization: Bearer <accessToken>
```

**Response 200 OK:**
```json
{
  "success": true,
  "message": "Account retrieved successfully",
  "data": {
    "accountNumber": "ACC-0000002",
    "balance": 5000.0000,
    "ownerUsername": "ahmed",
    "createdAt": "2026-03-07T12:00:00"
  },
  "timestamp": "2026-03-08T14:00:00"
}
```

**Response 401 Unauthorized** — missing or expired token.

**Response 409 Conflict:**
```json
{
  "success": false,
  "message": "No account found for user: ahmed",
  "data": null,
  "timestamp": "2026-03-08T14:00:00"
}
```

---

## Transfer Endpoints

### POST /api/transfers

Executes a fund transfer from the authenticated user's account to a specified receiver.  
The sender account is derived from the JWT — not from the request body.  
**Access:** Authenticated (USER, ADMIN)

**Headers:**
```
Authorization: Bearer <accessToken>
```

**Request Body:**
```json
{
  "receiverAccountNumber": "ACC-0000003",
  "amount": 250.00
}
```

**Response 200 OK:**
```json
{
  "success": true,
  "message": "Transfer completed successfully",
  "data": {
    "senderAccountNumber": "ACC-0000002",
    "receiverAccountNumber": "ACC-0000003",
    "amount": 250.00,
    "newBalance": 4750.0000,
    "status": "SUCCESS",
    "createdAt": "2026-03-08T14:00:00"
  },
  "timestamp": "2026-03-08T14:00:00"
}
```

**Response 400 — Insufficient funds:**
```json
{
  "success": false,
  "message": "Insufficient funds. Available balance: 4750.0000",
  "data": null,
  "timestamp": "2026-03-08T14:00:00"
}
```

**Response 400 — Receiver not found:**
```json
{
  "success": false,
  "message": "Receiver account not found: ACC-9999999",
  "data": null,
  "timestamp": "2026-03-08T14:00:00"
}
```

**Response 400 — Self-transfer:**
```json
{
  "success": false,
  "message": "You cannot transfer funds to your own account",
  "data": null,
  "timestamp": "2026-03-08T14:00:00"
}
```

---

### GET /api/transfers/history

Returns a paginated list of all transactions (sent and received) for the authenticated user.  
**Access:** Authenticated (USER, ADMIN)

**Headers:**
```
Authorization: Bearer <accessToken>
```

**Query Parameters:**

| Parameter | Type    | Default | Description               |
|-----------|---------|---------|---------------------------|
| `page`    | integer | `0`     | Zero-based page index     |
| `size`    | integer | `10`    | Number of records per page|

**Example Request:**
```
GET /api/transfers/history?page=0&size=10
```

**Response 200 OK:**
```json
{
  "success": true,
  "message": "Transaction history retrieved",
  "data": {
    "content": [
      {
        "id": 1,
        "senderAccountNumber": "ACC-0000002",
        "receiverAccountNumber": "ACC-0000003",
        "amount": 250.00,
        "status": "SUCCESS",
        "createdAt": "2026-03-08T14:00:00",
        "direction": "SENT"
      },
      {
        "id": 2,
        "senderAccountNumber": "ACC-0000001",
        "receiverAccountNumber": "ACC-0000002",
        "amount": 100.00,
        "status": "SUCCESS",
        "createdAt": "2026-03-08T13:30:00",
        "direction": "RECEIVED"
      }
    ],
    "currentPage": 0,
    "totalPages": 3,
    "totalElements": 25,
    "pageSize": 10,
    "hasNext": true,
    "hasPrevious": false
  },
  "timestamp": "2026-03-08T14:00:00"
}
```

---

### GET /api/transfers/history/sent

Returns a paginated list of only outgoing (sent) transactions for the authenticated user.  
**Access:** Authenticated (USER, ADMIN)

**Headers:**
```
Authorization: Bearer <accessToken>
```

**Query Parameters:** same as `/history` (`page`, `size`)

**Example Request:**
```
GET /api/transfers/history/sent?page=0&size=5
```

**Response 200 OK:**
```json
{
  "success": true,
  "message": "Sent transactions retrieved",
  "data": {
    "content": [
      {
        "id": 1,
        "senderAccountNumber": "ACC-0000002",
        "receiverAccountNumber": "ACC-0000003",
        "amount": 250.00,
        "status": "SUCCESS",
        "createdAt": "2026-03-08T14:00:00",
        "direction": "SENT"
      }
    ],
    "currentPage": 0,
    "totalPages": 2,
    "totalElements": 8,
    "pageSize": 5,
    "hasNext": true,
    "hasPrevious": false
  },
  "timestamp": "2026-03-08T14:00:00"
}
```

---

### GET /api/transfers/history/received

Returns a paginated list of only incoming (received) transactions for the authenticated user.  
**Access:** Authenticated (USER, ADMIN)

**Query Parameters:** same as `/history` (`page`, `size`)

**Example Request:**
```
GET /api/transfers/history/received?page=0&size=5
```

**Response 200 OK:**
```json
{
  "success": true,
  "message": "Received transactions retrieved",
  "data": {
    "content": [
      {
        "id": 2,
        "senderAccountNumber": "ACC-0000001",
        "receiverAccountNumber": "ACC-0000002",
        "amount": 100.00,
        "status": "SUCCESS",
        "createdAt": "2026-03-08T13:30:00",
        "direction": "RECEIVED"
      }
    ],
    "currentPage": 0,
    "totalPages": 1,
    "totalElements": 3,
    "pageSize": 5,
    "hasNext": false,
    "hasPrevious": false
  },
  "timestamp": "2026-03-08T14:00:00"
}
```

---

## User Endpoints

### GET /api/users/profile

Returns the full profile of the authenticated user, including account details and aggregated transaction statistics.  
**Access:** Authenticated (USER, ADMIN)

**Headers:**
```
Authorization: Bearer <accessToken>
```

**Response 200 OK:**
```json
{
  "success": true,
  "message": "Profile retrieved successfully",
  "data": {
    "username": "ahmed",
    "role": "USER",
    "accountNumber": "ACC-0000002",
    "balance": 4750.0000,
    "memberSince": "2026-03-07T12:00:00",
    "totalTransactionCount": 11,
    "totalSent": 1250.00,
    "totalReceived": 500.00
  },
  "timestamp": "2026-03-08T14:00:00"
}
```

---

## Error Reference

| HTTP Status | Meaning                          | When it occurs                                   |
|-------------|----------------------------------|--------------------------------------------------|
| `200`       | OK                               | Request completed successfully                   |
| `400`       | Bad Request                      | Validation failure, business rule violation      |
| `401`       | Unauthorized                     | Missing token, expired token, wrong credentials  |
| `404`       | Not Found                        | User or account not found                        |
| `409`       | Conflict                         | Account state conflict                           |
| `500`       | Internal Server Error            | Unexpected server-side error                     |

---

## Token Strategy

| Token         | Lifetime      | Storage          | Purpose                                |
|---------------|---------------|------------------|----------------------------------------|
| Access Token  | 15 minutes    | `localStorage`   | Authenticates API requests             |
| Refresh Token | 7 days        | `localStorage`   | Issues new access tokens automatically |

The Angular `JwtInterceptor` attaches the access token to every request automatically. When a `401` is received, it calls `POST /api/auth/refresh` transparently and retries the original request — the user never sees the session expire.

---
