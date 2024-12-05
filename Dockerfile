# Build stage
FROM maven:3.9.4-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Environment variables with default values
ENV SERVER_PORT=8082
ENV AZURE_ENDPOINT=https://moderationreaccon.cognitiveservices.azure.com/
ENV AZURE_KEY=5VSDQSYGu6rj9tgfbnA3SWULkZShPDFq4rnPrl70Bx0rLErsAKmLJQQJ99ALACYeBjFXJ3w3AAAFACOGzZek

EXPOSE ${SERVER_PORT}

ENTRYPOINT ["java", "-jar", "app.jar"]
