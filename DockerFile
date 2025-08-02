# Use OpenJDK 21 base image
FROM eclipse-temurin:21-jdk-alpine

# Set the name of your JAR file
ARG JAR_FILE=target/achievepro.jar

# Copy the JAR file into the image
COPY ${JAR_FILE} app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "/app.jar"]
