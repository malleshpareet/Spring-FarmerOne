# Use a base image with JDK
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy the jar file into the container
COPY target/*.jar app.jar

# Expose port
EXPOSE 8080

# Run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]
