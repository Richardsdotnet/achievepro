# syntax=docker/dockerfile:1

# Build stage
FROM eclipse-temurin:21-jdk-alpine as builder
WORKDIR /app

# Copy source
COPY . .

# Build the JAR
RUN ./mvnw clean package -DskipTests

# Package stage
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
