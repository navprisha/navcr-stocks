FROM ubuntu:22.04

# Avoid prompts from apt
ENV DEBIAN_FRONTEND=noninteractive

# Install Java 17 and other necessary tools
RUN apt-get update && \
    apt-get install -y openjdk-17-jdk wget

# Set working directory
WORKDIR /app

# Copy the project files
COPY . .

# Grant execution rights to the gradlew script
RUN chmod +x ./gradlew

# Build the application
# The build needs to be run here to package the application
RUN ./gradlew build --no-daemon

# Expose the port the app runs on
EXPOSE 8080

# Define the command to run the application
CMD ["java", "-jar", "build/libs/yahoo-finance-streamer-0.0.1-SNAPSHOT.jar"]
