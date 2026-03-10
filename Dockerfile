# ================================================================
# Single Dockerfile for Railway Deployment
#
# Structure expected:
#   project-root/
#   ├── Dockerfile          ← this file
#   ├── frontend/           ← Angular project
#   │   ├── package.json
#   │   ├── angular.json
#   │   └── src/
#   └── backend/            ← Spring Boot project
#       ├── pom.xml
#       └── src/
#
# What this file does in order:
#   Stage 1 → Build Angular  (Node 20)
#   Stage 2 → Build Spring Boot JAR with Angular embedded (Maven + JDK 17)
#   Stage 3 → Run the JAR (JRE 17 only — smallest possible image)
# ================================================================


# ----------------------------------------------------------------
# Stage 1 — Build Angular
# ----------------------------------------------------------------
FROM node:20-alpine AS frontend-builder

WORKDIR /frontend

# Copy manifests first — Docker caches this layer until
# package.json or package-lock.json changes
COPY frontend/package*.json ./

RUN npm ci --legacy-peer-deps

# Copy full frontend source
COPY frontend/ ./

# Build for production
# Output goes to: dist/transfer-app-frontend/browser/
RUN npm run build:prod


# ----------------------------------------------------------------
# Stage 2 — Build Spring Boot JAR
# ----------------------------------------------------------------
FROM maven:3.9.6-eclipse-temurin-17 AS backend-builder

WORKDIR /backend

# Copy pom.xml and resolve dependencies first (cached layer)
COPY backend/pom.xml ./
RUN mvn dependency:go-offline -B

# Copy Angular compiled output into Spring Boot static resources
# BEFORE the JAR is built so Angular files are embedded inside it.
# Spring Boot serves everything in classpath:/static/ automatically.
COPY --from=frontend-builder \
     /frontend/dist/transfer-app-frontend/browser/ \
     src/main/resources/static/

# Copy backend source
COPY backend/src ./src

# Build the JAR — static files are now inside BOOT-INF/classes/static/
RUN mvn clean package -DskipTests -B


# ----------------------------------------------------------------
# Stage 3 — Runtime
# Only the JRE — no Node, no Maven, no build tools
# ----------------------------------------------------------------
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Non-root user — security best practice
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copy only the fat JAR from the builder stage
COPY --from=backend-builder /backend/target/*.jar app.jar

RUN chown appuser:appgroup app.jar

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]