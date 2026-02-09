# Use Java 21 (matching your pom.xml java.version)
#FROM eclipse-temurin:21-jdk-alpine
FROM amazoncorretto:21-alpine-jdk

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR and rename it to 'interviewchatbot.jar' for simplicity
COPY target/interviewchatbot-0.0.1-SNAPSHOT.jar interviewchatbot.jar

# Copy your external config folder into the container
COPY config/ /app/config/

# Fix: Use the name 'interviewchatbot.jar' here to match the COPY command above
ENTRYPOINT ["java", "-Dspring.config.location=file:/app/config/application.properties", "-jar", "interviewchatbot.jar"]