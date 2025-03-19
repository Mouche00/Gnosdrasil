# Stage 1: Build with Maven
FROM maven:3.8.6-eclipse-temurin-17-alpine AS builder

WORKDIR /app

# Copy only the POM file first to leverage Docker cache
COPY pom.xml .

# Download dependencies and plugins (cache in Docker layer)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build package with cached dependencies
RUN mvn package -DskipTests -Dmaven.javadoc.skip=true -Dmaven.source.skip=true -B

# Stage 2: Runtime image
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Set default ARG for JAR name
ARG JAR_FILE=gd-job-scraper-service.jar

# Copy built JAR from builder
COPY --from=builder /app/target/*.jar ./${JAR_FILE}

EXPOSE 8761

ENTRYPOINT ["java", "-jar", "gd-job-scraper-service.jar"]