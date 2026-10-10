
# ==========================================
# Stage 1: Build the Spring Boot application
# ==========================================
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Copy Maven Wrapper files
COPY mvnw .
COPY .mvn/ .mvn/
COPY pom.xml .

# Download Maven dependencies
RUN sh mvnw -B dependency:go-offline

# Copy application source code
COPY src/ src/

# Build the Spring Boot JAR
RUN sh mvnw -B clean package -DskipTests


# ==========================================
# Stage 2: Run the application
# ==========================================
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the generated JAR
COPY --from=build /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8082

# Start the application
ENTRYPOINT ["java", "-jar", "app.jar"]