# Use an official OpenJDK runtime as a parent image
FROM openjdk:12-jdk-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the project’s build file to the container (assuming the build file is named "app.jar")
COPY target/javaproject.jar /app/javaproject.jar

# Expose port 8080 to the outside world
EXPOSE 8081

# Set the startup command to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/chunglnt0609/javaproject.jar"]
