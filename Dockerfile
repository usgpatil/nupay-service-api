
# ==========================================
# Stage 1: Build the Spring Boot application
# ==========================================
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Copy Maven Wrapper files
COPY mvnw .
COPY .mvn/ .mvn/
COPY pom.xml

# Run Maven Wrapper through the shell
# This avoids depending on executable permission for mvnw
RUN sh mvnw -B dependency:go-offline

# Copy application source code
COPY src/ src/

# Build the application JAR
RUN sh mvnw -B clean package -DskipTests


# ==========================================
# Stage 2: Run the Spring Boot application
# ==========================================
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the generated JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Application port
EXPOSE 8082

# Start the application
ENTRYPOINT ["java", "-jar", "app.jar"]
