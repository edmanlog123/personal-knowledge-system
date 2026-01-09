# ---- Build stage ----
    FROM gradle:8.5-jdk21 AS builder

    WORKDIR /app
    
    # Copy Gradle files first for caching
    COPY build.gradle.kts settings.gradle.kts gradle.properties ./
    COPY gradle ./gradle
    
    # Download dependencies
    RUN gradle dependencies --no-daemon
    
    # Copy source
    COPY src ./src
    
    # Build fat jar
    RUN gradle shadowJar --no-daemon
    
    # ---- Runtime stage ----
    FROM eclipse-temurin:21-jre
    
    WORKDIR /app
    
    # Copy built jar
    COPY --from=builder /app/build/libs/*.jar app.jar
    
    EXPOSE 8080
    
    ENTRYPOINT ["java", "-jar", "app.jar"]
    