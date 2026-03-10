# Secure Funds Transfer Application

A full-stack financial web application that allows authenticated users to securely transfer funds between bank accounts. The system demonstrates production-grade engineering practices including JWT-based authentication with token refresh, role-based authorization, atomic transaction processing, paginated transaction history, and a fully responsive Angular frontend with route animations.

---

## Live Demo

> **Live Demo Link:** [https://secure-funds-transfer-production.up.railway.app]

---

## Sample Credentials

Use the following accounts to explore the application:

| Username | Password     | Role  | Account Number | Initial Balance |
|----------|--------------|-------|----------------|-----------------|
| `ahmed`  | `ahmed@1234` | USER  | ACC-0000002    | $5,000.00       |
| `sara`   | `sara@1234`  | USER  | ACC-0000003    | $3,000.00       |
| `admin`  | `admin@1234` | ADMIN | ACC-0000001    | $10,000.00      |

> **Tip for testing:** Log in as `ahmed`, transfer funds to `ACC-0000003`, then log in as `sara` to see the received transaction appear in her history.

---

## Technologies Used

### Backend
- **Java 17** — language runtime
- **Spring Boot 3.2** — application framework
- **Spring Security** — authentication and authorization
- **Spring Data JPA / Hibernate** — ORM and data access
- **PostgreSQL** — relational database
- **Flyway** — database schema versioning and migrations
- **JJWT 0.11.5** — JWT token generation and validation
- **BCrypt** — password hashing
- **Lombok** — boilerplate reduction
- **Maven** — build and dependency management

### Frontend
- **Angular 17** — SPA framework
- **Angular Material** — UI component library
- **Angular Animations** — page and element transitions
- **RxJS** — reactive programming and HTTP stream management
- **TypeScript** — statically typed JavaScript

### Infrastructure
- **Docker & Docker Compose** — containerization and local orchestration
- **Nginx** — frontend static file serving and API reverse proxy
- **Neon / Supabase / Aiven** — cloud PostgreSQL (deployment)
- **Render / Railway** — backend deployment
- **Vercel / Netlify** — frontend deployment

---

## Project Structure

```
secure-funds-transfer/
├── Dockerfile
├── .env.example
├── API_DOCUMENTATION.md
│
├── transfer-app-backend/
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/transferapp/
│       │   ├── controller/     AuthController, AccountController,
│       │   │                   TransferController, UserController, SpaController
│       │   ├── service/        AuthService, AccountService,
│       │   │                   TransferService, UserService
│       │   ├── repository/     UserRepository, AccountRepository,
│       │   │                   TransactionRepository
│       │   ├── entity/         User, Account, Transaction
│       │   ├── dto/            LoginRequest, AuthResponse,
│       │   │                   TransferRequest, TransferResponse,
│       │   │                   TransactionResponse, AccountResponse,
│       │   │                   UserProfileResponse, PagedResponse ,ApiResponse
│       │   ├── security/       JwtService, JwtAuthenticationFilter,
│       │   │                   SecurityConfig
│       │   ├── config/         ApplicationConfig
│       │   ├── exception/      GlobalExceptionHandler
│       │   └── enums/          Role
│       └── resources/
│           ├── application.yml
│           └── db/migration/   V1–V4 Flyway SQL scripts
│
└── transfer-app-frontend/
    ├── nginx.conf
    └── src/app/
        ├── core/       AuthService, JwtInterceptor,
        │               AuthGuard, animations.ts
        ├── pages/      login/, dashboard/, transfer/,
        │               history/, profile/
        ├── services/   AccountService, TransferService, UserService
        ├── models/     TypeScript interfaces mirroring backend DTOs
        └── shared/     NavbarComponent, SharedModule
```

---

## API Endpoints Summary

| Method | Endpoint                          | Auth     | Description                          |
|--------|-----------------------------------|----------|--------------------------------------|
| POST   | `/api/auth/login`                 | Public   | Authenticate and receive tokens      |
| POST   | `/api/auth/refresh`               | Public   | Refresh expired access token         |
| GET    | `/api/accounts/me`                | Required | Get authenticated user's account     |
| POST   | `/api/transfers`                  | Required | Execute a fund transfer              |
| GET    | `/api/transfers/history`          | Required | All transactions (paginated)         |
| GET    | `/api/transfers/history/sent`     | Required | Sent transactions only (paginated)   |
| GET    | `/api/transfers/history/received` | Required | Received transactions only (paginated) |
| GET    | `/api/users/profile`              | Required | Full profile with transaction stats  |

> For full request/response examples, see [API_DOCUMENTATION.md](https://github.com/AhmedRagabMogoda/secure-funds-transfer/blob/master/docs/API_DOCUMENTATION.md).

---

---

# Local Setup — Option A: Docker (Single Container)

This project uses a **single multi-stage Dockerfile** that:

1. Builds the Angular frontend
2. Embeds the compiled frontend into the Spring Boot static resources
3. Builds the Spring Boot JAR
4. Runs the application in a lightweight runtime container

This approach ensures that the **frontend and backend are deployed together as a single service**, which simplifies deployment on platforms such as Railway.

---

## Prerequisites

* Docker Desktop installed and running

Verify installation:

```bash
docker --version
```

---

## Step 1 — Clone the repository

```bash
git clone https://github.com/your-username/secure-funds-transfer.git
cd secure-funds-transfer
```

---

## Step 2 — Create your environment file

```bash
cp .env.example .env
```

Open `.env` and set your values.

For production deployments, generate a strong JWT secret:

```bash
openssl rand -base64 32
```

Example `.env` values:

```env
DB_URL=jdbc:postgresql://localhost:5432/transfer_db
DB_USERNAME=transfer_user
DB_PASSWORD=your_password
JWT_SECRET=your_generated_secret
JWT_EXPIRATION=900000
REFRESH_TOKEN_EXPIRATION=604800000
```

---

## Step 3 — Build the Docker image

From the project root directory:

```bash
docker build -t secure-funds-transfer .
```

During the build process Docker will:

1. Install frontend dependencies
2. Build the Angular production bundle
3. Copy the compiled frontend into Spring Boot `static` resources
4. Build the Spring Boot JAR
5. Produce a lightweight runtime image

The first build usually takes **3–5 minutes**.

---

## Step 4 — Run the application

```bash
docker run -p 8080:8080 --env-file .env secure-funds-transfer
```

The application will start on:

```
http://localhost:8080
```

The Angular frontend is served directly by Spring Boot, and all API endpoints are available under:

```
http://localhost:8080/api/*
```

---

## Stop the container

Press:

```
CTRL + C
```

Or stop it from another terminal:

```bash
docker ps
docker stop <container_id>
```

---

# Local Setup — Option B: Manual (Without Docker)

Use this approach if you want to run the services directly for active development.

### Prerequisites

* Java 17 (verify: `java -version`)
* Maven 3.9+ (verify: `mvn -version`)
* Node.js 20+ and npm (verify: `node -v`)
* Angular CLI 17: `npm install -g @angular/cli`
* PostgreSQL 15+ running locally

---

## Database Setup

Connect to your PostgreSQL instance and create the database and user:

```sql
CREATE DATABASE transfer_db;
CREATE USER transfer_user WITH PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE transfer_db TO transfer_user;
```

Flyway will automatically run the migration scripts (`V1` through `V4`) on the first backend startup and seed the sample users.

---

## Backend Setup

Navigate to the backend directory:

```bash
cd backend
```

Set environment variables.

Linux/macOS:

```bash
export DB_URL=jdbc:postgresql://localhost:5432/transfer_db
export DB_USERNAME=transfer_user
export DB_PASSWORD=your_password
export JWT_SECRET=$(openssl rand -base64 32)
export JWT_EXPIRATION=900000
export REFRESH_TOKEN_EXPIRATION=604800000
```

Windows (PowerShell):

```powershell
$env:DB_URL = "jdbc:postgresql://localhost:5432/transfer_db"
$env:DB_USERNAME = "transfer_user"
$env:DB_PASSWORD = "your_password"
$env:JWT_SECRET = "your_generated_secret"
$env:JWT_EXPIRATION = "900000"
$env:REFRESH_TOKEN_EXPIRATION = "604800000"
```

Build and run the backend:

```bash
mvn clean package -DskipTests
mvn spring-boot:run
```

The backend starts on:

```
http://localhost:8080
```

---

## Frontend Setup (Development Mode)

Navigate to the frontend directory:

```bash
cd frontend
```

Install dependencies:

```bash
npm install --legacy-peer-deps
```

Start the Angular development server:

```bash
ng serve
```

The frontend will run on:

```
http://localhost:4200
```

The `proxy.conf.json` configuration automatically forwards `/api/*` requests to:

```
http://localhost:8080
```

---

Log in with any of the sample credentials listed above.

---

## Environment Variables Reference

| Variable                   | Required | Default    | Description                                        |
|----------------------------|----------|------------|----------------------------------------------------|
| `DB_URL`                   | Yes      | —          | Full JDBC connection string for PostgreSQL         |
| `DB_USERNAME`              | Yes      | —          | PostgreSQL username                                |
| `DB_PASSWORD`              | Yes      | —          | PostgreSQL password                                |
| `JWT_SECRET`               | Yes      | —          | Base64-encoded HMAC-SHA256 signing key (min 32 chars) |
| `JWT_EXPIRATION`           | No       | `900000`   | Access token lifetime in milliseconds (15 minutes) |
| `REFRESH_TOKEN_EXPIRATION` | No       | `604800000`| Refresh token lifetime in milliseconds (7 days)    |

---

## Authentication Flow

```
User submits credentials
        ↓
POST /api/auth/login
        ↓
Backend validates with BCrypt
        ↓
Issues Access Token (15 min) + Refresh Token (7 days)
        ↓
Angular stores tokens in localStorage
        ↓
JwtInterceptor attaches token to every request
        ↓
Token expires → 401 received
        ↓
Interceptor calls POST /api/auth/refresh automatically
        ↓
New access token issued → original request retried
        ↓
User never sees the session expire
```

---

## Key Design Decisions

**Atomic transfers** — The `TransferService.transfer()` method is annotated with `@Transactional`. Balance deduction, balance credit, and transaction record insertion are committed as a single unit or rolled back entirely if any step fails. Partial transfers are impossible.

**Sender identity from JWT** — The sender account is always derived from the authenticated principal in the `SecurityContext`, never from the request body. This prevents any possibility of spoofing the sender identity.

**Paginated history** — Transaction history is served from three dedicated endpoints (all, sent, received), each backed by a separate JPQL query with Spring Data `Page<T>`. This avoids loading unbounded datasets into memory as account history grows.

**Refresh race condition prevention** — The `JwtInterceptor` uses a `BehaviorSubject` to gate concurrent requests during a token refresh. Only one refresh call is made; all other requests queue behind it and are replayed with the new token once it arrives.

---
