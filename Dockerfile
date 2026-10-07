# ==============================
# Stage 1: Build the application
# ==============================
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Copy Maven wrapper
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Give execute permission to Maven wrapper
RUN chmod +x mvnw

# Download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src src

# Build Spring Boot JAR
RUN ./mvnw clean package -DskipTests


# ==============================
# Stage 2: Run the application
# ==============================
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy JAR from build stage
COPY --from=build /app/target/*.jar app.jar

# Spring Boot application port
EXPOSE 8082

# Start application
ENTRYPOINT ["java", "-jar", "app.jar"]