# syntax=docker/dockerfile:1

# Build stage
FROM eclipse-temurin:21-jdk-alpine as builder
WORKDIR /app

# Copy source code into the container
COPY . .

# Grant execute permissions to the Maven Wrapper script
# This is the line that fixes the "Permission denied" error.
RUN chmod +x ./mvnw

# Build the JAR file, skipping tests
RUN ./mvnw clean package -DskipTests

# Package stage
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Set the entrypoint to run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
