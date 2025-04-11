FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

# Copy the prebuilt fat JAR
COPY build/libs/rangiffler-photo-service-all.jar app.jar

EXPOSE 4003
ENTRYPOINT ["java", "-jar", "app.jar"]