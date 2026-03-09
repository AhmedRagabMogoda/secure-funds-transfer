# ============================================================
# Stage 1: Build
# Uses Maven + JDK 17 to compile and package the application
# ============================================================
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy dependency manifests first to leverage Docker layer cache.
# Dependencies are only re-downloaded when pom.xml changes.
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source and build the JAR (skip tests — handled in CI)
COPY src ./src
RUN mvn clean package -DskipTests -B

# ============================================================
# Stage 2: Runtime
# Minimal JRE image — no build tools included
# ============================================================
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Create a non-root user for security best practice
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copy only the fat JAR from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Set ownership
RUN chown appuser:appgroup app.jar

USER appuser

EXPOSE 8080

# JVM flags:
#   -XX:+UseContainerSupport     respect container CPU/memory limits
#   -XX:MaxRAMPercentage=75.0    use up to 75% of container memory for heap
#   -Djava.security.egd          faster startup in containers
ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]
