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
