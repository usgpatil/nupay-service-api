
# ==========================================
# Stage 1: Build the Spring Boot application
# ==========================================
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Copy Maven Wrapper and project configuration
COPY mvnw .
COPY .mvn/ .mvn/
COPY pom.xml .

# Copy Java source code
COPY src/ src/

# Resolve dependencies and build the application
RUN sh mvnw -B clean package -DskipTests


# ==========================================
# Stage 2: Run the Spring Boot application
# ==========================================
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the generated Spring Boot JAR
COPY --from=build /app/target/*.jar app.jar

# Application port
EXPOSE 8082

# Start the application
ENTRYPOINT ["java", "-jar", "app.jar"]
