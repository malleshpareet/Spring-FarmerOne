# # Use a base image with JDK
# FROM eclipse-temurin:17-jdk
#
# # Set working directory
# WORKDIR /app
#
# # Copy the jar file into the container
# COPY target/*.jar app.jar
#
# # Expose port
# EXPOSE 8080
#
# # Run the jar
# ENTRYPOINT ["java", "-jar", "app.jar"]
FROM eclipse-temurin:17-jdk as build
COPY . .
RUN mvn clean package -DskipTests

COPY --from=build /target/FarmerOne-0.0.1-SNAPSHOT.jar FarmerOne.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "FarmerOne.jar"]
# Stage 1: Build the application
FROM maven:3.9.3-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom and source code
COPY pom.xml .
COPY src ./src

# Build the JAR, skipping tests
RUN mvn clean package -DskipTests

# Stage 2: Create the runtime image
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/FarmerOne-0.0.1-SNAPSHOT.jar FarmerOne.jar

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "FarmerOne.jar"]
